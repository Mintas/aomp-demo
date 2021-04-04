package ru.sbt.edu.pools;

/**
 * @param <T> ordered sequence of items (of type T)
 *  It provides an enq(x) method that puts item to the tail,
 *  and a deq() method that removes and returns the item from the head.
 *  A concurrent queue is linearizable to a sequential queue.
 */
public interface Queue<T> extends Pool<T> {
    void enq(T item);

    @Override
    default void put(T item) {
        enq(item);
    }

    T deq();

    @Override
    default T get() {
        return deq();
    }
}
