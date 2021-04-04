package ru.sbt.edu.set;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeSet<T> implements ISet<T> {
    private final Node tail = new Node(null, Integer.MAX_VALUE, (Node) null);
    private final Node head  = new Node(null, Integer.MIN_VALUE, tail);

    public boolean add(T item) {
        int key = item.hashCode();
        boolean splice;
        while (true) {
            // find predecessor and curren entries
            Window window = find(head, key);
            Node pred = window.pred, curr = window.curr;
            // is the key present?
            if (curr.key == key) {
                return false;
            } else {
                // splice in new node
                Node node = new Node(item, curr);
                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }
    }

    public boolean remove(T item) {
        int key = item.hashCode();
        boolean snip;
        while (true) {
            // find predecessor and curren entries
            Window window = find(head, key);
            Node pred = window.pred, curr = window.curr;
            // is the key present?
            if (curr.key != key) {
                return false;
            } else {
                // snip out matching node
                Node succ = curr.next.getReference();
                snip = curr.next.attemptMark(succ, true);
                if (!snip)
                    continue;
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }
    /**
     * Test whether element is present
     * @param item element to test
     * @return true iff element is present
     */
    public boolean contains(T item) {
        int key = item.hashCode();
        // find predecessor and curren entries
        Window window = find(head, key);
        Node pred = window.pred, curr = window.curr;
        return (curr.key == key);
    }

    @AllArgsConstructor
    private class Node {
        private final T item;
        private final int key;
        private final AtomicMarkableReference<Node> next;

        Node(T item, int key, Node next) {
            this(item, key, new AtomicMarkableReference<>(next, false));
        }

        Node(T item, Node next) {
            this(item,  item.hashCode(), next);
        }
    }

    /**
     * Pair of adjacent list entries.
     */
    @RequiredArgsConstructor
    class Window {
        /**
         * Earlier node.
         */
        private final Node pred;
        /**
         * Later node.
         */
        private final Node curr;
    }

    /**
     * If element is present, returns node and predecessor. If absent, returns
     * node with least larger key.
     * @param head start of list
     * @param key key to search for
     * @return If element is present, returns node and predecessor. If absent, returns
     * node with least larger key.
     */
    public Window find(Node head, int key) {
        Node pred, curr, succ;
        boolean[] marked = {false}; // is curr marked?
        boolean snip;
        retry: while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                succ = curr.next.get(marked);
                while (marked[0]) {           // replace curr if marked
                    snip = pred.next.compareAndSet(curr, succ, false, false);
                    if (!snip) continue retry;
                    curr = pred.next.getReference();
                    succ = curr.next.get(marked);
                }
                if (curr.key >= key)
                    return new Window(pred, curr);
                pred = curr;
                curr = succ;
            }
        }
    }
}
