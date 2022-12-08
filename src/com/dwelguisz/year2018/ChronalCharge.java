package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ChronalCharge extends AoCDay {
    public static Integer GRID_SERIAL_NUMBER = 7989;

    public Integer calculatePowerLevel(Integer x, Integer y) {
        Integer rackID = x + 10;
        Integer powerLevel = rackID * y;
        powerLevel += GRID_SERIAL_NUMBER;
        powerLevel *= rackID;
        powerLevel %= 1000;
        powerLevel /= 100;
        return powerLevel - 5;
    }

    public void solve() {
        Integer[][] powerGrid = new Integer[300][300];
        for (int i = 1; i <= 300; i++) {
            for(int j = 1; j <= 300; j++) {
                powerGrid[i-1][j-1] = calculatePowerLevel(i,j);
            }
        }
        Pair<Integer, Integer> part1 = solutionPart1(powerGrid);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        Pair<Pair<Integer,Integer>, Integer> part2 = solutionPart2(powerGrid);
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

    public Integer calculatePower(Integer[][] powerGrid, final Integer i, final Integer j, final Integer size) {
        return IntStream.range(i,i+size).map(x -> IntStream.range(j,j+size).map(y -> powerGrid[x][y]).sum()).sum();
    }

    public Pair<Integer, Integer> solutionPart1(Integer[][] powerGrid) {
        Pair<Integer, Integer> location = Pair.of(-1,-1);
        Integer maxPower = Integer.MIN_VALUE;
        for (int i = 0; i < powerGrid.length-3; i++) {
            for (int j = 0; j < powerGrid[i].length-3; j++) {
                Integer value = calculatePower(powerGrid,i, j, 3);
                if (maxPower < value) {
                    maxPower = value;
                    location = Pair.of(i+1,j+1);
                }
            }
        }
        return location;
    }

    public Pair<Pair<Integer, Integer>, Integer> solutionPart2(Integer[][] powerGrid) {
        Pair<Integer, Integer> location = Pair.of(-1,-1);
        Integer maxPower = Integer.MIN_VALUE;
        Integer size = 0;
        for (int h = 300; h > 0; h--) {
            int maxPossibleValue = h * h * 4;
            if (maxPossibleValue < maxPower) {
                break;
            }
            for (int i = 0; i < powerGrid.length - h; i++) {
                for (int j = 0; j < powerGrid[i].length - h; j++) {
                    Integer value = calculatePower(powerGrid, i, j, h);
                    if (maxPower < value) {
                        maxPower = value;
                        location = Pair.of(i + 1, j + 1);
                        size = h;
                    }
                }
            }
        }
        return Pair.of(location,size);
    }


}
