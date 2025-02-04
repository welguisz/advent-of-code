package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ChronalCharge extends AoCDay {
    public static Integer GRID_SERIAL_NUMBER = 7989;

    public Integer calculatePowerLevel(Integer x, Integer y, Integer serialNumber) {
        Integer rackID = x + 10;
        Integer powerLevel = rackID * y;
        powerLevel += serialNumber;
        powerLevel *= rackID;
        powerLevel %= 1000;
        powerLevel /= 100;
        return powerLevel - 5;
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,11,false,0);
        Integer serialNumber = Integer.parseInt(lines.get(0));
        Integer[][] powerGrid = new Integer[300][300];
        for (int i = 1; i <= 300; i++) {
            for(int j = 1; j <= 300; j++) {
                powerGrid[i-1][j-1] = calculatePowerLevel(i,j, serialNumber);
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(powerGrid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(powerGrid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer calculatePower(Integer[][] powerGrid, final Integer i, final Integer j, final Integer size) {
        return IntStream.range(i,i+size).map(x -> IntStream.range(j,j+size).map(y -> powerGrid[x][y]).sum()).sum();
    }

    public Coord2D solutionPart1(Integer[][] powerGrid) {
        Coord2D location = new Coord2D(-1,-1);
        Integer maxPower = Integer.MIN_VALUE;
        for (int i = 0; i < powerGrid.length-3; i++) {
            for (int j = 0; j < powerGrid[i].length-3; j++) {
                Integer value = calculatePower(powerGrid,i, j, 3);
                if (maxPower < value) {
                    maxPower = value;
                    location = new Coord2D(i+1,j+1);
                }
            }
        }
        return location;
    }

    public Coord3D solutionPart2(Integer[][] powerGrid) {
        Coord3D location = new Coord3D(-1,-1, -1);
        Integer maxPower = Integer.MIN_VALUE;
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
                        location = new Coord3D(i + 1, j + 1, h);
                    }
                }
            }
        }
        return location;
    }


}
