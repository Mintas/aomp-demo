package ru.sbt.edu.utils;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.lang.Thread.currentThread;

public final class TwoThreadIds {
    public static int myId() {
        return parseInt(currentThread().getName());
    }

    public static int not(int me) {
        return me == 0 ? 1 : 0;
    }

    public static Thread thread0(Runnable runnable) {
        return initThreadWithId(runnable, 0);
    }

    public static Thread thread1(Runnable runnable) {
        return initThreadWithId(runnable, 1);
    }

    private static Thread initThreadWithId(Runnable runnable, int id) {
        return new Thread(runnable, valueOf(id));
    }
}
