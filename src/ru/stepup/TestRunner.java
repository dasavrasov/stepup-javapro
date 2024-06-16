package ru.stepup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class TestRunner {
    public static void runTests(Class c){
        Class<?> clazz = null;
        try {
            clazz = Class.forName(c.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object instance;
        try {
            instance = c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Method[] methods = clazz.getDeclaredMethods();

        Method beforeSuite = null;
        Method afterSuite = null;
        Method[] tests = new Method[methods.length];

        int testCount = 0;
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) throw new RuntimeException("Методов помеченных @BeforeSuite больше одного!");
                beforeSuite = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) throw new RuntimeException("\"Методов помеченных  @AfterSuite  больше одного!");
                afterSuite = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                tests[testCount++] = method; // Запоминаем методы с аннотацией @Test
            }
        }

        //запускаем метод помеченный @BeforeSuite
        if (beforeSuite != null) {
            try {
                beforeSuite.invoke(instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //запускаем методы помеченные @Test в порядке приоритета
        Method[] actualTests = new Method[testCount];
        System.arraycopy(tests, 0, actualTests, 0, testCount);
        Arrays.sort(actualTests, Comparator.comparingInt(m -> m.getAnnotation(Test.class).priority()));

        for (Method test : actualTests) {
            try {
                test.invoke(instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (afterSuite != null) {
            try {
                afterSuite.invoke(instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {
        runTests(Reflection1.class);
    }
}
