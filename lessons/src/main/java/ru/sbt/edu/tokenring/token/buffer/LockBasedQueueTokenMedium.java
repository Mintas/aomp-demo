package ru.sbt.edu.tokenring.token.buffer;

import ru.sbt.edu.tokenring.token.Token;
import ru.sbt.edu.tokenring.token.TokenMedium;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBasedQueueTokenMedium implements BufferedTokenMedium {
    private final Queue<Token> queue;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    public LockBasedQueueTokenMedium(Queue<Token> queue) {
        this.queue = queue;
    }

    @Override
    public void push(Token token) throws InterruptedException {
        lock.lock();
        try {
            boolean added = queue.add(token);//this can throw IllegalStateException if queue is full, what to do?
            notEmpty.signal(); //why not signalAll ?!
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Token poll() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
}
