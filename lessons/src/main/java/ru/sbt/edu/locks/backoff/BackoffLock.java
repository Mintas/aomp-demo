package ru.sbt.edu.locks.backoff;

import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackoffLock implements SLock {
    private static final int MIN_DELAY = 1, MAX_DELAY = 1000;

    private final AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);
        while (true) {
            while (state.get()) {
                Thread.onSpinWait();
            }
            if (!state.getAndSet(true)) {
                return;
            } else {
                backoff.backoff();
            }
        }
    }

    public void unlock() {
        state.set(false);
    }
}