package ru.otus.l03;

import org.junit.jupiter.api.Test;

import java.text.Collator;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyArrayListTest {

    @Test
    public void createTest(){
        MyArrayList<Integer> list = new MyArrayList<Integer>();
        MyArrayList<Integer> listCapacity = new MyArrayList<Integer>(100);
        MyArrayList<Integer> listCollection = new MyArrayList<Integer>(Arrays.asList(10, 20, 30));

        assertEquals(list.size(), 0);
        assertEquals(listCapacity.size(), 0);
        assertEquals(listCollection.size(), 3);
    }

    @Test
    public void addAllTest(){
        MyArrayList<Integer> list = new MyArrayList<Integer>();
        Collections.addAll(list, 10, 20, 30);

        printList(list);
        assertEquals(list.size(), 3);
    }

    @Test
    public void copyTest(){
        MyArrayList<Integer> list = new MyArrayList<Integer>();
        list.addAll(Arrays.asList(100, 200, 300, 400, 500));
        MyArrayList<Integer> listTest = new MyArrayList<Integer>(list.size());
        listTest.addAll(Arrays.asList(10, 20, 30));
        Collections.copy(list, listTest);

        printList(list);
    }

    @Test
    public void sortTest(){
        MyArrayList<Integer> list = new MyArrayList<Integer>();

        list.add(1);
        list.addAll(Arrays.asList(1100, 220, 330, 100, 20, 30));
        list.addAll(Arrays.asList(100, 230, 310, 11, 4, 90, 10));

        Collections.sort(list);

        printList(list);
    }

    @Test
    public void sortTestStrings(){
        MyArrayList<String> listStr = new MyArrayList<String>();
        listStr.addAll(Arrays.asList("Lennon", "McCartney", "Harrison", "Starr"));
        Collections.sort(listStr, Collections.reverseOrder());

        printList(listStr);
    }

    private static void printList(List<?> list) {
        System.out.println(Arrays.toString(list.toArray()));
    }

}
