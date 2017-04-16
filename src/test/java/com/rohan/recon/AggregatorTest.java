package com.rohan.recon;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.rohan.recon.Aggregator;
import com.rohan.recon.ImproperKeyException;

public class AggregatorTest {

    @Test
    public void aggregateToListTest() throws ImproperKeyException {
        Aggregator<Record> record1Aggregator = new Aggregator<>();
        Record r1 = new Record(10.5, 5, new BigDecimal(10), "PQR", null);
        Record r2 = new Record(10.5, 50, new BigDecimal(20), "PQR", Arrays.asList(new Integer[] { 1, 2 }));
        Record r3 = new Record(10.5, 50, new BigDecimal(20), "ABC", Arrays.asList(new Integer[] { 1, 2 }));
        Record r4 = new Record(10.5, 100, new BigDecimal(40), "ABC", Arrays.asList(new Integer[] { 1, 2, 5 }));
        List<Record> records = Arrays.asList(new Record[] { r1, r2, r3, r4 });

        Collection<Record> aggregatedRecords = record1Aggregator.aggregateToList(records, "a,d");
        aggregatedRecords.stream().forEach(System.out::println);
    }

    @Test
    public void aggregateToMapTest() throws ImproperKeyException {
        Aggregator<Record> record1Aggregator = new Aggregator<>();
        Record r1 = new Record(10.5, 5, new BigDecimal(10), "PQR", null);
        Record r2 = new Record(10.5, 50, new BigDecimal(20), "PQR", Arrays.asList(new Integer[] { 1, 2 }));
        Record r3 = new Record(10.5, 50, new BigDecimal(20), "ABC", Arrays.asList(new Integer[] { 1, 2 }));
        Record r4 = new Record(10.5, 100, new BigDecimal(40), "ABC", Arrays.asList(new Integer[] { 1, 2, 5 }));
        List<Record> records = Arrays.asList(new Record[] { r1, r2, r3, r4 });

        Map<List<Object>, Record> aggregatedRecords = record1Aggregator.aggregateToMap(records, "a,d");
        aggregatedRecords.entrySet().stream().forEach(System.out::println);
    }

}

/*
 * Test class for aggregation. Note: We cannot use an inner class as it will
 * have an implicit reference to its parent class
 */
class Record {
    double a;
    long b;
    BigDecimal c;
    String d;
    List<Integer> l;

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