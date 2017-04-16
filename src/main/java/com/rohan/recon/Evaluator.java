package com.rohan.recon;

@FunctionalInterface
public interface Evaluator<S1, S2, R> {

    /**
     * Evaluates side 1 and side 2 and returns result
     *
     * @param side1
     *            the first evaluation argument
     * @param side2
     *            the second evaluation argument
     * @return the evaluation result
     */
    R evaluate(S1 side1, S2 side2);
}
