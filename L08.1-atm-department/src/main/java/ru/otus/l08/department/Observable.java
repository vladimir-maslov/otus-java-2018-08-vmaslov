package ru.otus.l08.department;

import ru.otus.l08.atm.Observer;

public interface Observable {

    void registerObserver(Observer observer);

    void unregisterObserver(Observer observer);

    void notifyObservers(Event event);

}
