package ru.otus.l15.app.message;

import ru.otus.l15.app.MsgToDB;
import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.app.DBService;
import ru.otus.l15.messageSystem.Address;


public class MsgGetUserInfo extends MsgToDB {
    private final long userId;
    private final int sessionId;

    public MsgGetUserInfo(Address from, Address to, int sessionId, long userId) {
        super(from, to);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(DBService dbService) {
        UserDataSet user = null;
        try {
            user = dbService.read(userId);
        } catch (Exception e){
            e.printStackTrace();
        }
        dbService.getMS().sendMessage(new MsgGetUserInfoAnswer(getTo(), getFrom(), sessionId, userId, user));
    }
}
