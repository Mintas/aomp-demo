package ru.sbt.edu.worker;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class ThreadMatrixMultiplierTest {
    double[][] lhs = new double[][]{{1., 2., 3.}, {4., 5., 6.}, {7., 8., 9.}};
    double[][] rhs = new double[][]{{1., 0, 0}, {0, 1, 0}, {0, 0, 1}};

    @Test
    public void multiply() {
        ThreadMatrixMultiplier multiplier = new ThreadMatrixMultiplier(lhs, rhs);
        double[][] product = multiplier.multiply();
        assertEquals(lhs, product);
    }

    @Test
    public void multiply50() {
        doMultiply(50);
    }

    @Test
    public void multiply500() {
        doMultiply(500);
    }

    private void doMultiply(int size) {
        ThreadMatrixMultiplier multiplier = new ThreadMatrixMultiplier(
                new double[size][size],
                new double[size][size]);

        double[][] product = multiplier.multiply();
    }


    @Test
    public void multiplyExecutors() {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        Callable<double[][]> multiplyTask = () ->
                new ThreadMatrixMultiplier(lhs, rhs).multiply();

        Future<double[][]> futureResult = threadPool.submit(multiplyTask);
        try {
            double[][] product = futureResult.get();
            assertEquals(lhs, product);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}