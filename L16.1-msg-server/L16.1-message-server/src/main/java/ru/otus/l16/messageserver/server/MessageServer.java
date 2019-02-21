package ru.otus.l16.messageserver.server;

import ru.otus.l16.messageserver.app.MsgWorker;
import ru.otus.l16.messageserver.channel.ServerSocketMsgWorker;
import ru.otus.l16.messageserver.channel.SocketMsgWorker;
import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.MessageJson;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MessageServer {

    private static final Logger logger = Logger.getLogger(MessageServer.class.getName());

    private static final String MESSAGE_SERVER_NAME = "MessageServer";

    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5050;

    private static int DELAY_WAIT_MS = 100;

    private final ExecutorService executor;
    private final List<MsgWorker> workers;
    private final Map<Address, MsgWorker> workersByAdressMap;

    public MessageServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        workers = new CopyOnWriteArrayList<>();
        workersByAdressMap = new ConcurrentHashMap<>();
    }

    public void start() throws Exception {
        executor.submit(this::mainLoop);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Allow the socket to be bound even though a previous connection is in a timeout state
            serverSocket.setReuseAddress(true);

            logger.info("Server started on port: " + serverSocket.getLocalPort());

            while (!executor.isShutdown()) {
                Socket socket = serverSocket.accept();
                SocketMsgWorker worker = new ServerSocketMsgWorker(socket);
                worker.init();
                workers.add(worker);
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void mainLoop() {
        while (true) {
            for (MsgWorker worker : workers) {
                MessageJson message = (MessageJson) worker.poll();

                while (message != null) {
                    workersByAdressMap.putIfAbsent(message.getFrom(), worker);

                    if (workersByAdressMap.containsKey(message.getTo())) {
                        MsgWorker toWorker = workersByAdressMap.get(message.getTo());
                        toWorker.send(message);
                    } else if (!message.getTo().getId().equals(MESSAGE_SERVER_NAME)) {
                        worker.refund(message);
                    }

                    message = (MessageJson) worker.poll();
                }
            }
            try {
                Thread.sleep(DELAY_WAIT_MS);
            } catch (InterruptedException e) {
                logger.severe("Interrupted " + e.toString());
            }
        }
    }
}
