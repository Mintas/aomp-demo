package ru.sbt.edu.worker;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicStampedReference;

public class BoundedDEQue {
    private final RecursiveAction[] tasks;
    private final AtomicStampedReference<Integer> top;  //Index & Stamp, synced
    private volatile int bottom; // we need memory barrier here (guess why not atomic? =))

    public BoundedDEQue(int capacity) {
        tasks = new RecursiveAction[capacity];
        top = new AtomicStampedReference<>(0, 0);
        bottom = 0;
    }

    // called by thieves to determine whether to try to steal
    boolean isEmpty() {
        return (top.getReference() < bottom);
    }

    public void pushBottom(RecursiveAction r) {
        tasks[bottom] = r;
        bottom++;
    }

    public RecursiveAction popTop() {
        int[] stamp = new int[1];
        int oldTop = top.get(stamp);
        int oldStamp = stamp[0];

        if (bottom <= oldTop)
            return null;
        RecursiveAction r = tasks[oldTop];
        if (top.compareAndSet(oldTop, oldTop + 1, oldStamp, oldStamp + 1))
            return r; //task stolen successfully!
        else //someone else stole that task! (probably taken by owner)
            return null;
    }

    public RecursiveAction popBottom() {
        if (bottom == 0) //queue is empty already
            return null;
        int newBottom = --bottom;
        RecursiveAction r = tasks[newBottom];

        int[] stamp = new int[1];
        int oldTop = top.get(stamp);
        int oldStamp = stamp[0];
        int newStamp = oldStamp + 1;
        if (newBottom > oldTop)
            return r; //more than 1 item in queue
        bottom = 0; //reset to 0, matching top and bot at the array beginning
        if (newBottom == oldTop) { //last work item
            if (top.compareAndSet(oldTop, 0, oldStamp, newStamp))
                return r;
        }
        top.set(0, newStamp);
        return null;
    }
}