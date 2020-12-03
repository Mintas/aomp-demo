package ru.sbt.edu.locks.tas;

import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaTasLock implements SLock {
    private final AtomicBoolean locked = new AtomicBoolean(false);

    @Override
    public void lock() {
        while(true) {
            while (locked.get()) {}
            if (!locked.getAndSet(true)) return;
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
    }
}