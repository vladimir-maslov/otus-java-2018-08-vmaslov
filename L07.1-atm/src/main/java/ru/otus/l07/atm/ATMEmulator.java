package ru.otus.l07.atm;

import java.util.Arrays;
import java.util.Map;

public class ATMEmulator implements ATM {

    private long limit = 0;
    long balance = 0;

    private Storage storage;

    public ATMEmulator(ATMBanknotesStrategy strategy) {
        storage = Storage.createStorage(strategy);
    }

    @Override
    public void put(Banknote banknote, int amount) throws ATMException {
        storage.put(banknote, amount);
    }

    @Override
    public Map<Banknote, Integer> withdraw(int amount) throws ATMException {
        if (amount > getCashLimit()) {
            throw new ATMException("Requested amount (" + amount + ") is more than the cash limit");
        }
        Map<Banknote, Integer> banknotes = storage.get(amount);
        return banknotes;
    }

    @Override
    public int getCashLimit() {
        return storage.getTotalBalance();
    }

}
