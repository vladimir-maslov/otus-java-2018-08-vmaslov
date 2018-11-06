package ru.otus.l08;

import ru.otus.l08.atm.*;
import ru.otus.l08.department.ATMDepartment;
import ru.otus.l08.department.Event;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        ATMObserver atm = new ATMEmulator(new MinimumBanknotes());
        try {
            atm.put(Banknote.HUNDRED, 5);
            atm.put(Banknote.FIVE_HUNDRED, 2);
            atm.put(Banknote.ONE_THOUSAND, 5);
            atm.put(Banknote.TWO_HUNDRED, 2);

            atm.save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ATMObserver atm1 = new ATMEmulator(new MinimumBanknotes());
        try {
            atm1.put(Banknote.HUNDRED, 1);
            atm1.put(Banknote.FIVE_HUNDRED, 1);
            atm1.put(Banknote.ONE_THOUSAND, 2);
            atm1.put(Banknote.TWO_HUNDRED, 3);

            atm1.save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ATMDepartment atmDep = new ATMDepartment();
        atmDep.add(atm);
        atmDep.add(atm1);

        System.out.println("Start.");
        printCashLimit(atmDep.get(0).getCashLimit());
        printCashLimit(atmDep.get(1).getCashLimit());
        printTotalCashLimit(atmDep.getTotalCashLimit());

        try {
            printBanknotes(atmDep.get(0).withdraw(300));
            printBanknotes(atmDep.get(0).withdraw(5100));

            printBanknotes(atmDep.get(1).withdraw(1700));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        printCashLimit(atmDep.get(0).getCashLimit());
        printCashLimit(atmDep.get(1).getCashLimit());
        printTotalCashLimit(atmDep.getTotalCashLimit());

        System.out.println("Restore ATMs.");
        atmDep.notifyObservers(new Event());

        printCashLimit(atmDep.get(0).getCashLimit());
        printCashLimit(atmDep.get(1).getCashLimit());
        printTotalCashLimit(atmDep.getTotalCashLimit());

    }

    public static void printBanknotes(Map<Banknote, Integer> banknotes) {
        System.out.println("Banknotes withdrawn: ");
        banknotes.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        System.out.println();
    }

    public static void printCashLimit(int cashLimit) {
        System.out.println("Cash limit: " + cashLimit);
    }

    public static void printTotalCashLimit(long cashLimit) {
        System.out.println("Total cash limit: " + cashLimit);
        System.out.println();
    }
}
