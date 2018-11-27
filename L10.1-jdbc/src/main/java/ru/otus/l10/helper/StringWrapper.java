package ru.otus.l10.helper;

public enum StringWrapper {
    QUOTES {
        @Override
        public String wrap(String string) {
            return "\"" + string + "\"";
        }
    };

    public abstract String wrap(String string);
}
