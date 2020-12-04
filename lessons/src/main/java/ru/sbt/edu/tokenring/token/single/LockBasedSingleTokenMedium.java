package ru.sbt.edu.tokenring.token.single;

import ru.sbt.edu.tokenring.token.Token;
import ru.sbt.edu.tokenring.token.single.SingleTokenMedium;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBasedSingleTokenMedium implements SingleTokenMedium {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Token token;

    @Override
    public void push(Token token) throws InterruptedException {
        lock.lock();
        try {
            while (this.token != null) {
                condition.await();
            }

            this.token = token;
            condition.signalAll();

        } finally {
            lock.unlock();
        }
    }

    @Override
    public Token poll() throws InterruptedException {
        lock.lock();
        try {
            while (this.token == null) {
                condition.await(); //problems?
            }

            Token taken = token;
            token = null;
            condition.signalAll();
            return taken;

        } finally {
            lock.unlock();
        }
    }
}
