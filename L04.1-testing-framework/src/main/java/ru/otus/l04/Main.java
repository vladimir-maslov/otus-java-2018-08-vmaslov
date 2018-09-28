package ru.otus.l04;

import ru.otus.l04.framework.TestRunner;

public class Main {

    public static void main(String[] args) {
        testClass("ru.otus.l04.test.MyTest");
        testPackage("ru.otus.l04.test");
    }

    public static void testClass(String className){
        System.out.println("[Testing class: " + className + "]");
        try {
            TestRunner.runTests(className);
        } catch (ClassNotFoundException e){
            System.out.println("Error: Class not found");
            e.printStackTrace();
        }
    }

    public static void testPackage(String packageName){
        System.out.println("[Testing package: " + packageName + "]");
        try {
            TestRunner.runTestsPackage(packageName);
        } catch (Exception e){
            System.out.println("Error: Class not found");
            e.printStackTrace();
        }
    }
}
