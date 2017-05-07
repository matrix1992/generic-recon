package com.rohan.recon;

import java.math.BigDecimal;
import java.util.List;

public class Apple {
    int size;
    String color;
    BigDecimal weight;
    List<String> varieties;

    public Apple() {
    }

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
