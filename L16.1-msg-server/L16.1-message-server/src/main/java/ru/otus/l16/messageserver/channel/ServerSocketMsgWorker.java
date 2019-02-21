package ru.otus.l16.messageserver.channel;

import java.io.IOException;
import java.net.Socket;

import com.fasterxml.jackson.databind.JsonNode;

import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.MessageJson;
import ru.otus.l16.messageserver.messageSystem.Message;

public class ServerSocketMsgWorker extends SocketMsgWorker {
    public ServerSocketMsgWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public ServerSocketMsgWorker(Socket socket) {
        super(socket);
    }

    @Override
    protected Message getMessageFromJson(String json) throws Exception {
        JsonNode jsonRoot = mapper.readTree(json);
        Address from = mapper.readValue(jsonRoot.get("from").toString(), Address.class);
        Address to = mapper.readValue(jsonRoot.get("to").toString(), Address.class);
        return new MessageJson(from, to, json);
    }

    @Override
    protected String writeMessageToJson(Message msg) throws Exception {
        MessageJson jsonMessage = (MessageJson) msg;
        return jsonMessage.getMessage();
    }
}