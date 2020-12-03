package ru.sbt.edu.hw2;

import ru.sbt.edu.locks.SLock;

public class SimpleReadWriteLock {
    private int readers = 0;
    private boolean writer = false;
    private final ReadLock readLock = new ReadLock();
    private final WriteLock writeLock = new WriteLock();

    public SLock readLock() {
        return readLock;
    }

    public SLock writeLock() {
        return writeLock;
    }

    private void await() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class ReadLock implements SLock {
        @Override
        public void lock() {
          synchronized (SimpleReadWriteLock.this){
                while (writer) await();
                readers++;
          }
        }

        @Override
        public void unlock() {
            synchronized (SimpleReadWriteLock.this){
                readers--;
                if (readers == 0) SimpleReadWriteLock.this.notifyAll();
            }
        }
    }

    class WriteLock implements SLock {
        @Override
        public void lock() {
            synchronized (SimpleReadWriteLock.this){
                while (readers > 0 || writer) await();
                writer = true;
            }
        }

        @Override
        public void unlock() {
            synchronized (SimpleReadWriteLock.this){
                writer = false;
                SimpleReadWriteLock.this.notifyAll();
            }
        }
    }
}
