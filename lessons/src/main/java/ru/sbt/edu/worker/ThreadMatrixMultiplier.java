package ru.sbt.edu.worker;

public class ThreadMatrixMultiplier {
    private final double[][] lhs, rhs, prod;
    private final int n;

    public ThreadMatrixMultiplier(double[][] lhs, double[][] rhs) {
        n = lhs.length;
        this.lhs = lhs;
        this.rhs = rhs;
        this.prod = new double[n][n];
    }

    double[][] multiply() {
        Worker[][] worker = new Worker[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                worker[row][col] = new Worker(row, col);
            }
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                worker[row][col].start();
            }
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                try {
                    worker[row][col].join();
                } catch (InterruptedException ex) {
                    //
                }
            }
        }

        return prod;
    }

    class Worker extends Thread {
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