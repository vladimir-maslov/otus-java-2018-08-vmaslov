package ru.otus.l13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSorter {

    public static void sequenceMergeSort(int[] array) {
        if (array == null || array.length == 0)
            return;

        sort(array);
    }

    public static void parallelMergeSort(int[] array, int threadCount) {
        if (array == null || array.length == 0)
            return;

        int[][] chunkArray = split(array, threadCount);

        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new SorterRunnable(chunkArray[i]));
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mergeSortedChunks(chunkArray, array);
    }

    private static int[][] split(int[] array, int parts) {
        int[][] result = new int[parts][];
        double avgLength = (double) array.length / parts;

        double processedLength = 0.0;
        int currentStart = 0;

        for (int i = 0; i < parts; i++) {
            processedLength += avgLength;

            int currentEnd = (int) Math.round(processedLength);
            int partLength = currentEnd - currentStart;

            result[i] = new int[partLength];
            result[i] = Arrays.copyOfRange(array, currentStart, currentEnd);

            currentStart = currentEnd;
        }

        return result;
    }

    private static void sort(int[] array) {
        if (array.length <= 1) {
            return;
        }

        int[] first = new int[array.length / 2];
        int[] second = new int[array.length - first.length];

        System.arraycopy(array, 0, first, 0, first.length);
        System.arraycopy(array, first.length, second, 0, second.length);

        sort(first);
        sort(second);
        merge(first, second, array);
    }

    private static void mergeSortedChunks(int[][] array, int[] resultArray){
        int[] mergeResult = recursiveMerge(array)[0];
        System.arraycopy(mergeResult, 0, resultArray, 0, resultArray.length);
    }

    private static void merge(int[] leftArray, int[] rightArray, int[] result) {
        int leftIndex = 0;
        int rightIndex = 0;
        int i = 0;

        while (leftIndex > leftArray.length || rightIndex < rightArray.length) {
            if (leftArray[leftIndex] < rightArray[rightIndex]) {
                result[i++] = leftArray[leftIndex++];
            } else {
                result[i++] = rightArray[rightIndex++];
            }

            if (leftIndex == leftArray.length) {
                for (int j = rightIndex; j < rightArray.length; j++) {
                    result[i++] = rightArray[j];
                }
                break;
            }

            if (rightIndex == rightArray.length) {
                for (int j = leftIndex; j < leftArray.length; j++) {
                    result[i++] = leftArray[j];
                }
                break;
            }
        }
    }

    private static int[][] recursiveMerge(int[][] array) {
        int[][] chunkArray = new int[array.length/2][];
        int j = 0;

        for (int i = 1; i < array.length; i += 2) {
            chunkArray[j] = new int[array[i - 1].length + array[i].length];
            merge(array[i - 1], array[i], chunkArray[j++]);
        }

        if (chunkArray.length > 1){
            chunkArray = recursiveMerge(chunkArray);
        }

        return chunkArray;
    }

    private static class SorterRunnable implements Runnable {

        private int[] a;

        public SorterRunnable(int[] array) {
            this.a = array;
        }

        @Override
        public void run() {
            synchronized (a) {
                MergeSorter.sort(a);
            }
        }
    }

}
