package ru.sbt.edu.pools;

/**
 * LIFO pool of objects
 * @param <T>
 */
public interface Stack<T> extends Pool<T> {
    void push(T item);

    @Override
    default void put(T item) {
        push(item);
    }

    T pop();

    @Override
    default T get() {
        return pop();
    }
}
