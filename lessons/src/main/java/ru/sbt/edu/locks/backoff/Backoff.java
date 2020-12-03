package ru.sbt.edu.locks.backoff;

import java.util.Random;

public class Backoff {
    private final int minDelay, maxDelay;
    private int limit; //why not volatile? ask students
    private final Random random;

    public Backoff(int min, int max) {
        minDelay = min; maxDelay = max;
        limit = minDelay;
        random = new Random();
    }

    public void backoff() {
        int delay = random.nextInt(limit);
        limit = Math.min(maxDelay, 2 * limit);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //this should be handled properly in  practice
            e.printStackTrace();
        }
    }
}