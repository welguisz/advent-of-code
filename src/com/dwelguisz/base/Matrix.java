package com.dwelguisz.base;

import java.util.HashSet;
import java.util.Set;

public class Matrix {
    public static Integer[][] ROTATION_MATRIX_2D_0;
    public static Integer[][] ROTATION_MATRIX_2D_90;
    public static Integer[][] ROTATION_MATRIX_2D_180;
    public static Integer[][] ROTATION_MATRIX_2D_270;
    public static Integer[][] ROTATION_MATRIX_3D_ROLL_0;
    public static Integer[][] ROTATION_MATRIX_3D_ROLL_90;
    public static Integer[][] ROTATION_MATRIX_3D_ROLL_180;
    public static Integer[][] ROTATION_MATRIX_3D_ROLL_270;
    public static Integer[][] ROTATION_MATRIX_3D_PITCH_0;
    public static Integer[][] ROTATION_MATRIX_3D_PITCH_90;
    public static Integer[][] ROTATION_MATRIX_3D_PITCH_180;
    public static Integer[][] ROTATION_MATRIX_3D_PITCH_270;
    public static Integer[][] ROTATION_MATRIX_3D_YAW_0;
    public static Integer[][] ROTATION_MATRIX_3D_YAW_90;
    public static Integer[][] ROTATION_MATRIX_3D_YAW_180;
    public static Integer[][] ROTATION_MATRIX_3D_YAW_270;
    public static Set<Integer[][]> ROTATION_2D;
    public static Set<Integer[][]> ROTATION_3D;


    static {
        ROTATION_MATRIX_2D_0 = new Integer[][]{{1, 0}, {0, 1}};
        ROTATION_MATRIX_2D_90 = new Integer[][]{{0,1},{-1,0}};
        ROTATION_MATRIX_2D_180 = new Integer[][]{{-1,0},{0,-1}};
        ROTATION_MATRIX_2D_270 = new Integer[][]{{0,-1},{1,0}};
        ROTATION_MATRIX_3D_ROLL_0 = new Integer[][]{{1,0,0},{0,1,0},{0,0,1}};
        ROTATION_MATRIX_3D_ROLL_90 = new Integer[][]{{1,0,0},{0,0,-1},{0,1,0}};
        ROTATION_MATRIX_3D_ROLL_180 = new Integer[][]{{1,0,0},{0,-1,0},{0,0,-1}};
        ROTATION_MATRIX_3D_ROLL_270 = new Integer[][]{{1,0,0},{0,0,1},{0,-1,0}};
        ROTATION_MATRIX_3D_PITCH_0 = new Integer[][]{{1,0,0},{0,1,0},{0,0,1}};
        ROTATION_MATRIX_3D_PITCH_90 = new Integer[][]{{0,0,1},{0,1,0},{-1,0,0}};
        ROTATION_MATRIX_3D_PITCH_180 = new Integer[][]{{-1,0,0},{0,1,0},{0,0,-1}};
        ROTATION_MATRIX_3D_PITCH_270 = new Integer[][]{{0,0,-1},{0,1,0},{1,0,0}};
        ROTATION_MATRIX_3D_YAW_0 = new Integer[][]{{1,0,0},{0,1,0},{0,0,1}};
        ROTATION_MATRIX_3D_YAW_90 = new Integer[][]{{0,-1,0},{1,0,0},{0,0,1}};
        ROTATION_MATRIX_3D_YAW_180 = new Integer[][]{{-1,0,0},{0,-1,0},{1,0,0}};
        ROTATION_MATRIX_3D_YAW_270 = new Integer[][]{{0,1,0},{-1,0,0},{0,0,1}};
        ROTATION_2D = Set.of(ROTATION_MATRIX_2D_0, ROTATION_MATRIX_2D_90, ROTATION_MATRIX_2D_180, ROTATION_MATRIX_2D_270);
        Set<Integer[][]> rollSet = Set.of(ROTATION_MATRIX_3D_ROLL_0, ROTATION_MATRIX_3D_ROLL_90, ROTATION_MATRIX_3D_ROLL_180, ROTATION_MATRIX_3D_ROLL_270);
        Set<Integer[][]> pitchSet = Set.of(ROTATION_MATRIX_3D_PITCH_0, ROTATION_MATRIX_3D_PITCH_90, ROTATION_MATRIX_3D_PITCH_180, ROTATION_MATRIX_3D_PITCH_270);
        Set<Integer[][]> yawSet = Set.of(ROTATION_MATRIX_3D_YAW_0, ROTATION_MATRIX_3D_YAW_90, ROTATION_MATRIX_3D_YAW_180, ROTATION_MATRIX_3D_YAW_270);
        ROTATION_3D = new HashSet<>();
        for (Integer[][] roll : rollSet) {
            for(Integer[][] pitch : pitchSet) {
                for(Integer[][] yaw : yawSet) {
                    ROTATION_3D.add(matrixMultiplace(matrixMultiplace(roll,pitch),yaw));
                }
            }
        }
    }

    public static Integer[][] matrixMultiplace(Integer[][] matrixA, Integer[][] matrixB) {
        if (matrixA[0].length != matrixB.length) {
            return new Integer[][]{{0},{0}};
        }
        Integer row = matrixA.length;
        Integer col = matrixB[0].length;
        Integer[][] newMatrix = new Integer[row][col];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                newMatrix[i][j] = multiplyMatrixCell(matrixA, matrixB, i, j);
            }

        }
        return newMatrix;
    }

    public static Integer multiplyMatrixCell(Integer[][] matrixA, Integer[][] matrixB, Integer row, Integer col) {
        Integer sum = 0;
        for(int i = 0; i < matrixB.length; i++) {
            sum += matrixA[row][i] + matrixB[i][col];
        }
        return sum;
    }

    Double[][] matrix;
    public Matrix(Double [][] matrix) {
        this.matrix = matrix;
    }

    public void swapRow(int i, int j) {
        for (int k = 0; k < matrix[0].length; k++) {
            Double temp = matrix[i][k];
            matrix[i][k] = matrix[j][k];
            matrix[j][k] = temp;
        }
    }

    public Integer forwardElimination() {
        for (int k = 0; k < matrix.length; k++) {
            int iMax = k;
            Double vMax = matrix[iMax][k];
            for (int i = k + 1; i < matrix.length; i++) {
                if (Math.abs(matrix[iMax][k]) > vMax) {
                    iMax = i;
                    vMax = matrix[iMax][k];
                }
            }
            if (matrix[k][iMax] == 0.0) {
                return k; //Matrix is singular
            }
//            if (iMax != k) {
//                swapRow(iMax, k);
//            }
            double factor = matrix[k][k];
            for (int j = k; j < matrix[0].length; j++) {
                matrix[k][j] /= factor;
            }
            for (int i=k+1; i < matrix.length; i++) {
                double f = ((double) matrix[i][k])/(double)matrix[k][k];
                for (int j = k+1; j < matrix[0].length; j++) {
                    matrix[i][j] -= matrix[k][j] * f;
                }
                matrix[i][k] = 0.0;
            }
//            System.out.println(this.toString());
        }
        return -1;
    }

    public void backSub() {
        for (int i = matrix.length-1; i >= 0; i--) {
            for (int j = 0; j <i; j++) {
                double f = ((double) matrix[j][i])/(double)matrix[i][i];
                for (int k = j; k < matrix[0].length; k++) {
                    matrix[j][k] -= matrix[i][k] * f;
                }
            }
            for (int k = i + 1; k < matrix[0].length; k++) {
                matrix[i][k] = matrix[i][k]/matrix[i][i];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sb.append("" + matrix[i][j] + "   ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Double getValueAtRowColumn(int row, int column) {
        return matrix[row][column];
    }

}
