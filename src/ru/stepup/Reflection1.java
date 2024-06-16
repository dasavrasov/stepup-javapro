package ru.stepup;
import ru.stepup.Test;
import ru.stepup.BeforeSuite;
import ru.stepup.AfterSuite;

public class Reflection1 {
    @Test(priority = 7)
    public void method1() {
        System.out.println("Run method1");
    }
    @Test
    public void method2() {
        System.out.println("Run method2");
    }
    @Test(priority = 3)
    public void method3() {
        System.out.println("Run method3");
    }

    @BeforeSuite
    public static void static1() {
        System.out.println("Run static1");
    }
    @AfterSuite
    public static void static2() {
        System.out.println("Run static2");
    }
}
