package ru.otus.l07.atm;

import java.util.Map;

public interface ATMBanknotesStrategy {
    Map<Banknote, Integer> calculate(int amount, Map<Banknote, Integer> sourceBanknotes)
            throws ATMException;
}
