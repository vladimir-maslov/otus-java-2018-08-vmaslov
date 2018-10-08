package ru.otus.l04.framework;

import ru.otus.l04.framework.annotations.After;
import ru.otus.l04.framework.annotations.Before;
import ru.otus.l04.framework.annotations.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

public class TestRunner {

    public TestRunner() {
    }

    public static void runTests(String className) throws ClassNotFoundException {
        Class<?> testClass = Class.forName(className);
        try {
            Method[] methods = testClass.getDeclaredMethods();

            ArrayList<Method> beforeMethods = new ArrayList<>();
            ArrayList<Method> testMethods = new ArrayList<>();
            ArrayList<Method> afterMethods = new ArrayList<>();

            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Before) {
                        beforeMethods.add(method);
                    } else if (annotation instanceof Test) {
                        testMethods.add(method);
                    } else if (annotation instanceof After) {
                        afterMethods.add(method);
                    }
                }
            }

            if (!testMethods.isEmpty()) {
                System.out.println("Running tests for the class: " + className);
            } else {
                System.out.println("No tests found in the class: " + className);
                return;
            }

            int successTests = 0;

            for (Method testMethod : testMethods) {
                try {
                    Object testObject = testClass.getDeclaredConstructor().newInstance();
                    successTests++;

                    for (Method method : beforeMethods) {
                        method.invoke(testObject);
                    }

                    testMethod.invoke(testObject);

                    for (Method method : afterMethods) {
                        method.invoke(testObject);
                    }
                } catch (Exception e) {
                    System.out.println("Test failed: " + e.getCause().getMessage());
                    successTests--;
                }
            }

            System.out.println("Tests passed: " + successTests);

            if (successTests == testMethods.size()){
                System.out.println("[Success]");
            } else {
                System.out.println("[Failure]");
            }

        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }

    }

    public static void runTestsPackage(String packageName) throws ClassNotFoundException {
        Class[] classes = getAllClasses(packageName);
        for (Class cls : classes) {
            runTests(cls.getName());
        }
    }

    private static Class[] getAllClasses(String packageName)  throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class> classes = new ArrayList<>();

        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);
        File directory = new File(resource.getFile());

        classes = findClassesInDir(directory, packageName);

        return classes.toArray(new Class[classes.size()]);
    }

    private static ArrayList<Class> findClassesInDir(File directory, String packageName) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();

        for (File f : files) {
            if (f.isDirectory()) {
                classes.addAll(findClassesInDir(f, packageName + "." + f.getName()));
            } else if (f.getName().endsWith(".class")) {
                Class<?> cls = Class.forName(packageName + "." + f.getName().substring(0, f.getName().lastIndexOf(".")));
                classes.add(cls);
            }
        }
        return classes;
    }

}
