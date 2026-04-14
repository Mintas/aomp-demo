package ru.sbt.edu.locks;

import org.junit.Test;
import ru.sbt.edu.counter.Counter;
import ru.sbt.edu.counter.LockCounter;
import ru.sbt.edu.counter.NaiveCounter;
import ru.sbt.edu.locks.theory.*;
import ru.sbt.edu.utils.TwoThreadIds;

import static junit.framework.TestCase.assertEquals;

public class PetersonLockTest {
    @Test
    public void testWithPetersonLock()  {
        //PetersonLock lock = new PetersonLock();
        //SLock lock = new FirstLock();
        SLock lock = new SecondLock();
        Counter counter = new LockCounter(lock);

        testCounter(counter, 1);
    }

    @Test
    public void testNaiveCounter()  {
        Counter counter = new NaiveCounter();

        testCounter(counter, 1000);
    }

    private void testCounter(Counter counter, int iters) {
        Runnable increment = () -> {
            System.out.println(TwoThreadIds.myId());
            for (int i = 0; i < iters; i++) {
                counter.increment();
            }
        };

        Thread t0 = TwoThreadIds.thread0(increment);
        Thread t1 = TwoThreadIds.thread1(increment);
        t0.start();
        t1.start();

        try {
            t0.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int count = counter.value();
        System.out.println(count);
        assertEquals(iters * 2, count);
    }
}