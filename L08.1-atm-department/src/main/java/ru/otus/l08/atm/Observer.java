package ru.otus.l08.atm;

import ru.otus.l08.department.Event;

public interface Observer {
    void update(Event event);
}
