package ru.sbt.edu.trap;

import org.junit.Test;
import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.locks.tas.TaTasLock;
import ru.sbt.edu.locks.tas.TasLock;
import ru.sbt.edu.locks.theory.FirstLock;
import ru.sbt.edu.locks.theory.PetersonLock;
import ru.sbt.edu.locks.theory.SecondLock;
import ru.sbt.edu.utils.TwoThreadIds;

public class OuterSpaceTest {
    @Test
    public void firstSpaceWalk() {
        SLock lock = new FirstLock();
        OuterSpace space = new OuterSpace(lock);

        testWalks(space, 1000);
    }

    @Test
    public void secondSpaceWalk() {
        SLock lock = new SecondLock();
        OuterSpace space = new OuterSpace(lock);

        testWalks(space, 10);
    }

    @Test
    public void petersonSpaceWalk() {
        SLock lock = new TasLock();
        OuterSpace space = new OuterSpace(lock);

        testWalks(space, 10_000);
    }

    private void testWalks(OuterSpace outerSpace, int iters) {
        Runnable increment = () -> {
            System.out.println(TwoThreadIds.myId());
            for (int i = 0; i < iters; i++) {
                outerSpace.spaceWalk();
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
    }
}