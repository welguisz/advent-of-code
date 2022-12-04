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
}
