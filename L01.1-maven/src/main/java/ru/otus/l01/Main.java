package ru.otus.l01;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

    public static void main(String[] args) {
        int[] fibonacciNumbers = new int[]{1, 1, 2, 3, 5, 8, 13, 21, 34, 55};
        printJson(fibonacciNumbers);
    }

    private static void printJson(int[] arr) {
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(arr));
    }
}
