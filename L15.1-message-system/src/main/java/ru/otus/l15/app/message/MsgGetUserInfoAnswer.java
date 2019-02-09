package ru.otus.l15.app.message;

import ru.otus.l15.app.FrontendService;
import ru.otus.l15.app.MsgToFrontend;
import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.messageSystem.Address;

public class MsgGetUserInfoAnswer extends MsgToFrontend {
    private final long userId;
    private final UserDataSet user;
    private final int sessionId;

    public MsgGetUserInfoAnswer(Address from, Address to, int sessionId, long userId, UserDataSet user) {
        super(from, to);
        this.userId = userId;
        this.user = user;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.handleGetUserInfoResponse(sessionId, user);
    }
}
