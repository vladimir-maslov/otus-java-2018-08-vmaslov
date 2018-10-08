package ru.otus.l04.test;

import ru.otus.l04.framework.annotations.After;
import ru.otus.l04.framework.annotations.Before;
import ru.otus.l04.framework.annotations.Test;

public class MyLittleTest {

    @Before
    public void beforeTest(){
        System.out.println("Before the little test method");
    }

    @Test
    public void test(){
        System.out.println("Little test method");
        throw new RuntimeException("My test exception");
    }

    @After
    public void afterTest(){
        System.out.println("After the little test method");
    }

}
