package ru.otus.l07.atm;

import java.util.*;

public class Storage {
    private Map<Banknote, Cell> cellsStorage = new TreeMap<>(new Comparator<Banknote>() {
        @Override
        public int compare(Banknote b1, Banknote b2) {
            return Integer.compare(b2.getDenomination(), b1.getDenomination());
        }
    });
    private ATMBanknotesStrategy banknotesStrategy;

    public Storage() {
    }

    public static Storage createStorage(ATMBanknotesStrategy strategy) {
        Storage s = new Storage();
        s.setBanknotesStrategy(strategy);
        return s;
    }

    public void put(Banknote banknote, int amount) throws ATMException {
        Cell cell = cellsStorage.get(banknote);
        if (cell == null) {
            cellsStorage.put(banknote, new Cell(banknote, amount));
        } else {
            cell.put(banknote, amount);
        }
    }

    public Map<Banknote, Integer> get(int amount) throws ATMException {
        Map<Banknote, Integer> availableBanknotes = getBanknotesAmount();
        Map<Banknote, Integer> minimumBanknotes = banknotesStrategy.calculate(amount, availableBanknotes);

        for (Banknote note : minimumBanknotes.keySet()) {
            cellsStorage.get(note).get(note, minimumBanknotes.get(note));
        }
        return minimumBanknotes;
    }

    public void setBanknotesStrategy(ATMBanknotesStrategy strategy){
        this.banknotesStrategy = strategy;
    }

    public Map<Banknote, Integer> getBanknotesAmount() {
        Map<Banknote, Integer> banknotes = new TreeMap<>(Collections.reverseOrder());
        cellsStorage.forEach((banknote, cell) ->
                banknotes.put(banknote, cell.getCount()));
        return banknotes;
    }

    public int getTotalBalance() {
        int balance = cellsStorage.entrySet().stream()
                .map((banknoteCellEntry) ->
                        (banknoteCellEntry.getKey().getDenomination() * banknoteCellEntry.getValue().getCount()))
                .mapToInt(Integer::intValue)
                .sum();
        return balance;
    }

}
