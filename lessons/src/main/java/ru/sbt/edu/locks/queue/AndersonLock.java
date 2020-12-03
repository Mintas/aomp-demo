package ru.sbt.edu.locks.queue;


import ru.sbt.edu.locks.SLock;

import java.util.concurrent.atomic.AtomicInteger;

public class AndersonLock implements SLock {
    private final ThreadLocal<Integer> mySlotIndex = ThreadLocal.withInitial(() -> 0);
    private final AtomicInteger tail;
    private volatile boolean[] flag; //here to exclude compiler optimisations in while loop, not to introduce memory barrier
    private final int size;

    public AndersonLock(int capacity) {
        size = capacity;
        tail = new AtomicInteger(0);
        flag = new boolean[capacity];
        flag[0] = true;
    }

    @Override
    public void lock() {
        int slot = chooseSlot();
        localSpinning(slot);
    }

    private int chooseSlot() {
        int slot = tail.getAndIncrement() % size;
        mySlotIndex.set(slot);
        return slot;
    }

    private void localSpinning(int slot) {
        while (!flag[slot]) {
        }
    }

    @Override
    public void unlock() {
        int slot = releaseSlot();
        notify(slot);
    }

    private int releaseSlot() {
        int slot = mySlotIndex.get();
        flag[slot] = false;
        return slot;
    }

    private void notify(int slot) {
        flag[(slot + 1) % size] = true;
    }
}
