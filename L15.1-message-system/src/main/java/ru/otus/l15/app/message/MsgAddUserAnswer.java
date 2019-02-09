package ru.otus.l15.app.message;

import ru.otus.l15.app.FrontendService;
import ru.otus.l15.app.MsgToFrontend;
import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.messageSystem.Address;

public class MsgAddUserAnswer extends MsgToFrontend {
    private final UserDataSet user;
    private final int sessionId;

    public MsgAddUserAnswer(Address from, Address to, int sessionId, UserDataSet user) {
        super(from, to);
        this.user = user;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.handleAddUserResponse(sessionId, user);
    }
}
