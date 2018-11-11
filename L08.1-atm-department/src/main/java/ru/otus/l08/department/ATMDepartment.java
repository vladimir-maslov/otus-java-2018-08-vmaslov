package ru.otus.l08.department;

import ru.otus.l08.atm.ATM;
import ru.otus.l08.atm.Observer;
import ru.otus.l08.atm.ATMObserver;
import ru.otus.l08.atm.events.EventInterface;
import ru.otus.l08.atm.events.ResetEvent;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartment implements Department, Observable {
    private List<ATM> atms = new ArrayList<>();
    private List<Observer> atmObservers = new ArrayList<>();

    public ATMDepartment() {
    }

    public void add(ATMObserver atm) {
        atms.add(atm);
        registerObserver(atm);
    }

    public ATM get(int index) {
        return atms.get(index);
    }

    @Override
    public long getTotalCashLimit() {
        if (atms.isEmpty())
            return 0;

        long sum = atms.stream()
                .mapToLong((atm -> atm.getCashLimit())).sum();
        return sum;
    }

    @Override
    public void reset(){
        notifyObservers(new ResetEvent());
    }

    @Override
    public void registerObserver(Observer observer) {
        atmObservers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        atmObservers.remove(observer);
    }

    @Override
    public void notifyObservers(EventInterface event) {
        for (Observer observer : atmObservers) {
            observer.update(event);
        }
    }
}
