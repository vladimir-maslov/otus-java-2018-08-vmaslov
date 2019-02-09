package ru.otus.l15.messageSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());
    private static final int DEFAULT_STEP_TIME = 10;

    private final List<Thread> workers;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;

    private boolean isStarted = false;

    public MessageSystem() {
        workers = new ArrayList<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
        start(addressee);
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }

    public void start(Addressee addressee) {
        String name = "MS-worker-" + addressee.getAddress().getId();
        Thread thread = new Thread(() -> {
            LinkedBlockingQueue<Message> queue = messagesMap.get(addressee.getAddress());
            while (true) {
                try {
                    Message message = queue.take();
                    message.exec(addressee);
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.add(thread);
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }
}
