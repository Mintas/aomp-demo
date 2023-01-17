package ru.sbt.edu.locks.queue;

import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements SLock {
    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> myPred;
    private final ThreadLocal<QNode> myNode;

    public CLHLock() {
        tail = new AtomicReference<>(null);
        //introduce implicit queue structure
        myNode = ThreadLocal.withInitial(QNode::new);
        myPred = ThreadLocal.withInitial(() -> null);
    }

    @Override
    public void lock() {
        QNode pred = announceAcquiring();
        myPred.set(pred);
        localSpinning(pred);
    }

    private QNode announceAcquiring() {
        QNode qnode = myNode.get();
        qnode.lock();
        return tail.getAndSet(qnode);
    }

    private void localSpinning(QNode pred) {
        while (pred.isLocked()) {
            Thread.onSpinWait();
        }
    }

    @Override
    public void unlock() {
        release();
        reusePredecessorNode();
    }

    private void release() {
        myNode.get().unlock();
    }

    private void reusePredecessorNode() {
        myNode.set(myPred.get());
    }

    private static class QNode {
        private final AtomicBoolean locked = new AtomicBoolean(true);

        public boolean isLocked() {
            return locked.get();
        }

        public void lock(){
            locked.set(true);
        }

        public void unlock(){
            locked.set(false);
        }
    }
}
