package ru.otus.l16.messageserver.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.Message;

public class MsgGetUserInfo extends Message {
    @JsonProperty("userId")
    private final long userId;
    @JsonProperty("sessionId")
    private final int sessionId;

    @JsonCreator
    public MsgGetUserInfo(
            @JsonProperty("from") Address from,
            @JsonProperty("to") Address to,
            @JsonProperty("sessionId") int sessionId,
            @JsonProperty("userId") long userId) {
        super(from, to);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public long getUserId() {
        return userId;
    }
}
