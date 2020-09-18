package ru.sbt.edu.locks.theory;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.TwoThreadIds;

import static java.lang.Integer.parseInt;

public class PetersonLock implements SLock {
    private boolean[] wannaAquire = new boolean[2];
    private int porter;

    @Override
    public void lock() {
        int me = TwoThreadIds.myId();
        int another = TwoThreadIds.not(me);
        wannaAquire[me] = true;
        porter = me;
        while (wannaAquire[another] && porter == me) {};
    }


    @Override
    public void unlock() {
        wannaAquire[TwoThreadIds.myId()] = false;
    }

}
