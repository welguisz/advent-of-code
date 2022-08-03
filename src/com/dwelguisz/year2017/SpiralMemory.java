package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class SpiralMemory extends AoCDay {
    public void solve(){
        double part1 = solutionPart1(347991);
        Integer part2 = solutionPart2(347991);
        System.out.println(String.format("Part 1 Answer: %f",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public double solutionPart1(Integer value) {
        double spiral = Math.sqrt((double)value/4) - 0.5;
        double spiralNumber = Math.ceil(spiral);
        double[] centers;
        centers = new double[]{0.125, 0.375, 0.625, 0.875};
        double distI = 0.0;
        boolean isNegFromCenter = false;
        double tmpSpiral = spiral % 1;
        for(double center : centers) {
            distI = Math.abs(tmpSpiral - center);
            if (distI <= 0.125) {
                if (value < center) {
                    isNegFromCenter = true;
                }
                break;
            }
        }
        double offset = 8 * spiralNumber * distI;
        if (isNegFromCenter) {
            offset = Math.ceil(offset);
        }
        return Math.floor(spiralNumber + offset);
    }

    public Integer solutionPart2(Integer value) {
        int x = 0;
        int y = 0;
        int current = 1;
        Map<Pair<Integer, Integer>,Integer> matrix = new HashMap<>();
        matrix.put(Pair.of(x,y),current);
        while (current < value) {
            if (Math.abs(x) == Math.abs(y)) {
                if ((x > 0) && (y > 0)) {
                    // Go Left
                    x -= 1;
                    matrix = checkAround(x, y, matrix);
                }
                else if ((x < 0) && (y > 0)) {
                    //Go Down
                    y -= 1;
                    matrix = checkAround(x,y,matrix);
                }
                else if (((x <= 0) && (y <= 0)) || ((x>0) && (y <0))) {
                    //Go Right
                    x += 1;
                    matrix = checkAround(x,y,matrix);
                }
            } else if ((x>0) && (Math.abs(y) < Math.abs(x))) {
                y += 1;
                matrix = checkAround(x,y,matrix);
            } else if ((y>0) && (Math.abs(x) < Math.abs(y))) {
                x -= 1;
                matrix = checkAround(x,y,matrix);
            } else if ((x<0) && (Math.abs(y) < Math.abs(x))) {
                y -= 1;
                matrix = checkAround(x,y,matrix);
            } else if ((y<0) && (Math.abs(x) < Math.abs(y))) {
                x += 1;
                matrix = checkAround(x,y,matrix);
            }
            current = matrix.get(Pair.of(x,y));
        }
        return current;
    }

    public Map<Pair<Integer, Integer>,Integer> checkAround(int x, int y, Map<Pair<Integer, Integer>,Integer> matrix) {
        Integer value = 0;
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y -1; j < y + 2; j++) {
                value += matrix.getOrDefault(Pair.of(i,j),0);
            }
        }
        matrix.put(Pair.of(x,y),value);
        return matrix;
    }

}
