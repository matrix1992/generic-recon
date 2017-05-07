package com.rohan.recon;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ReconcilerTest {

    @Test
    public void reconcileTest() throws ImproperKeyException, IllegalAccessException, InstantiationException, InvocationTargetException,
            NoSuchMethodException {
        Apple a1 = new Apple(5, "Red", BigDecimal.ONE, null);
        Apple a2 = new Apple(5, "Red", BigDecimal.ONE, Arrays.asList(new String[] { "Kashmiri" }));

        Car c1 = new Car(10, "Blue", BigDecimal.ONE, "Ferrari");
        Car c2 = new Car(5, "Red", BigDecimal.ONE, "Hundai");

        List<Apple> apples = Arrays.asList(new Apple[] { a1, a2 });
        List<Car> cars = Arrays.asList(new Car[] { c1, c2 });

        Reconciler<Apple, Car, String> appleCarReconciler = new Reconciler<>();

        List<Reconciler<Apple, Car, String>.RecUnit> recUnits = appleCarReconciler.reconcile(apples, cars, "color", (apple, car) -> {
            if (apple == null) {
                return "Apple Side Miss";
            } else if (car == null) {
                return "Car Side Miss";
            } else if (apple.getSize() == car.getSize()) {
                return "Match";
            } else {
                return "Break";
            }
        });
        recUnits.stream().forEach(System.out::println);
    }
}