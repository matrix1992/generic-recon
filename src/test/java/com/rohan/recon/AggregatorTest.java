package com.rohan.recon;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public class AggregatorTest {

    @Test
    public void aggregateToListTest() throws ImproperKeyException, IllegalAccessException, InstantiationException, InvocationTargetException,
            NoSuchMethodException {
        Aggregator<Record> record1Aggregator = new Aggregator<>();
        Record r1 = new Record(10.5, 5, new BigDecimal(10), "PQR", null);
        Record r2 = new Record(10.5, 50, new BigDecimal(20), "PQR", Arrays.asList(new Integer[] { 1, 2 }));
        Record r3 = new Record(10.5, 50, new BigDecimal(20), "ABC", Arrays.asList(new Integer[] { 1, 2 }));
        Record r4 = new Record(10.5, 100, new BigDecimal(40), "ABC", Arrays.asList(new Integer[] { 1, 2, 5 }));
        List<Record> records = Arrays.asList(new Record[] { r1, r2, r3, r4 });

        Collection<Pair<Record, List<Record>>> aggregatedRecords = record1Aggregator.aggregateToList(records, "a,d");
        aggregatedRecords.stream().forEach(System.out::println);
    }

    @Test
    public void aggregateToMapTest() throws ImproperKeyException, IllegalAccessException, InstantiationException, InvocationTargetException,
            NoSuchMethodException {
        Aggregator<Record> record1Aggregator = new Aggregator<>();
        Record r1 = new Record(10.5, 5, new BigDecimal(10), "PQR", null);
        Record r2 = new Record(10.5, 50, new BigDecimal(20), "PQR", Arrays.asList(new Integer[] { 1, 2 }));
        Record r3 = new Record(10.5, 50, new BigDecimal(20), "ABC", Arrays.asList(new Integer[] { 1, 2 }));
        Record r4 = new Record(10.5, 100, new BigDecimal(40), "ABC", Arrays.asList(new Integer[] { 1, 2, 5 }));
        List<Record> records = Arrays.asList(new Record[] { r1, r2, r3, r4 });

        Map<List<Object>, Pair<Record, List<Record>>> aggregatedRecords = record1Aggregator.aggregateToMap(records, "a,d");
        aggregatedRecords.entrySet().stream().forEach(System.out::println);
    }

}