package ru.otus.l16.messageserver.app;

public abstract class Msg {
    public static final String CLASS_NAME_VARIABLE = "className";

    private final String className;

    protected Msg(Class<?> klass) {
        this.className = klass.getName();
    }
}
