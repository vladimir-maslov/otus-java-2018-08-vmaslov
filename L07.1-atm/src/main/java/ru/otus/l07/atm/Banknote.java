package ru.otus.l07.atm;

public enum Banknote {
    HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    FIVE_THOUSANDS(5000);

    private final int denomination;

    Banknote(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }
}
