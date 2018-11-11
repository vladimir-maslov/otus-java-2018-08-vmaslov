package ru.otus.l08.atm.events;

import ru.otus.l08.atm.ATMEmulator;

public class ResetEvent implements EventInterface {
    public void doCommand(ATMEmulator atm) {
        atm.reset();
    }
}
