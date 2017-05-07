package com.rohan.recon;

import java.math.BigDecimal;
import java.util.List;

public class Record {
    double a;
    long b;
    BigDecimal c;
    String d;
    List<Integer> l;

    public Record() {
    }

    Record(double a, long b, BigDecimal c, String d, List<Integer> l) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.l = l;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }

    public BigDecimal getC() {
        return c;
    }

    public void setC(BigDecimal c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    @Override
    public String toString() {
        return "Record1 [a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", l=" + l + "]";
    }

    public void setD(String d) {
        this.d = d;
    }

    public List<Integer> getL() {
        return l;
    }

    public void setL(List<Integer> l) {
        this.l = l;
    }

}
