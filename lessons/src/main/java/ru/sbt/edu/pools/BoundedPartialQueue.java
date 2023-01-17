package ru.sbt.edu.pools;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @param <T> non-null items for simplicity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoundedPartialQueue<T> implements Queue<T> {
    Node head = new Node(null), tail = head; //why not volatile?

    final ReentrantLock enqLock = new ReentrantLock();
    final ReentrantLock deqLock = new ReentrantLock();
    final Condition notEmptyCondition = deqLock.newCondition(),
            notFullCondition = enqLock.newCondition();

    final AtomicInteger size = new AtomicInteger(0);
    final int capacity; //const

    public BoundedPartialQueue(int capacity) {
        this.capacity = capacity;
    }

    protected class Node {
        public final T value;
        public volatile Node next;

        public Node(T x) {
            value = x;
            next = null;
        }
    }

    @Override
    public void enq(T item) {
        boolean mustWakeDequeuers = false;
        enqLock.lock();
        try {
            awaitForSlot();
            doEnq(item);
            if (size.getAndIncrement() == 0) mustWakeDequeuers = true;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            enqLock.unlock();
        }

        if (mustWakeDequeuers) {
            signalDequeuers();
        }
    }

    private void awaitForSlot() throws InterruptedException {
        while (size.get() == capacity) {
            notFullCondition.await();
        }
    }

    private void doEnq(T item) {
        Node e = new Node(item);
        tail.next = e;
        tail = e;
    }

    private void signalDequeuers() {
        deqLock.lock(); //introduce waiting of all active dequeuers will finish their job  (dequeue or fall into waiting)
        try {
            notEmptyCondition.signalAll();
        } finally {
            deqLock.unlock();
        }
    }

    /**
     * there is a symmetry in enq/deq implementations
     */
    @Override
    public T deq() {
        T result = null;
        boolean mustWakeEnqueuers = false;
        deqLock.lock();
        try {
            awaitForItem();
            result = doDeq();
            if (size.getAndDecrement() == capacity) mustWakeEnqueuers = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            deqLock.unlock();
        }
        if (mustWakeEnqueuers) {
            signalEnqueuers();
        }
        return result;
    }

    private T doDeq() { //while we do this inside dequeueLock.lock() section
        T result = head.next.value;
        head = head.next;
        return result;
    }

    private void awaitForItem() throws InterruptedException {
        while (head.next == null) {
            notEmptyCondition.await();
        }
    }

    private void signalEnqueuers() {
        enqLock.lock();
        try {
            notFullCondition.signalAll();
        } finally {
            enqLock.unlock();
        }
    }
}
