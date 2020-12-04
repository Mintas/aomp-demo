package ru.sbt.edu.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadID {
    private static final AtomicInteger identifier = new AtomicInteger();
    private static final ThreadLocal<Integer> me = ThreadLocal.withInitial(identifier::getAndIncrement);

    public static int get() {
        return me.get();
    }
}
