package ru.sbt.edu.set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiPredicate;

public class CoarseGrainedSet<T> implements ISet<T> {
    private final Node tail = new Node(null, Integer.MAX_VALUE);
    private final Node head = new Node(null, Integer.MIN_VALUE, tail);
    private final Lock lock = new ReentrantLock();

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    private class Node {
        private final T item;
        private final int key;
        private Node next;

        Node(T item) {
            this(item, item.hashCode(), null);
        }
    }

    @Override
    public boolean add(T item) {
        return scan(item,
                (pred, curr) -> false,
                (pred, curr) -> doAdd(item, pred, curr));
    }

    private boolean doAdd(T item, Node pred, Node curr) {
        Node node = new Node(item);
        node.next = curr;
        pred.next = node;
        return true;
    }

    @Override
    public boolean remove(T item) {
        return scan(item,
                this::doRemove,
                (pred, curr) -> false);
    }

    private boolean doRemove(Node pred, Node curr) {
        pred.next = curr.next;
        return true;
    }

    private boolean scan(T item, BiPredicate<Node, Node> onHit, BiPredicate<Node, Node> onMiss) {
        int key = item.hashCode();
        //initialize lookup
        Node pred = head;
        Node curr = pred.next;
        lock.lock();
        try {
            //seq scan
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            //found item!
            if (key == curr.key) {
                return onHit.test(pred, curr);
            }
            //item is missing
            return onMiss.test(pred, curr);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(T item) {
        return scan(item,
                (pred, curr) -> true,
                (pred, curr) -> false);
    }
}