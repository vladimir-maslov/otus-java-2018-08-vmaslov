package ru.otus.l02;

public class Main {
    public static void main(String[] args) {
        MemoryMeasurer measurer = new MemoryMeasurer();
        measurer.getMeasures();
        measurer.printResult();
    }
}
