package ru.sbt.edu.set;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeSet<T> implements ISet<T> {
    private final Node tail = new Node(null, Integer.MAX_VALUE, (Node) null);
    private final Node head  = new Node(null, Integer.MIN_VALUE, tail);

    public boolean add(T item) {
        int key = item.hashCode();
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
                if (pred.nextAndMyMark.compareAndSet(curr, node, false, false)) {
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
                Node succ = curr.nextAndMyMark.getReference();
                snip = curr.nextAndMyMark.attemptMark(succ, true);
                if (!snip)
                    continue;
                pred.nextAndMyMark.compareAndSet(curr, succ, false, false);
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
        // find predecessor and current entries
        Window window = find(head, key);
        Node curr = window.curr;
        return (curr.key == key);
    }

    @AllArgsConstructor
    private class Node {
        private final T item;
        private final int key;
        private final AtomicMarkableReference<Node> nextAndMyMark;

        Node(T item, int key, Node nextAndMyMark) {
            this(item, key, new AtomicMarkableReference<>(nextAndMyMark, false));
        }

        Node(T item, Node nextAndMyMark) {
            this(item,  item.hashCode(), nextAndMyMark);
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
        rescan: while (true) { //goto
            pred = head;
            curr = pred.nextAndMyMark.getReference();
            while (true) {
                succ = curr.nextAndMyMark.get(marked);
                while (marked[0]) {           // replace curr if marked
                    snip = pred.nextAndMyMark.compareAndSet(curr, succ, false, false);
                    if (!snip) continue rescan;
                    curr = pred.nextAndMyMark.getReference();
                    succ = curr.nextAndMyMark.get(marked);
                }
                if (curr.key >= key)
                    return new Window(pred, curr);
                pred = curr;
                curr = succ;
            }
        }
    }
}
