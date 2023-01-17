package ru.sbt.edu.locks.tas;

import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicBoolean;

public class TasLock  implements SLock {
    private final AtomicBoolean locked = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (locked.getAndSet(true)) {// M ||| I -> I ? Mem .. S .. M ||| I
            Thread.onSpinWait();
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
    }
}