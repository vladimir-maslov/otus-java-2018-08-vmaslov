package ru.otus.l02;

public class Main {
    public static void main(String[] args) {
        MemoryMeasurer measurer = new MemoryMeasurer();
        MeasureResult result = measurer.getMeasures();
        result.printResult();
    }
}
