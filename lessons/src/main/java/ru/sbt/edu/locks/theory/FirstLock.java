package ru.sbt.edu.locks.theory;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.TwoThreadIds;

import static ru.sbt.edu.utils.TwoThreadIds.myId;

public class FirstLock implements SLock {
    private final boolean[] flag = new boolean[2];

    @Override
    public void lock() {
        int me = TwoThreadIds.myId();
        //int another = TwoThreadIds.not(me);
        flag[me] = true;
        //while (flag[another]) {};
    }


    @Override
    public void unlock() {
        flag[myId()] = false;
    }
}