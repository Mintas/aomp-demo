package ru.sbt.edu.worker;

import java.util.stream.IntStream;

public class Matrix {
    private final int dim;
    private final double[][] data;
    private final int rowDisplace, colDisplace;

    Matrix(int d) {
        dim = d;
        rowDisplace = colDisplace = 0;
        data = new double[d][d];
    }

    Matrix(double[][] matrix, int x, int y, int d) {
        data = matrix;
        rowDisplace = x;
        colDisplace = y;
        dim = d;
    }

    public double get(int row, int col) {
        return data[row + rowDisplace][col + colDisplace];
    }

    public void set(int row, int col, double value) {
        data[row + rowDisplace][col + colDisplace] = value;
    }

    public int getDim() {
        return dim;
    }

    public Matrix quarter(int i, int j) { //requires O(1) time
        int newDim = dim / 2;
        return new Matrix(data,
                rowDisplace + (i * newDim),
                colDisplace + (j * newDim),
                newDim);
    }

    public static void add(Matrix lhs, Matrix rhs, Matrix sum) {
        IntStream.range(0, lhs.dim)
                .forEach(i -> IntStream.range(0, lhs.dim)
                        .forEach(j -> sum.set(i, j, Double.sum(lhs.get(i,j), rhs.get(i, j)))));
    }

    public static void multiply(Matrix lhs, Matrix rhs, Matrix product) {
        //todo
    }
}
