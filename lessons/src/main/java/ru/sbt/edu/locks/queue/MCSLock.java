package ru.sbt.edu.locks.queue;

import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements SLock {
    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myNode;

    public MCSLock() {
        tail = new AtomicReference<QNode>(null);
        myNode = ThreadLocal.withInitial(QNode::new);
    }

    @Override
    public void lock() {
        QNode qNode = myNode.get();
        QNode pred = registerGlobally(qNode);

        if (queueIsEmpty(pred)) return;

        announceAndRegister(qNode, pred);
        waitForNotification(qNode);
    }

    private QNode registerGlobally(QNode qNode) {
        return tail.getAndSet(qNode);
    }

    private boolean queueIsEmpty(QNode pred) {
        return pred == null;
    }

    private void announceAndRegister(QNode qNode, QNode pred) {
        qNode.locked = true;
        pred.next = qNode;
    }

    private void waitForNotification(QNode qNode) {
        while (qNode.locked) {}
    }

    @Override
    public void unlock() {
        QNode qNode = myNode.get();
        if (qNode.next == null) {
            if (amTheLastOne(qNode)) return;
            waitForSuccessor(qNode);
        }
        releaseAndNotificate(qNode);
    }

    private void releaseAndNotificate(QNode qNode) {
        qNode.next.locked = false;
        qNode.next = null;
    }

    private boolean amTheLastOne(QNode qNode) {
        return tail.compareAndSet(qNode, null);
    }

    private void waitForSuccessor(QNode qNode) {
        while (qNode.next == null) {}
    }

    private static class QNode {
        volatile boolean locked = false;
        volatile QNode next = null;
    }
}
