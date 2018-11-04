package ru.otus.l07.atm;

import java.util.Comparator;
import java.util.Map;

public class Cell {
    public static final int CELL_CAPACITY = 100;

    private final int capacity = CELL_CAPACITY;
    private final int denomination;
    private int count = 0;

    public Cell(Banknote banknote) {
        denomination = banknote.getDenomination();
    }

    public Cell(Banknote banknote, int amount) {
        denomination = banknote.getDenomination();
        count = amount;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getCount() {
        return count;
    }

    public void get(Banknote banknote, int amount) throws ATMException {
        if (banknote.getDenomination() != denomination) {
            throw new ATMException("Incompatible cell");
        }
        if (count < amount) {
            throw new ATMException("Not enough money in the cell");
        }
        count -= amount;
    }

    public void put(Banknote banknote, int amount) throws ATMException {
        if (banknote.getDenomination() != denomination) {
            throw new ATMException("Incompatible cell");
        }
        if (count + amount > capacity) {
            throw new ATMException("Cash amount is too big for the current cell capacity");
        }
        count += amount;
    }
}
