package ru.sbt.edu.locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class TasLock  implements SLock {
    private final AtomicBoolean locked = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (locked.getAndSet(true)) {
            //busy waiting aka spinning
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
    }
}