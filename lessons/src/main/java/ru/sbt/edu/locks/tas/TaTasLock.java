package ru.sbt.edu.locks.tas;

import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaTasLock implements SLock {
    private final AtomicBoolean locked = new AtomicBoolean(false);

    @Override
    public void lock() {
        while(true) {
            while (locked.get()) { // re-read local cache copy S ||| I -> Mem.. S
                Thread.onSpinWait();
            } //all threads see changes simultaneously ''
            if (!locked.getAndSet(true)) return; // M ||| I ->
        }
        //
    }

    @Override
    public void unlock() {
        locked.set(false);
    } // M ||| I
}