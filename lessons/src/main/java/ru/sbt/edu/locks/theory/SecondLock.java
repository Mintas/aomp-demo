package ru.sbt.edu.locks.theory;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.TwoThreadIds;

public class SecondLock implements SLock {
    private int porter;

    @Override
    public void lock() {
        int me = TwoThreadIds.myId();
        porter = me;
        while (porter == me) {}
    }


    @Override
    public void unlock() {
        //nothing to do here
    }

}
