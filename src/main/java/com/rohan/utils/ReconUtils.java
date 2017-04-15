package com.rohan.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reconciler Utility class
 * 
 * @author piyush
 *
 */
public final class ReconUtils {

    public static final String COMMA_STR = ",";

    private ReconUtils() {
    }

    /*
     * Validates if the key is proper for the class
     */
    public static boolean isValidKey(final Class<?> clazz, final Set<String> keyFields) {

        Set<String> temp = new HashSet<>(keyFields);

        Set<String> applicableFields = Arrays.stream(clazz.getDeclaredFields()).map(field -> field.getName()).collect(Collectors.toSet());

        temp.removeAll(applicableFields);

        return temp.size() == 0;
    }

    /*
     * Validates if the key is proper for the class
     */
    public static boolean isValidKey(final Class<?> clazz, final String keyFieldsStr) {

        Set<String> keyFields = new HashSet<>(Arrays.asList(keyFieldsStr.split(COMMA_STR)));
        return isValidKey(clazz, keyFields);
    }

}
