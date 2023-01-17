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
            return leftTask.join() + rightTask.join();//A
        } else {
            return arg;
        }
    }
}

/**
 * 1) system ? => N1 , N2, N3, N4 -> 8cores, Intel HT -> ~8 ~16 || ~ 17 18
 * 50 ? -> tpt/latency: less performance more scheduler
 * 2) WarmUp? JIT -> stats + optimize/deoptimize -> server -> send/receive ~10k
 * 3) Spawn too much packets ? ... 100B -> 100MB -> GC STW/P
 * 4) run once -> say all time truth _-_-_-_
 * 5) Latency 1 msg through ring / ring length ? linear? 16 max, 40 or 50 ><
 * 6) k= 2,5; y=kx ??? why?
 * 7) 1msg / ring
 * 8) atomic/exchanger || blocking/non-blocking/synchronousQ
 * 9) 8node, 1msg..5msg..7msg || k-bounded? max = k*n, n or k msg  __-----__
 */