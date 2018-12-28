package ru.otus.l13;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import ru.otus.l13.helpers.ArrayHelper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MergeSorterTest {

    @Test
    public void sortTest(){
        int[] numbers = ArrayHelper.createRandomArray(12, 1000);
        int[] numbersCopy = numbers.clone();

        System.out.println("Before sort");
        ArrayHelper.print(numbers);

        MergeSorter.sequenceMergeSort(numbers);
        MergeSorter.parallelMergeSort(numbersCopy, 4);

        System.out.println("After sort");
        ArrayHelper.print(numbers);
        ArrayHelper.print(numbersCopy);

        assertArrayEquals(numbers, numbersCopy);
    }

    @Test
    public void stdArrayTest(){
        int[] numbers = ArrayHelper.createRandomArray(80000, 10000);
        int[] numbersCopy = numbers.clone();

        MergeSorter.parallelMergeSort(numbers, 4);
        Arrays.sort(numbersCopy);

        assertArrayEquals(numbers, numbersCopy);
    }

    @Test
    public void sortArraySpeedTest(){
        int[] numbers = ArrayHelper.createRandomArray(320000, 10000);
        int[] numbersCopy = numbers.clone();

        long startTime = System.nanoTime();
        MergeSorter.sequenceMergeSort(numbers);
        long stopTime = System.nanoTime();

        long startTime1 = System.nanoTime();
        MergeSorter.parallelMergeSort(numbersCopy, 4);
        long stopTime1 = System.nanoTime();

        long sequenceTime = (stopTime - startTime);
        long parallelTime = (stopTime1 - startTime1);

        System.out.println("Sequence speed: " + sequenceTime + " ns");
        System.out.println("Parallel speed: " + parallelTime + " ns");

        assertTrue(sequenceTime > parallelTime);
    }
}
