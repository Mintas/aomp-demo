package ru.sbt.edu.hw2.ex86;

import ru.sbt.edu.locks.tas.TaTasLock;

public class CountingBarrier implements Barrier {
    private final TaTasLock lock = new TaTasLock();
    private int counter = 0;

    private final int parties;

    public CountingBarrier(int parties) {
        this.parties = parties;
    }


    @Override
    public void await() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }

        while (counter < parties) {}
    }
}
