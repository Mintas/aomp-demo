package ru.sbt.edu.pools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> implements Queue<T> {
    private final Node dummy = new Node(null);
    private final AtomicReference<Node> head = new AtomicReference<>(dummy),
            tail = new AtomicReference<>(dummy);

    public class Node {
        public final T value;
        public final AtomicReference<Node> next = new AtomicReference<>(null);

        public Node(T value) {
            this.value = value;
        }
    }

    @Override
    public void enq(T item) {
        Node node = new Node(item);
        while (true) {
            Node last = tail.get();
            Node next = last.next.get();
            if (last == tail.get()) { //last still in tail
                if (next == null) { //and nothing appended to last
                    if (last.next.compareAndSet(next, node)) {//enqueue as new last
                        tail.compareAndSet(last, node); //update tail
                        return;
                    }
                } else {
                    tail.compareAndSet(last, next); //there is faster enqueuer - help him
                }
            }
        }
    }

    @Override
    public T deq() {
        while (true) {
            Node first = head.get();
            Node last = tail.get();
            Node next = first.next.get();
            if (first == head.get()) { //observed node still in head
                if (first == last) { //if queue collapsed
                    if (next == null) { //and nothing left
                        throw new EmptyQueueException();  //linearizable tho
                    } //else there is a fast enqueuer - help him
                    tail.compareAndSet(last, next);
                } else {
                    T value = next.value; //if queue is still not look like empty
                    if (head.compareAndSet(first, next)) //try dequeue, cutting reference
                        return value;
                }
            }
        }
    }
}
