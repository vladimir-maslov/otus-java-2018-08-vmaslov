package ru.otus.l16.frontend.app;

import ru.otus.l16.messageserver.channel.ManagedMsgSocketWorker;
import ru.otus.l16.messageserver.channel.SocketMsgWorker;
import ru.otus.l16.messageserver.message.MsgAddUserAnswer;
import ru.otus.l16.messageserver.message.MsgGetUserInfoAnswer;
import ru.otus.l16.messageserver.message.MsgPing;
import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.Addressee;
import ru.otus.l16.messageserver.messageSystem.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MessageServerClient {

    private static final Logger logger = Logger.getLogger("MessageServerClient");

    private static final String HOST = "localhost";
    private static final int PORT = 5050;

    private static final int MAX_MESSAGES_COUNT = 10;

    private SocketMsgWorker client;

    private FrontendService frontendService;
    private ExecutorService executor;

    public MessageServerClient() {
    }

    public void setFrontend(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    public void send(Message msg) {
        if (client != null)
            client.send(msg);
    }

    public void start() {
        if (client != null)
            return;
        try {
            client = new ManagedMsgSocketWorker(HOST, PORT);
        } catch (IOException e1) {
            e1.printStackTrace();
            logger.info(e1.getMessage());
            return;
        }

        client.init();

        logger.info("MessageServerClient started on port: " + PORT);

        client.send(new MsgPing(((Addressee) frontendService).getAddress(),
                new Address("MessageServer"), "ping"));

        executor = Executors.newSingleThreadExecutor();
        executor.execute(this::workLoop);
        //executor.shutdown();
    }

    private void workLoop() {
        int count = 0;
        while (count < MAX_MESSAGES_COUNT) {
            Message msg;
            try {
                msg = client.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            logger.info("Message received:" + msg.toString());

            if (msg instanceof MsgAddUserAnswer) {
                MsgAddUserAnswer ans = (MsgAddUserAnswer) msg;
                frontendService.handleAddUserResponse(ans.getSessionId(), ans.getUserId(), ans.getName());
            } else if (msg instanceof MsgGetUserInfoAnswer) {
                MsgGetUserInfoAnswer ans = (MsgGetUserInfoAnswer) msg;
                frontendService.handleGetUserInfoResponse(ans.getSessionId(), ans.getUserId(), ans.getName());
            }

            count++;
        }

        client.close();
    }

}
