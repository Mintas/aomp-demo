package ru.sbt.edu.trap;

import ru.sbt.edu.locks.SLock;
import ru.sbt.edu.utils.TwoThreadIds;

import java.util.EmptyStackException;
import java.util.Stack;

import static java.lang.String.format;
import static ru.sbt.edu.utils.TwoThreadIds.myId;

public class OuterSpace {
    private final Stack<Object> spaceSuit;
    private final SLock lock;

    public OuterSpace(SLock lock) {
        Stack<Object> spaceSuit = new Stack<>();
        spaceSuit.push(new Object());
        this.spaceSuit = spaceSuit;
        this.lock = lock;
    }

    public void spaceWalk() {
        lock.lock();
        try {
            Object suit = spaceSuit.pop();
            System.out.println(format(
                    "%s : I am wearing a suit! Having really nice spacewalk in spaceSuit: %s",
                    myId(), suit.hashCode()));
            spaceSuit.push(suit);
        } catch (EmptyStackException fatality) {
            System.out.println(format(
                    "%s : Oh crap, DONT GO TO OUTER SPACE WITHOUT SPACESUIT! Am dead! REMEMBER ME!!!",
                    myId()));
        } finally {
            lock.unlock();
        }
    }
}
