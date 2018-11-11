package ru.otus.l08.atm;

import ru.otus.l08.atm.events.EventInterface;

public interface Observer {
    void update(EventInterface event);
}
