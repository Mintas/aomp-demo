package ru.sbt.edu.locks.theory;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.ThreadID;

public class ZeroLock implements SLock {
    private final boolean flag[];

    public ZeroLock() {
        this.flag = new boolean[2];
    }

    @Override
    public void lock() {
        int i = ThreadID.get();
        flag[i] = true;
    }

    @Override
    public void unlock() {
        this.flag[ThreadID.get()] = false;
    }
}
