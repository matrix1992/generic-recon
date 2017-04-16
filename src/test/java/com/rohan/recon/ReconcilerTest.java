package com.rohan.recon;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.rohan.recon.ImproperKeyException;
import com.rohan.recon.Reconciler;

public class ReconcilerTest {

    @Test
    public void reconcileTest() throws ImproperKeyException {
        Apple a1 = new Apple(5, "Red", BigDecimal.ONE, null);
        Apple a2 = new Apple(5, "Red", BigDecimal.ONE, Arrays.asList(new String[] { "Kashmiri" }));

        Car c1 = new Car(10, "Red", BigDecimal.ONE, "Ferrari");
        Car c2 = new Car(5, "Red", BigDecimal.ONE, "Hundai");

        List<Apple> apples = Arrays.asList(new Apple[] { a1, a2 });
        List<Car> cars = Arrays.asList(new Car[] { c1, c2 });

        Reconciler<Apple, Car, String> appleCarReconciler = new Reconciler<>();

        List<Reconciler<Apple, Car, String>.RecUnit> recUnits = appleCarReconciler.reconcile(apples, cars, "color", (apple, car) -> "Match");
        recUnits.stream().forEach(System.out::println);
    }
}

class Apple {
    int size;
    String color;
    BigDecimal weight;
    List<String> varieties;

    public Apple(int size, String color, BigDecimal weight, List<String> varieties) {
        this.size = size;
        this.color = color;
        this.weight = weight;
        this.varieties = varieties;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Apple [size=" + size + ", color=" + color + ", weight=" + weight + ", varieties=" + varieties + "]";
    }

    public List<String> getVarieties() {
        return varieties;
    }

    public void setVarieties(List<String> varieties) {
        this.varieties = varieties;
    }
}

class Car {
    int size;
    String color;
    BigDecimal weight;
    String brand;

    @Override
    public String toString() {
        return "Car [size=" + size + ", color=" + color + ", weight=" + weight + ", brand=" + brand + "]";
    }

    public Car(int size, String color, BigDecimal weight, String brand) {
        this.size = size;
        this.color = color;
        this.weight = weight;
        this.brand = brand;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}