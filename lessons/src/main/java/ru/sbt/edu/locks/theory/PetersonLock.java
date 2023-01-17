package ru.sbt.edu.locks.theory;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.TwoThreadIds;

public class PetersonLock implements SLock {
    private final boolean[] wannaAquire = new boolean[2];
    private int victim;

    @Override
    public void lock() {
        int me = TwoThreadIds.myId();
        int another = TwoThreadIds.not(me);
        wannaAquire[me] = true;
        victim = me;
        while (wannaAquire[another] && victim == me) {}
    }


    @Override
    public void unlock() {
        wannaAquire[TwoThreadIds.myId()] = false;
    }

}
