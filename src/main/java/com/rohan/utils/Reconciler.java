package com.rohan.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generic Reconciliation Utility between Two different Sources.<br/>
 * 
 * @author piyush
 *
 * @param <S1>
 * @param <S2>
 * @param <R>
 */
public class Reconciler<S1, S2, R> {

    /**
     * Performs reconciliation between side1 and side2 records based on the
     * specified <b>Recon Key</b> and gives list of Rec Units
     * 
     * @param side1Records
     *            Side 1 Records
     * @param side2Records
     *            Side 2 Records
     * @param reconKey
     *            Key for Reconciliation
     * @param evaluator
     *            Evaluator for finding the reconciliation result
     * @return List of Rec Units
     * @throws ImproperAggregationKeyException
     * @throws ImproperKeyException
     */
    public List<RecUnit> reconcile(List<S1> side1Records, List<S2> side2Records, String reconKey, Evaluator<S1, S2, R> evaluator)
            throws ImproperKeyException {

        if (!ReconUtils.isValidKey(side1Records.get(0).getClass(), reconKey)) {
            throw new ImproperKeyException("Improper Recon Key for Side1 Records", reconKey);
        }

        if (!ReconUtils.isValidKey(side2Records.get(0).getClass(), reconKey)) {
            throw new ImproperKeyException("Improper Recon Key for Side2 Records", reconKey);
        }

        Aggregator<S1> side1Aggregator = new Aggregator<>();
        Aggregator<S2> side2Aggregator = new Aggregator<>();
        Map<List<Object>, S1> side1AggregatedMap = side1Aggregator.aggregateToMap(side1Records, reconKey);
        Map<List<Object>, S2> side2AggregatedMap = side2Aggregator.aggregateToMap(side2Records, reconKey);

        Set<List<Object>> allRecordKeys = new HashSet<>();
        allRecordKeys.addAll(side1AggregatedMap.keySet());
        allRecordKeys.addAll(side2AggregatedMap.keySet());

        List<RecUnit> recUnits = new ArrayList<>();
        for (List<Object> recordKey : allRecordKeys) {
            S1 side1Record = side1AggregatedMap.get(recordKey);
            S2 side2Record = side2AggregatedMap.get(recordKey);
            R recResult = evaluator.evaluate(side1Record, side2Record);
            RecUnit recUnit = new RecUnit(side1Record, side2Record, recResult);
            recUnits.add(recUnit);
        }

        return recUnits;
    }

    /**
     * Rec Unit class for storing the Reconciliation Result
     * 
     * @author piyush
     *
     */
    class RecUnit {
        S1 side1;
        S2 side2;
        R recResult;

        public RecUnit(S1 side1, S2 side2, R recResult) {
            this.side1 = side1;
            this.side2 = side2;
            this.recResult = recResult;
        }

        @Override
        public String toString() {
            return "RecUnit [side1=" + side1 + ", side2=" + side2 + ", recResult=" + recResult + "]";
        }
    }

}
