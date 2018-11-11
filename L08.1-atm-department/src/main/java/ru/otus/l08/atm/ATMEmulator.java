package ru.otus.l08.atm;

import ru.otus.l08.atm.events.EventInterface;

import java.util.Map;

public class ATMEmulator implements ATMObserver {

    private Storage storage;
    private ATMMemento memento;

    public ATMEmulator(ATMBanknotesStrategy strategy) {
        storage = Storage.createStorage(strategy);
        this.save();
    }

    public ATMEmulator(ATMEmulator atmEmulator){
        this.storage = new Storage(atmEmulator.storage);
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
        memento = new ATMMemento(this);
    }

    @Override
    public void reset() {
        ATMEmulator atmBackup = memento.getSavedState();
        this.storage = atmBackup.getStorage();
    }

    private Storage getStorage(){
        return new Storage(this.storage);
    }

    @Override
    public void update(EventInterface event) {
        event.doCommand(this);
    }
}
