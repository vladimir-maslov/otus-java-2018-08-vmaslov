package ru.otus.l15.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import ru.otus.l15.app.message.MsgAddUser;
import ru.otus.l15.app.message.MsgGetUserInfo;
import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Message;
import ru.otus.l15.messageSystem.MessageSystem;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Service
public class FrontendServiceImpl implements FrontendService {
    private final Address address;
    private final MessageSystemContext context;

    private static final String METHOD_GET_USER_BY_ID_NAME = "getUserById";
    private static final String METHOD_ADD_USER_NAME = "addUser";

    private static final String ID_PARAMETER_NAME = "userId";
    private static final String ID_PARAMETER_RESPONSE_NAME = "id";
    private static final String NAME_PARAMETER_NAME = "name";
    private static final String AGE_PARAMETER_NAME = "age";
    private static final String METHOD_PARAMETER_NAME = "method";
    private static final String ERROR_PARAMETER_NAME = "error";

    private static final String USER_NOT_FOUND = "User not found";

    private static Logger log = Logger.getLogger("Frontend");

    private final Map<Integer, Session> sessions = new HashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    public FrontendServiceImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
        context.setFrontAddress(this.address);
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void handleRequest(int sessionId, String message){
        JsonObject jObj = new Gson().fromJson(message, JsonObject.class);
        String method = jObj.get(METHOD_PARAMETER_NAME).getAsString();

        switch (method){
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
        Message message = new MsgGetUserInfo(getAddress(), context.getDbAddress(), sessionId, userId);
        context.getMessageSystem().sendMessage(message);
    }

    public void handleAddUserRequest(int sessionId, String userName, int age) {
        UserDataSet user = new UserDataSet(userName, age);
        Message message = new MsgAddUser(getAddress(), context.getDbAddress(), sessionId, user);
        context.getMessageSystem().sendMessage(message);
    }

    public void handleGetUserInfoResponse(int sessionId, UserDataSet user) {
        Map<String, String> result = new HashMap<>();

        try {
            result.put(ID_PARAMETER_RESPONSE_NAME, Long.toString(user.getId()));
            result.put(NAME_PARAMETER_NAME, user.getName());
        } catch (Throwable e){
            result.put(ERROR_PARAMETER_NAME, USER_NOT_FOUND);
        }

        String json = new GsonBuilder().create().toJson(result);
        sendMessage(sessionId, json);
    }

    public void handleAddUserResponse(int sessionId, UserDataSet user) {
        Map<String, String> result = new HashMap<>();

        result.put(ID_PARAMETER_RESPONSE_NAME, Long.toString(user.getId()));
        result.put(NAME_PARAMETER_NAME, user.getName());
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

    public void sendMessage(int sessionId, String message){
        try {
            sessions.get(sessionId).getBasicRemote().sendText(message);
        } catch (Exception e){
            log.info(message);
        }
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}
