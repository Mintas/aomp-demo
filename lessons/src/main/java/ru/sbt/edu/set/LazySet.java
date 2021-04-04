package ru.sbt.edu.set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiPredicate;

public class LazySet<T> implements ISet<T> {
    private final Node tail = new Node(null, Integer.MAX_VALUE);
    private final Node head = new Node(null, Integer.MIN_VALUE, tail, false);

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    private class Node {
        private final T item;
        private final int key;
        private Node next;

        private final Lock lock = new ReentrantLock();
        private boolean marked;

        Node(T item) {
            this(item, item.hashCode(), null, false);
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }

        void markDeleted() {
            marked = true;
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
                this::doMarkAndRemove,
                (pred, curr) -> false);
    }

    private boolean doMarkAndRemove(Node pred, Node curr) {
        curr.markDeleted(); //logical
        pred.next = curr.next; //physical removal
        return true;
    }

    private boolean scan(T item, BiPredicate<Node, Node> onHit, BiPredicate<Node, Node> onMiss) {
        int key = item.hashCode();
        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            //now we are in place that is possible our scan solution
            //lets validate it is consistent and proceed our action
            pred.lock();
            try {
                curr.lock();
                try {
                    if (!validate(pred, curr)) {
                        //inconsistent, rescan!
                        continue;
                    }
                    //found item!
                    if (key == curr.key) {
                        return onHit.test(pred, curr);
                    }
                    //item is missing
                    return onMiss.test(pred, curr);
                } finally {
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    private boolean validate(Node pred, Node curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }

    @Override
    public boolean contains(T item) {
        int key = item.hashCode();
        Node curr = head;
        while (curr.key < key)
            curr = curr.next;
        return curr.key == key && !curr.marked;
    }
}
