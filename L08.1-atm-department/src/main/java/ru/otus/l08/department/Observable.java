package ru.otus.l08.department;

import ru.otus.l08.atm.Observer;
import ru.otus.l08.atm.events.EventInterface;

public interface Observable {

    void registerObserver(Observer observer);

    void unregisterObserver(Observer observer);

    void notifyObservers(EventInterface event);

}
