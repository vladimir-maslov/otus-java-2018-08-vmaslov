package ru.otus.l13.helpers;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayHelper {

    public static int[] createRandomArray(int size, int bound) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = ThreadLocalRandom.current().nextInt(bound);
        }
        return array;
    }

    public static void print(int[] array) {
        System.out.println(Arrays.toString(array));
    }
}

