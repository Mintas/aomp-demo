package ru.sbt.edu.locks.theory;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.TwoThreadIds;

import static ru.sbt.edu.utils.TwoThreadIds.myId;

public class CanLock implements SLock {
    private final boolean[] cans = new boolean[2];

    @Override
    public void lock() {
        int me = TwoThreadIds.myId();
        //int another = TwoThreadIds.not(me);
        cans[me] = true;
        //while (flag[another]) {};
    }


    @Override
    public void unlock() {
        while (cans[myId()]) {}
    }
}
