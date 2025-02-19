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
        Map<Coord2D, Coord2D> values = new HashMap<>();
        for (int x = 0; x < powerGrid.length; x++) {
            for (int y = 0; y < powerGrid[x].length; y++) {
                values.put(new Coord2D(x,y), new Coord2D(0, 0));
            }
        }
        for (int currentSize = 0; currentSize < powerGrid.length; currentSize++) {
            if (currentSize >= location.z + 10) {
                continue;
            }
            for (int x = 0; x < powerGrid.length-1; x++) {
                if (x+currentSize >= powerGrid.length) {
                    continue;
                }
                for (int y = 0; y < powerGrid[x].length; y++) {
                    if (y+currentSize >= powerGrid[x].length-1) {
                        continue;
                    }
                    Coord2D sLocation = new Coord2D(x,y);
                    Coord2D tInfor = values.get(sLocation);
                    Integer sum = tInfor.x;
                    Integer newX = x + currentSize;
                    Integer newY = y + currentSize;
                    for (int i = 0; i < tInfor.y; i++) {
                        sum += powerGrid[newX][y+i];
                        sum += powerGrid[x+i][newY];
                    }
                    if (x + tInfor.y >= powerGrid.length || y + tInfor.y >= powerGrid.length) {
                        values.put(sLocation, new Coord2D(tInfor.x, currentSize+1));
                        continue;
                    }
                    sum += powerGrid[x+tInfor.y][y+tInfor.y];
                    values.put(new Coord2D(x,y), new Coord2D(sum, currentSize+1));
                    if (maxPower < sum) {
                        maxPower = sum;
                        location = new Coord3D(x+1,y+1,currentSize+1);
                    }
                }
            }
        }
        return location;
    }


}
