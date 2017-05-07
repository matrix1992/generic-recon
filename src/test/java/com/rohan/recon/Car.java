package com.rohan.recon;

import java.math.BigDecimal;

public class Car {
    int size;
    String color;
    BigDecimal weight;
    String brand;

    public Car() {
    }

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
