package ru.sbt.edu.worker;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class ThreadPoolMatrixMultiplier {
    private final double[][] lhs, rhs, prod;
    private final int n;
    private final ExecutorService executors;

    public ThreadPoolMatrixMultiplier(double[][] lhs, double[][] rhs, ExecutorService service) {
        n = lhs.length;
        this.lhs = lhs;
        this.rhs = rhs;
        this.prod = new double[n][n];
        this.executors = service;
    }

    double[][] multiply() {
            Worker[][] worker = new Worker[n][n];
            ArrayList<Worker> workers = new ArrayList<>();
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    workers.add(new Worker(row, col));
                }
            }

            workers.stream()
                    //.parallel()
                    .map(executors::submit)
                    .forEach(future -> {
                        try {
                            future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    });

        return prod;
    }

    class Worker implements Runnable {
        private final int row, col;

        Worker(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            double dotProduct = 0.0;
            for (int i = 0; i < n; i++) {
                dotProduct += lhs[row][i] * rhs[i][col];
            }
            prod[row][col] = dotProduct;
        }
    }
}