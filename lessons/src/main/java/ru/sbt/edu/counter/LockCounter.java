package ru.sbt.edu.counter;

import ru.sbt.edu.locks.SLock;

public class LockCounter implements Counter {
    private final SLock lock;
    private int count;

    public LockCounter(SLock lock) {
        this.lock = lock;
        this.count = 0;
    }

    @Override
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int value() {
        return count;
    }
}
