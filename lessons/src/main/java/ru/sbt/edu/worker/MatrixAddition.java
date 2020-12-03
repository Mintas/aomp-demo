package ru.sbt.edu.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class MatrixAddition extends RecursiveAction {
    static final int THRESHOLD = 4;
    Matrix lhs, rhs, sum;

    public MatrixAddition(Matrix lhs, Matrix rhs, Matrix sum) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.sum = sum;
    }

    public void compute() {
        if (getTaskSize() <= THRESHOLD) {
            recursionTail();
        } else {
            recursiveStep();
        }
    }

    private void recursionTail() {
        Matrix.add(lhs, rhs, sum);
    }

    private void recursiveStep() {
        List<MatrixAddition> tasks = new ArrayList<>(4);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) { //2^2
                tasks.add(
                        new MatrixAddition(
                                lhs.quarter(i, j),
                                rhs.quarter(i, j),
                                sum.quarter(i, j)));
            }
        }
        tasks.forEach(ForkJoinTask::fork);
        tasks.forEach(ForkJoinTask::join);
    }

    private int getTaskSize() {
        return lhs.getDim();
    }
}