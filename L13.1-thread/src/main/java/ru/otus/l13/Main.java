package ru.otus.l13;

import ru.otus.l13.helpers.ArrayHelper;

public class Main {

    private static final int MAX_THREADS = 4;
    private static int[] numbers;

    public static void main(String[] args) {
        numbers = ArrayHelper.createRandomArray(32000, 10000);
        MergeSorter.parallelMergeSort(numbers, MAX_THREADS);
    }
}
