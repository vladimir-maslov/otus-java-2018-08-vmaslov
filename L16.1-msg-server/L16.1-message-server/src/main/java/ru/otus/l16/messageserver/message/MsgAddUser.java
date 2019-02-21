package ru.otus.l16.messageserver.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.l16.messageserver.messageSystem.Message;
import ru.otus.l16.messageserver.messageSystem.Address;

public class MsgAddUser extends Message {
    @JsonProperty("sessionId")
    private final int sessionId;
    @JsonProperty("name")
    private final String userName;
    @JsonProperty("age")
    private final int age;

    @JsonCreator
    public MsgAddUser(
            @JsonProperty("from") Address from,
            @JsonProperty("to") Address to,
            @JsonProperty("sessionId") int sessionId,
            @JsonProperty("name") String userName,
            @JsonProperty("age") int age) {
        super(from, to);
        this.userName = userName;
        this.age = age;
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getName() {
        return userName;
    }

    public int getAge() {
        return age;
    }
}

