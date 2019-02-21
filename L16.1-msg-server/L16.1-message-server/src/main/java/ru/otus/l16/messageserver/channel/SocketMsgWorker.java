package ru.otus.l16.messageserver.channel;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.otus.l16.messageserver.app.MsgWorker;
import ru.otus.l16.messageserver.messageSystem.Message;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

public class SocketMsgWorker implements MsgWorker {
    private static final Logger logger = Logger.getLogger(SocketMsgWorker.class.getName());
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;

    protected static final ObjectMapper mapper = new ObjectMapper();

    public SocketMsgWorker(Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);

        logger.setLevel(Level.INFO);
        logger.info("SocketMsgWorker started.");
    }

    @Override
    public void send(Message msg) {
        output.add(msg);
    }

    @Override
    public Message poll() {
        return input.poll();
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    public void refund(Message msg) {
        input.add(msg);
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    @Blocks
    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {
            while (socket.isConnected()) {
                Message msg = output.take(); // blocks
                String json = writeMessageToJson(msg);// new Gson().toJson(msg);
                out.println(json);
                out.println(); // line with json + an empty line
                logger.info("SocketMsgWorker sendMessage: " + json);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Blocks
    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) { //blocks
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    String json = stringBuilder.toString();
                    Message msg = getMessageFromJson(json);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                    logger.info("SocketMsgWorker receiveMessage: " + json);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    protected Message getMessageFromJson(String json) throws Exception {
        return mapper.readValue(json, Message.class);
    }

    protected String writeMessageToJson(Message msg) throws Exception {
        return mapper.writeValueAsString(msg);
    }
}
