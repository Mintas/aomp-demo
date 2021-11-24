package ru.sbt.edu.worker;

import java.util.concurrent.RecursiveTask;

public class RecursiveFibonacci extends RecursiveTask<Integer> {
    private final int arg;

    public RecursiveFibonacci(int n) {
        arg = n;
    }

    protected Integer compute() { //A from FJP - aka callee thread is able to go down
        // to the tail of recursion
        if (arg > 1) {
            RecursiveFibonacci rightTask = new RecursiveFibonacci(arg - 1);
            rightTask.fork(); //add to queue; free FJP threads

            RecursiveFibonacci leftTask = new RecursiveFibonacci(arg - 2);
            //A
            return leftTask.compute() + rightTask.join();//A
        } else {
            return arg;
        }
    }
}