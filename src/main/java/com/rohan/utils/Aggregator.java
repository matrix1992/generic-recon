package com.rohan.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

<<<<<<< HEAD
    private static final String COMMA_STR = ",";

=======
>>>>>>> 72ccd6ad29bd0581aab13084e011b9316ae78720
    /**
     * Aggregates the records
     * 
     * @param records
     *            List of records to be aggregated
     * @param key
     *            Key for the aggregation
     * @return Collection of aggregated records
     * @throws ImproperAggregationKeyException
     *             Throws Exception of Improper Aggregation Key
     */
    public Collection<T> aggregate(final List<T> records, final String key) throws ImproperAggregationKeyException {
        if (records == null || records.isEmpty())
            return records;

<<<<<<< HEAD
        Set<String> keyFields = new HashSet<>(Arrays.asList(key.split(COMMA_STR)));
=======
        Set<String> keyFields = new HashSet<>(Arrays.asList(key.split(",")));
>>>>>>> 72ccd6ad29bd0581aab13084e011b9316ae78720

        if (!isValidKey(records.get(0).getClass(), keyFields)) {
            throw new ImproperAggregationKeyException(key);
        }

        System.out.println("Start aggregating the records");

        Map<Set<Object>, T> map = new HashMap<>();

        for (int i = 0; i < records.size(); i++) {
            Set<Object> keyValues = getKeyValues(records.get(i), keyFields);
            if (map.containsKey(keyValues)) {
                T oldRecord = map.get(keyValues);
                // aggregate the current record with the old record
                mergeRecords(records.get(i), oldRecord, keyFields);
            } else {
                map.put(keyValues, records.get(i));
            }
        }
        System.out.println("Merged the records");

        return map.values();
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
<<<<<<< HEAD
                oldRecordPropertyAccessor.setPropertyValue(field, (String) oldObjVal + COMMA_STR + (String) currObjVal);
=======
                oldRecordPropertyAccessor.setPropertyValue(field, (String) oldObjVal + (String) currObjVal);
>>>>>>> 72ccd6ad29bd0581aab13084e011b9316ae78720
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
    private Set<Object> getKeyValues(final T record, final Set<String> keyFields) {
        Set<Object> keyValues = new HashSet<>();
        PropertyAccessor recordPropertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(record);
        for (String keyField : keyFields) {
            keyValues.add(recordPropertyAccessor.getPropertyValue(keyField));
        }
        return keyValues;
    }

    /*
     * Validates if the key is proper for the class
     */
    private boolean isValidKey(final Class<?> clazz, final Set<String> keyFields) {

        Set<String> temp = new HashSet<>(keyFields);

        Set<String> applicableFields = Arrays.stream(clazz.getDeclaredFields()).map(field -> field.getName()).collect(Collectors.toSet());

        temp.removeAll(applicableFields);

        return temp.size() == 0;
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

/**
 * Custom Exception class for indicating an incorrect aggregation key
 * 
 * @author piyush
 *
 */
class ImproperAggregationKeyException extends Exception {

    String message = null;

    private static final long serialVersionUID = 5427721028775303056L;

    public ImproperAggregationKeyException(String key) {
        this.message = "Improper Aggregation Key " + key;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}