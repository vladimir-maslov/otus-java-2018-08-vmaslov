package ru.otus.l16.messageserver.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.l16.messageserver.messageSystem.Address;
import ru.otus.l16.messageserver.messageSystem.Message;

public class MsgPing extends Message {
    @JsonProperty("time")
    private final long time;
    @JsonProperty("message")
    private final String message;

    @JsonCreator
    public MsgPing(@JsonProperty("from") Address from,
                   @JsonProperty("to") Address to,
                   @JsonProperty("message") String message) {
        super(from, to);
        time = System.currentTimeMillis();
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PingMsg{" + "time=" + time + '}';
    }
}
