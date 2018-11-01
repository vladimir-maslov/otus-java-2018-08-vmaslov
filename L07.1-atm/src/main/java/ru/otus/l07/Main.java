package ru.otus.l07;

import ru.otus.l07.atm.ATM;
import ru.otus.l07.atm.ATMEmulator;
import ru.otus.l07.atm.Banknote;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATMEmulator();

        try {
            atm.put(Banknote.HUNDRED, 1);
            atm.put(Banknote.HUNDRED, 10);
            atm.put(Banknote.FIVE_HUNDRED, 1);
            atm.put(Banknote.ONE_THOUSAND, 2);
            atm.put(Banknote.TWO_HUNDRED, 3);
            atm.put(Banknote.FIVE_THOUSANDS, 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        printCashLimit(atm.getCashLimit());

        try {
            printBanknotes(atm.withdraw(300));
            printBanknotes(atm.withdraw(1100));
            printBanknotes(atm.withdraw(5700));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        printCashLimit(atm.getCashLimit());

        try {
            printBanknotes(atm.withdraw(250));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            printBanknotes(atm.withdraw(7000));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        System.out.println();
    }
}
