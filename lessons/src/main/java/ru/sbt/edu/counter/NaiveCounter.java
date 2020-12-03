package ru.sbt.edu.counter;

public class NaiveCounter implements Counter {
    private int count = 0;

    @Override
    public void increment() {
        count++;
    }

    @Override
    public int value() {
        return count;
    }
}
