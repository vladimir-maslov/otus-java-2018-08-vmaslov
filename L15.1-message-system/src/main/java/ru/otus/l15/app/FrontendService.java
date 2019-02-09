package ru.otus.l15.app;

import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.messageSystem.Addressee;

import javax.websocket.Session;

/**
 * Created by tully.
 */
public interface FrontendService extends Addressee {
    void handleRequest(int sessionId, String message);

    void handleGetUserInfoRequest(int sessionId, long userId);
    void handleGetUserInfoResponse(int sessionId, UserDataSet user);

    void handleAddUserRequest(int sessionId, String userName, int age);
    void handleAddUserResponse(int sessionId, UserDataSet user);

    int addSession(Session session);
    void removeSession(int sessionId);
}
