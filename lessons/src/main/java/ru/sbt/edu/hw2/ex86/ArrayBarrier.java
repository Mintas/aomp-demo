package ru.sbt.edu.hw2.ex86;

import java.util.concurrent.atomic.AtomicInteger;

public class ArrayBarrier implements Barrier {
    private final AtomicInteger identifier = new AtomicInteger();
    private final ThreadLocal<Integer> me = ThreadLocal.withInitial(identifier::getAndIncrement);
    private final int barrier[];
    private final int parties;


    public ArrayBarrier(int parties) {
        this.barrier = new int[parties];
        this.parties = parties;
    }

    @Override
    public void await() {
        Integer id = me.get();
        arrive(id);
        notify(id);
    }

    private void arrive(Integer id) {
        if (id != 0) while (barrier[id - 1] != 1) {}
        barrier[id] = 1;
    }

    private void notify(Integer id) {
        if (id != parties-1) while (barrier[id + 1] != 2) {}
        barrier[id] = 2;
    }
}