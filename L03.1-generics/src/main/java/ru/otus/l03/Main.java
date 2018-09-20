package ru.otus.l03;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        MyArrayList<String> list = new MyArrayList<String>();

        Collections.addAll(list, "ae", "cd", "bz", "ex", "dx");
        printList(list);

        MyArrayList<String> listTest = new MyArrayList<String>(list.size());

        Collections.addAll(listTest, "xs", "zo", "yb");
        printList(listTest);

        Collections.copy(list, listTest);
        printList(list);

        Collections.sort(list);
        printList(list);
    }

    private static void printList(List<?> list) {
        System.out.println(Arrays.toString(list.toArray()));
    }
}
