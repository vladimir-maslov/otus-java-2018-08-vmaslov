package ru.otus.l11.helper;

public enum StringWrapper {
    QUOTES {
        @Override
        public String wrap(String string) {
            return "\"" + string + "\"";
        }
    };

    public abstract String wrap(String string);
}
