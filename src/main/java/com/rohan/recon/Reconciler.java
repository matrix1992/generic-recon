package com.rohan.recon;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

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
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public List<RecUnit> reconcile(final List<S1> side1Records, final List<S2> side2Records, final String reconKey,
            final Evaluator<S1, S2, R> evaluator) throws ImproperKeyException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {

        if (!ReconUtils.isValidKey(side1Records.get(0).getClass(), reconKey)) {
            throw new ImproperKeyException("Improper Recon Key for Side1 Records", reconKey);
        }

        if (!ReconUtils.isValidKey(side2Records.get(0).getClass(), reconKey)) {
            throw new ImproperKeyException("Improper Recon Key for Side2 Records", reconKey);
        }

        Aggregator<S1> side1Aggregator = new Aggregator<>();
        Aggregator<S2> side2Aggregator = new Aggregator<>();
        Map<List<Object>, Pair<S1, List<S1>>> side1AggregatedMap = side1Aggregator.aggregateToMap(side1Records, reconKey);
        Map<List<Object>, Pair<S2, List<S2>>> side2AggregatedMap = side2Aggregator.aggregateToMap(side2Records, reconKey);

        Set<List<Object>> allRecordKeys = new HashSet<>();
        allRecordKeys.addAll(side1AggregatedMap.keySet());
        allRecordKeys.addAll(side2AggregatedMap.keySet());

        List<RecUnit> recUnits = new ArrayList<>();
        for (List<Object> recordKey : allRecordKeys) {

            S1 aggregatedSide1Record = null;
            S2 aggregatedSide2Record = null;
            List<S1> side1RecordsForKey = null;
            List<S2> side2RecordsForKey = null;

            if (side1AggregatedMap.containsKey(recordKey)) {
                aggregatedSide1Record = side1AggregatedMap.get(recordKey).getLeft();
                side1RecordsForKey = side1AggregatedMap.get(recordKey).getRight();
            }

            if (side2AggregatedMap.containsKey(recordKey)) {
                aggregatedSide2Record = side2AggregatedMap.get(recordKey).getLeft();
                side2RecordsForKey = side2AggregatedMap.get(recordKey).getRight();
            }

            R recResult = evaluator.evaluate(aggregatedSide1Record, aggregatedSide2Record);
            RecUnit recUnit = new RecUnit(aggregatedSide1Record, aggregatedSide2Record, recResult, side1RecordsForKey, side2RecordsForKey);
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
        private final S1 aggregatedSide1Record;
        private final S2 aggregatedSide2Record;
        private final R recResult;
        private final List<S1> side1Records;
        private final List<S2> side2Records;

        public RecUnit(S1 aggregatedSide1Record, S2 aggregatedSide2Record, R recResult, List<S1> side1Records, List<S2> side2Records) {
            this.aggregatedSide1Record = aggregatedSide1Record;
            this.aggregatedSide2Record = aggregatedSide2Record;
            this.recResult = recResult;
            this.side1Records = side1Records;
            this.side2Records = side2Records;
        }

        @Override
        public String toString() {
            return "RecUnit [aggregatedSide1Record=" + aggregatedSide1Record + ", aggregatedSide2Record=" + aggregatedSide2Record + ", recResult="
                    + recResult + ", side1Records=" + side1Records + ", side2Records=" + side2Records + "]";
        }

        public S1 getAggregatedSide1Record() {
            return aggregatedSide1Record;
        }

        public S2 getAggregatedSide2Record() {
            return aggregatedSide2Record;
        }

        public R getRecResult() {
            return recResult;
        }

        public List<S1> getSide1Records() {
            return side1Records;
        }

        public List<S2> getSide2Records() {
            return side2Records;
        }

    }

}
