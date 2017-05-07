package com.rohan.recon;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Generic aggregation utility for list of records based on a key<br/>
 * Note:-The list of records need to have public getters and setters for all the
 * Key Fields
 * 
 * @author piyush
 *
 * @param <T>
 */
public class Aggregator<T> {

    /**
     * Aggregates the records to a collection
     * 
     * @param records
     *            List of records to be aggregated
     * @param key
     *            Key for the aggregation
     * @return Collection of aggregated & constituent record pairs
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ImproperSortKeyException
     *             Throws Exception of Improper Aggregation Key
     */
    public Collection<Pair<T, List<T>>> aggregateToList(final List<T> records, final String key) throws ImproperKeyException, IllegalAccessException,
            InstantiationException, InvocationTargetException, NoSuchMethodException {
        Map<List<Object>, Pair<T, List<T>>> aggregatedMap = aggregateToMap(records, key);
        return aggregatedMap.values();
    }

    /**
     * Aggregates the records to a map based on values of key
     * 
     * @param records
     *            List of Records to be aggregated
     * @param key
     *            Aggregation Key
     * @return Map from key to Pair of aggregated records & individual records
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ImproperAggregationKeyException
     *             Throws Exception of Improper Aggregation Key
     * 
     */
    public Map<List<Object>, Pair<T, List<T>>> aggregateToMap(final List<T> records, final String key) throws ImproperKeyException,
            IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if (records == null || records.isEmpty())
            return new HashMap<>();

        Set<String> keyFields = new HashSet<>(Arrays.asList(key.split(ReconUtils.COMMA_STR)));

        if (!ReconUtils.isValidKey(records.get(0).getClass(), keyFields)) {
            throw new ImproperKeyException("Improper Aggregation Key", key);
        }

        System.out.println("Start aggregating the records");

        Map<List<Object>, Pair<T, List<T>>> map = new HashMap<>();

        for (int i = 0; i < records.size(); i++) {
            List<Object> keyValues = getKeyValues(records.get(i), keyFields);
            if (map.containsKey(keyValues)) {
                T oldAggregatedRecord = map.get(keyValues).getLeft();
                // aggregate the current record with the old record, and add the
                // new record to the aggregated records list
                mergeRecords(records.get(i), oldAggregatedRecord, keyFields);
                map.get(keyValues).getRight().add(records.get(i));
            } else {
                @SuppressWarnings("unchecked")
                // Do not tamper the original record, make a copy
                T copyOfRecord = (T) BeanUtils.cloneBean(records.get(i));
                List<T> constituentRecords = new ArrayList<>();
                constituentRecords.add(records.get(i));
                Pair<T, List<T>> aggregatedRecordsPair = Pair.of(copyOfRecord, constituentRecords);
                map.put(keyValues, aggregatedRecordsPair);
            }
        }
        System.out.println("Merged the records");

        return map;
    }

    /*
     * Merges the contents of the Current record in the Old Record
     */
    protected void mergeRecords(T currentRecord, T oldRecord, final Set<String> keyFields) {
        Set<String> allFields = Arrays.stream(currentRecord.getClass().getDeclaredFields()).map(field -> field.getName()).collect(Collectors.toSet());
        // Do NOT aggregate the key fields
        allFields.removeAll(keyFields);
        PropertyAccessor currentRecordPropertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(currentRecord);
        PropertyAccessor oldRecordPropertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(oldRecord);

        for (String field : allFields) {
            Object currObjVal = currentRecordPropertyAccessor.getPropertyValue(field);
            Object oldObjVal = oldRecordPropertyAccessor.getPropertyValue(field);

            // only aggregate if the current object field is not null
            if (currObjVal == null) {
                continue;
            }
            // In case of null old value, put the current one
            if (oldObjVal == null) {
                oldRecordPropertyAccessor.setPropertyValue(field, currObjVal);
            }

            switch (JavaType.get(currObjVal.getClass().getName())) {
            case INTEGER:
                oldRecordPropertyAccessor.setPropertyValue(field, (Integer) oldObjVal + (Integer) currObjVal);
                break;
            case LONG:
                oldRecordPropertyAccessor.setPropertyValue(field, (Long) oldObjVal + (Long) currObjVal);
                break;
            case SHORT:
                oldRecordPropertyAccessor.setPropertyValue(field, (Short) oldObjVal + (Short) currObjVal);
                break;
            case FLOAT:
                oldRecordPropertyAccessor.setPropertyValue(field, (Float) oldObjVal + (Float) currObjVal);
                break;
            case DOUBLE:
                oldRecordPropertyAccessor.setPropertyValue(field, (Double) oldObjVal + (Double) currObjVal);
                break;
            case BIG_INTEGER:
                oldRecordPropertyAccessor.setPropertyValue(field, ((BigInteger) oldObjVal).add((BigInteger) currObjVal));
                break;
            case BIG_DECIMAL:
                oldRecordPropertyAccessor.setPropertyValue(field, ((BigDecimal) oldObjVal).add((BigDecimal) currObjVal));
                break;
            case STRING:
                oldRecordPropertyAccessor.setPropertyValue(field, (String) oldObjVal + ReconUtils.COMMA_STR + (String) currObjVal);
                break;
            default:
                break;
            }

        }

    }

    /*
     * Helper method to accumulate the values of the Key Fields Specified, from
     * the record object
     */
    private List<Object> getKeyValues(final T record, final Set<String> keyFields) {
        List<Object> keyValues = new ArrayList<>();
        PropertyAccessor recordPropertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(record);
        for (String keyField : keyFields) {
            keyValues.add(recordPropertyAccessor.getPropertyValue(keyField));
        }
        return keyValues;
    }

}

/**
 * Enum for all supported Java Types which could be aggregated
 * 
 * @author piyush
 *
 */
enum JavaType {
    INTEGER("java.lang.Integer"), LONG("java.lang.Long"), FLOAT("java.lang.Float"), DOUBLE("java.lang.Double"), SHORT("java.lang.Short"), BIG_INTEGER(
            "java.math.BigInteger"), BIG_DECIMAL("java.math.BigDecimal"), BOOLEAN("java.lang.Boolean"), STRING("java.lang.String"), CUSTOM("");

    private final String javaClassName;

    private static final Map<String, JavaType> lookup = new HashMap<String, JavaType>();

    static {
        for (JavaType javaType : JavaType.values()) {
            lookup.put(javaType.getJavaClassName(), javaType);
        }
    }

    static JavaType get(String javaType) {
        return lookup.containsKey(javaType) ? lookup.get(javaType) : CUSTOM;
    }

    JavaType(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    String getJavaClassName() {
        return this.javaClassName;
    }

}
