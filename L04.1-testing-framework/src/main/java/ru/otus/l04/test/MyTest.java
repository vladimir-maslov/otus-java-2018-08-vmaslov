package ru.otus.l04.test;

import ru.otus.l04.framework.annotations.After;
import ru.otus.l04.framework.annotations.Before;
import ru.otus.l04.framework.annotations.Test;

public class MyTest {

    @Before
    public void beforeTest(){
        System.out.println("Before the test method");
    }

    @Test
    public void test(){
        System.out.println("Test method");
    }

    @Test
    public void testDifferent(){
        System.out.println("Test another method");
    }

    @After
    public void afterTest(){
        System.out.println("After the test method");
    }

}
