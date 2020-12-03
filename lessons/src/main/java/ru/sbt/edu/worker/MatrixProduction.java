package ru.sbt.edu.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class MatrixProduction extends RecursiveAction {
    static final int THRESHOLD = 4;
    private final Matrix lhs, rhs, product;

    public MatrixProduction(Matrix lhs, Matrix rhs, Matrix product) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.product = product;
    }

    public void compute() {
        int n = lhs.getDim();
        if (n <= THRESHOLD) {
            Matrix.multiply(lhs, rhs, product);
        } else {
            Matrix[] term = splitAndRecursiveMultiply(n);
            computeTermsSum(term);
        }
    }

    private Matrix[] splitAndRecursiveMultiply(int n) {
        List<MatrixProduction> tasks = new ArrayList<>(8);
        Matrix[] term = new Matrix[]{new Matrix(n), new Matrix(n)};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) { //2^3
                    tasks.add(
                            new MatrixProduction(
                                    lhs.quarter(j, i),
                                    rhs.quarter(i, k),
                                    term[i].quarter(j, k)
                            )
                    );
                }
            }
        }
        tasks.forEach(ForkJoinTask::fork);
        tasks.forEach(ForkJoinTask::join);
        return term;
    }

    private void computeTermsSum(Matrix[] term) {
        //requires
        new MatrixAddition(term[0], term[1], product).compute();
    }
}