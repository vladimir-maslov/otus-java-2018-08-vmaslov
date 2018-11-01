package ru.otus.l07.atm;

import java.util.Map;
import java.util.TreeMap;

public class MinimumBanknotes implements ATMBanknotesStrategy {

    public MinimumBanknotes() {
    }

    @Override
    public Map<Banknote, Integer> calculate(int amount, Map<Banknote, Integer> sourceBanknotes)
            throws ATMException {
        Map<Banknote, Integer> resultBanknotes = new TreeMap<>();
        int neededAmount = amount;

        for (Banknote note : sourceBanknotes.keySet()) {
            int denomination = note.getDenomination();
            int count = sourceBanknotes.get(note);

            while ((neededAmount > 0) && (count > 0)
                    && (denomination <= neededAmount)) {
                resultBanknotes.merge(note, 1, Integer::sum);
                neededAmount -= denomination;
                count--;
            }
            if (neededAmount == 0) break;
        }

        if (neededAmount > 0) {
            throw new ATMException("Impossible to get the cash amount requested (" + amount + ")");
        }
        return resultBanknotes;
    }
}
