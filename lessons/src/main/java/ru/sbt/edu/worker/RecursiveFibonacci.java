package ru.sbt.edu.worker;

import java.util.concurrent.RecursiveTask;

public class RecursiveFibonacci extends RecursiveTask<Integer> {
    private final int arg;

    public RecursiveFibonacci(int n) {
        arg = n;
    }

    protected Integer compute() {
        if (arg > 1) {
            RecursiveFibonacci rightTask = new RecursiveFibonacci(arg - 1);
            rightTask.fork();

            RecursiveFibonacci leftTask = new RecursiveFibonacci(arg - 2);
            return rightTask.join() + leftTask.compute();
        } else {
            return arg;
        }
    }
}
