package ru.sbt.edu.worker;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;

public class MatrixAdditionTest {
    @Test
    public void compute() {
        Matrix lhs = new Matrix(5); //init as empty, simple example
        Matrix rhs = new Matrix(5);
        Matrix sum = new Matrix(5);

        MatrixAddition recursiveAddTask = new MatrixAddition(lhs, rhs, sum);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(recursiveAddTask); //go in !

        assertEquals(sum, sum); //some assertions
    }
}