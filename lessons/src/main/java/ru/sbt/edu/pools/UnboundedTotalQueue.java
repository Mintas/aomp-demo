package ru.sbt.edu.pools;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.locks.ReentrantLock;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnboundedTotalQueue<T> implements Queue<T> {
    Node head = new Node(null), tail = head;

    final ReentrantLock enqLock = new ReentrantLock();
    final ReentrantLock deqLock = new ReentrantLock();

    protected class Node {
        public T value;
        public volatile Node next;

        public Node(T x) {
            value = x;
            next = null;
        }
    }

    @Override
    public void enq(T item) {
        enqLock.lock();
        try {
            doEnq(item);
        } finally {
            enqLock.unlock();
        }
    }

    private void doEnq(T item) {
        Node e = new Node(item);
        tail.next = e;
        tail = e;
    }

    /**
     * there is a symmetry in enq/deq implementations
     */
    @Override
    public T deq() throws EmptyQueueException {
        deqLock.lock();
        try {
            if (head.next == null) throw new EmptyQueueException();
            return doDeq();
        } finally {
            deqLock.unlock();
        }
    }

    private T doDeq() {
        T result = head.next.value;
        head = head.next;
        return result;
    }
}
