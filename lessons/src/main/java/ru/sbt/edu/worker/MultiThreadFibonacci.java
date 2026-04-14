package ru.sbt.edu.worker;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadFibonacci {
    class FibTask implements Callable<Integer> {
        static ExecutorService exec = Executors.newCachedThreadPool();
        int arg;

        public FibTask(int n) {
            arg = n;
        }

        @SneakyThrows
        public Integer call() {
            if (arg > 2) {
                Future<Integer> left = exec.submit(new FibTask(arg - 1));
                Future<Integer> right = exec.submit(new FibTask(arg - 2));
                return left.get() + right.get();
            } else {
                return 1;
            }
        }
    }

}
