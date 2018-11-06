package ru.otus.l08.atm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ATMMemento {

    private Map<Banknote, Cell> state = new TreeMap<>(new Comparator<Banknote>() {
        @Override
        public int compare(Banknote b1, Banknote b2) {
            return Integer.compare(b2.getDenomination(), b1.getDenomination());
        }
    });

    public ATMMemento(Map<Banknote, Cell> state) {
        for (Map.Entry<Banknote, Cell> entry : state.entrySet()) {
            this.state.put(entry.getKey(), new Cell(state.get(entry.getKey())));
        }
    }

    public Map<Banknote, Cell> getSavedState() {
        return state;
    }
}
