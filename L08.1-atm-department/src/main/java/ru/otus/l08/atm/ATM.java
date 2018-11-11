package ru.otus.l08.atm;

import java.util.Map;

/**
 * ATM
 */
public interface ATM {

    /**
     * Load cash into the ATM
     *
     * @param banknote Banknote
     * @param amount   Amount of banknotes
     * @throws ATMException When the capacity of the cell with banknotes is full
     */
    void put(Banknote banknote, int amount) throws ATMException;

    /**
     * Withdraw cash from the ATM
     *
     * @return Map<Banknote               ,                               Long> Cash money
     * @throws ATMException When there are not enough cash
     */
    Map<Banknote, Integer> withdraw(int amount) throws ATMException;

    /**
     * Get the amount of cash left in the ATM
     *
     * @return Amount of cash
     */
    int getCashLimit();

    /**
     * Save the state of the ATM
     */
    void save();

    /**
     * Reset the current state of the ATM to the saved one
     */
    void reset();

}
