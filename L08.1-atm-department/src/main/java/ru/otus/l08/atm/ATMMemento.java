package ru.otus.l08.atm;

public class ATMMemento {
    private ATMEmulator state;

    public ATMMemento(ATMEmulator state){
        this.state = new ATMEmulator(state);
    }

    public ATMEmulator getSavedState() {
        return new ATMEmulator(this.state);
    }
}
