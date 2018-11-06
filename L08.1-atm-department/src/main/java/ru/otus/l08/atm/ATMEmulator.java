package ru.otus.l08.atm;

import ru.otus.l08.department.Event;

import java.util.Map;

public class ATMEmulator implements ATMObserver {

    private long limit = 0;
    long balance = 0;

    private Storage storage;
    private ATMMemento memento;

    public ATMEmulator(ATMBanknotesStrategy strategy) {
        storage = Storage.createStorage(strategy);
        this.save();
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

    @Override
    public void save() {
        memento = storage.save();
    }

    @Override
    public void reset() {
        storage.reset(memento);
    }

    @Override
    public void update(Event event) {
        reset();
    }
}
