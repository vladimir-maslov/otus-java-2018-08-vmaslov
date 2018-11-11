package ru.otus.l08.atm.events;

import ru.otus.l08.atm.ATMEmulator;

public interface EventInterface {
    void doCommand(ATMEmulator atm);
}
