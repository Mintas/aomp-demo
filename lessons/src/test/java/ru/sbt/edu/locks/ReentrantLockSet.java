package ru.sbt.edu.locks;

import ru.sbt.edu.set.ISet;

import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReentrantLockSet implements ISet<Integer> {
    private final HashSet<Integer> set = new HashSet<>();
    private final Lock lock  = new ReentrantLock();
    @Override
    public boolean add(Integer element) {
        return tryWithLock(set::add, element);

    }

    @Override
    public boolean remove(Integer element) {
        return tryWithLock(set::remove, element);
    }

    @Override
    public boolean contains(Integer element) {
        return tryWithLock(set::contains, element);
    }

    private boolean tryWithLock(Predicate<Integer> setFunction, Integer element) {
        lock.lock();
        try {
            return setFunction.test(element);
        } finally {
            lock.unlock();
        }
    }
}
