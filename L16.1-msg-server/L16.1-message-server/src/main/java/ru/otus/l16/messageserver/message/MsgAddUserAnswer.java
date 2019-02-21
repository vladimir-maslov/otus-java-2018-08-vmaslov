package ru.otus.l16.messageserver.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.Message;

public class MsgAddUserAnswer extends Message {
    @JsonProperty("sessionId")
    private final int sessionId;
    @JsonProperty("userId")
    private final long userId;
    @JsonProperty("name")
    private final String userName;

    @JsonCreator
    public MsgAddUserAnswer(
            @JsonProperty("from") Address from,
            @JsonProperty("to") Address to,
            @JsonProperty("sessionId") int sessionId,
            @JsonProperty("userId") long userId,
            @JsonProperty("name") String userName) {
        super(from, to);
        this.sessionId = sessionId;
        this.userName = userName;
        this.userId = userId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    /*
    @Override
    public void exec(FrontendService frontendService) {
        frontendService.handleAddUserResponse(sessionId, user);
    }
    */
}
