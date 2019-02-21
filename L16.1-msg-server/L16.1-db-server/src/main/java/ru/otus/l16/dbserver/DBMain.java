package ru.otus.l16.dbserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import ru.otus.l16.dbserver.dataset.UserDataSet;
import ru.otus.l16.dbserver.db.DBService;
import ru.otus.l16.messageserver.channel.ManagedMsgSocketWorker;
import ru.otus.l16.messageserver.channel.SocketMsgWorker;
import ru.otus.l16.messageserver.message.*;
import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.Message;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

@Service
public class DBMain {
    private static final Logger logger = Logger.getLogger("DBServer");

    private static final String HOST = "localhost";
    private static final int PORT = 5050;

    private Address address;
    private DBService dbService;

    public static void main(String[] args) throws IOException {
        configureLogger();
        new DBMain().start();
    }

    private static void configureLogger() {
        try {
            Handler fh = new FileHandler("DBServer.log");
            logger.addHandler(fh);

            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            fh.setLevel(Level.WARNING);
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        dbService = context.getBean("dbService", DBService.class);
        address = dbService.getAddress();

        logger.info("[DBServer] [Start]");

        SocketMsgWorker client = new ManagedMsgSocketWorker(HOST, PORT);
        try {
            client.init();
            client.send(new MsgPing(address, new Address("MessageServer"), "ping"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Message message = client.take();
                    logger.info("[DBServer] [Receive] Message:" + message.toString());

                    Message result = null;

                    if (message instanceof MsgAddUser) {
                        result = processMessageAddUser((MsgAddUser) message);
                    } else if (message instanceof MsgGetUserInfo) {
                        result = processMessageGetUserInfo((MsgGetUserInfo) message);
                    }

                    if (result != null) {
                        client.send(result);
                        logger.info("[DBServer] [Send] Message: " + result.toString());
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
    }

    /**
     * Process "Add user" message (MsgAddUser)
     *
     * @param message
     * @return
     */
    private Message processMessageAddUser(MsgAddUser message) {
        UserDataSet user = new UserDataSet(message.getName(), message.getAge());

        dbService.save(user);

        return new MsgAddUserAnswer(message.getTo(),
                message.getFrom(),
                message.getSessionId(),
                user.getId(),
                user.getName());
    }

    /**
     * Process "Get user info" message (MsgGetUserInfo)
     *
     * @param message
     * @return
     */
    private Message processMessageGetUserInfo(MsgGetUserInfo message) {
        UserDataSet user = null;
        try {
            user = dbService.read(message.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MsgGetUserInfoAnswer(message.getTo(),
                message.getFrom(),
                message.getSessionId(),
                user.getId(),
                user.getName());
    }
}