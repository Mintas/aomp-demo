package ru.sbt.edu.worker;

import java.util.concurrent.*;

public class Plan {
    public static void main(String[] args) {
        ThreadMatrixMultiplier multiplier;
        Thread thread;
        //ThreadMatrixMultiplierTest test;

        //goto : slides
        ExecutorService service;
        Runnable runnable;
        Callable<Object> callable;
        Future future;
        //ThreadMatrixMultiplierTest test;
        //rewrite using singleThreadExecutor?

        //goto : slides
        MatrixAddition addition;
        ForkJoinPool pool;
        ForkJoinTask<Object> task;
        RecursiveAction action; RecursiveTask<?> recTask;
    }
}
