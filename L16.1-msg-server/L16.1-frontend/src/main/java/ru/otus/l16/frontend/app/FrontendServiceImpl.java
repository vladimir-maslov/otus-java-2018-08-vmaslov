package ru.otus.l16.frontend.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.l16.frontend.servlet.TemplateProcessor;
import ru.otus.l16.messageserver.message.MsgAddUser;
import ru.otus.l16.messageserver.message.MsgGetUserInfo;
import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.Addressee;
import ru.otus.l16.messageserver.messageSystem.Message;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;

@Service
public class FrontendServiceImpl implements FrontendService, Addressee {
    private final Address address;
    private final Address toAddress;

    private static final String METHOD_GET_USER_BY_ID_NAME = "getUserById";
    private static final String METHOD_ADD_USER_NAME = "addUser";

    private static final String ID_PARAMETER_NAME = "userId";
    private static final String ID_PARAMETER_RESPONSE_NAME = "id";
    private static final String NAME_PARAMETER_NAME = "name";
    private static final String AGE_PARAMETER_NAME = "age";
    private static final String METHOD_PARAMETER_NAME = "method";
    private static final String ERROR_PARAMETER_NAME = "error";

    private static final String USER_NOT_FOUND = "User not found";

    private static Logger logger = Logger.getLogger("Frontend");

    private final Map<Integer, Session> sessions = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final MessageServerClient messageServerClient;

    public FrontendServiceImpl(MessageServerClient messageServerClient, TemplateProcessor templateProcessor) throws IOException {
        this.address = new Address("Frontend");
        this.toAddress = new Address("DBService");

        this.messageServerClient = messageServerClient;
        this.messageServerClient.setFrontend(this);
        this.messageServerClient.start();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public Address getToAddress() {
        return toAddress;
    }

    public void handleRequest(int sessionId, String message) {
        messageServerClient.start();

        JsonObject jObj = new Gson().fromJson(message, JsonObject.class);
        String method = jObj.get(METHOD_PARAMETER_NAME).getAsString();

        switch (method) {
            case METHOD_GET_USER_BY_ID_NAME:
                long userId = jObj.get(ID_PARAMETER_NAME).getAsLong();
                handleGetUserInfoRequest(sessionId, userId);
                break;
            case METHOD_ADD_USER_NAME:
                String userName = jObj.get(NAME_PARAMETER_NAME).getAsString();
                int age = jObj.get(AGE_PARAMETER_NAME).getAsInt();
                handleAddUserRequest(sessionId, userName, age);
                break;
        }
    }

    public void handleGetUserInfoRequest(int sessionId, long userId) {
        Message message = new MsgGetUserInfo(getAddress(), getToAddress(), sessionId, userId);
        messageServerClient.send(message);
    }

    public void handleAddUserRequest(int sessionId, String userName, int age) {
        Message message = new MsgAddUser(getAddress(), getToAddress(), sessionId, userName, age);
        messageServerClient.send(message);
    }

    public void handleGetUserInfoResponse(int sessionId, long userId, String userName) {
        Map<String, String> result = new HashMap<>();

        try {
            result.put(ID_PARAMETER_RESPONSE_NAME, Long.toString(userId));
            result.put(NAME_PARAMETER_NAME, userName);
        } catch (Throwable e) {
            result.put(ERROR_PARAMETER_NAME, USER_NOT_FOUND);
        }

        String json = new GsonBuilder().create().toJson(result);
        sendMessage(sessionId, json);
    }

    public void handleAddUserResponse(int sessionId, long userId, String userName) {
        Map<String, String> result = new HashMap<>();

        result.put(ID_PARAMETER_RESPONSE_NAME, Long.toString(userId));
        result.put(NAME_PARAMETER_NAME, userName);
        String json = new GsonBuilder().create().toJson(result);

        sendMessage(sessionId, json);
    }

    @Override
    public int addSession(Session session) {
        int id = counter.incrementAndGet();
        sessions.put(id, session);
        return id;
    }

    @Override
    public void removeSession(int sessionId) {
        if (!sessions.containsKey(sessionId)) return;
        sessions.remove(sessionId);
    }

    public void sendMessage(int sessionId, String message) {
        try {
            sessions.get(sessionId).getBasicRemote().sendText(message);
        } catch (Exception e) {
            logger.info(message);
        }
    }
}
