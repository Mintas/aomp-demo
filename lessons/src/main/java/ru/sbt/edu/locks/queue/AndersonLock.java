package ru.sbt.edu.locks.queue;


import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class AndersonLock implements SLock {
    private final ThreadLocal<Integer> mySlotIndex = ThreadLocal.withInitial(() -> 0);
    private final AtomicInteger tail;
    private final boolean[] permission; //
    private final int size;

    public AndersonLock(int capacity) {
        size = capacity;
        tail = new AtomicInteger(0);
        permission = new boolean[capacity];
        permission[0] = true;
    }

    @Override
    public void lock() {
        int slot = chooseSlot();
        localSpinning(slot);
    }

    private int chooseSlot() {
        int slot = tail.getAndIncrement() % size; //memory barrier
        mySlotIndex.set(slot);
        return slot;
    }

    private void localSpinning(int slot) {
        while (!permission[slot]) {
            Thread.onSpinWait();
        }
    }

    @Override
    public void unlock() {
        int slot = releaseSlot();
        notify(slot);
    }

    private int releaseSlot() {
        int slot = mySlotIndex.get();
        permission[slot] = false;
        return slot;
    }

    private void notify(int slot) {
        permission[(slot + 1) % size] = true;
    }
}
