package ru.sbt.edu.locks.theory;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.TwoThreadIds;

public class FirstLock implements SLock {
    private boolean[] wannaAquire = new boolean[2];

    @Override
    public void lock() {
        int me = TwoThreadIds.myId();
        //int another = TwoThreadIds.not(me);
        wannaAquire[me] = true;
        //while (wannaAquire[another]) {};
    }


    @Override
    public void unlock() {
        wannaAquire[TwoThreadIds.myId()] = false;
    }

}
