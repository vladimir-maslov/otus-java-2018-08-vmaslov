package ru.otus.l16.frontend.app;

import ru.otus.l16.messageserver.messageSystem.Addressee;

import javax.websocket.Session;

public interface FrontendService {
    void handleRequest(int sessionId, String message);

    void handleGetUserInfoRequest(int sessionId, long userId);

    void handleGetUserInfoResponse(int sessionId, long userId, String userName);

    void handleAddUserRequest(int sessionId, String userName, int age);

    void handleAddUserResponse(int sessionId, long userId, String userName);

    int addSession(Session session);

    void removeSession(int sessionId);
}