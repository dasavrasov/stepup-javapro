package ru.stepup;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
        Map<Method, Object[]> csvTests = new HashMap<>();

        int testCount = 0;
        csvTests.clear();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) throw new RuntimeException("Методов помеченных @BeforeSuite больше одного!");
                beforeSuite = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) throw new RuntimeException("\"Методов помеченных  @AfterSuite  больше одного!");
                afterSuite = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                tests[testCount++] = method; // Запоминаем методы с аннотацией @Test
            } else if (method.isAnnotationPresent(CsvSource.class)) {
                CsvSource csvSource = method.getAnnotation(CsvSource.class);
                String value = csvSource.value();
                String[] values = value.split(",");
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (parameterTypes[i] == int.class) {
                        parameters[i] = Integer.parseInt(values[i]);
                    } else if (parameterTypes[i] == boolean.class) {
                        parameters[i] = Boolean.parseBoolean(values[i]);
                    } else if (parameterTypes[i] == String.class) {
                        parameters[i] = values[i];
                    }
                }
                csvTests.put(method,parameters); // Запоминаем методы с аннотацией @CsvSource
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
        //запускаем csv tests
        for (Map.Entry<Method, Object[]> entry : csvTests.entrySet()) {
            try {
                entry.getKey().invoke(instance, entry.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {
        runTests(Reflection1.class);
    }
}
