package ru.otus.l09;

public enum StringWrapper {
    QUOTES {
        @Override
        public String wrap(String string) {
            return "\"" + string + "\"";
        }
    },
    ROUND_BRACKETS {
        @Override
        public String wrap(String string) {
            return "(" + string + ")";
        }
    },
    SQUARE_BRACKETS {
        @Override
        public String wrap(String string) {
            return "[" + string + "]";
        }
    },
    CURLY_BRACKETS {
        @Override
        public String wrap(String string) {
            return "{" + string + "}";
        }
    };

    public abstract String wrap(String string);
}
