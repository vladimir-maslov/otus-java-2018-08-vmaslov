package ru.otus.l08.department;

/**
 * ATM department
 */
public interface Department {

    /**
     * Get total amount of cash left on all ATMs in the department
     *
     * @return Amount of cash
     */
    long getTotalCashLimit();

}