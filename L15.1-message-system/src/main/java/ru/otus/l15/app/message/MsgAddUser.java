package ru.otus.l15.app.message;

import ru.otus.l15.app.MsgToDB;
import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.app.DBService;
import ru.otus.l15.messageSystem.Address;

public class MsgAddUser extends MsgToDB {
    private final UserDataSet user;
    private final int sessionId;

    public MsgAddUser(Address from, Address to, int sessionId, UserDataSet user) {
        super(from, to);
        this.user = user;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(DBService dbService) {
        dbService.save(user);
        dbService.getMS().sendMessage(new MsgAddUserAnswer(getTo(), getFrom(), sessionId, user));
    }
}

