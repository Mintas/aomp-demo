package ru.sbt.edu.pools;

/**
 * Pools are usually used in Producer-Consumer scenarios
 * to introduce back-pressure
 * or in managing expensive resources effectively
 * @param <T> type of items to hold
 */
public interface Pool<T> {
    /**
     * Method implementations can be
     * partial (include waiting to correctness conditions)
     * or total (fail-fast)
     * @param item
     */
    void put(T item);
    T get();
}
