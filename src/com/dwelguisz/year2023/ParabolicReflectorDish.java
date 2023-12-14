package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParabolicReflectorDish extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 14, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(convertToCharGrid(lines));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(convertToCharGrid(lines));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    char[][] tilt(char[][] grid) {
        int maxR = grid.length;
        int maxC = grid[0].length;
        boolean frozen = false;
        while (!frozen) {
            frozen = true;
            for (int r = 0; r < maxR; r++) {
                for (int c = 0; c < maxC; c++) {
                    if (grid[r][c]=='O' && r > 0 && grid[r-1][c]=='.') {
                        grid[r][c] = '.';
                        grid[r-1][c] = 'O';
                        frozen = false;
                    }
                }
            }
        }
        return grid;
    }


    Long scoreGrid(char[][] grid) {
        int maxR = grid.length;
        int maxC = grid[0].length;
        Long score = 0L;
        for (int r = 0; r < maxR; r++) {
            for (int c = 0; c < maxC; c++) {
                if (grid[r][c] == 'O') {
                    score += maxR-r;
                }
            }
        }
        return score;
    }

    Long solutionPart1(char[][] grid) {
        grid = tilt(grid);
        return scoreGrid(grid);
    }

    Long hashGrid(char[][] grid) {
        Long hash = 0L;
        Map<Character,Integer> conversion = Map.of('.',0,'O',1,'#',2);
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                hash *= 3;
                hash += conversion.get(grid[r][c]);
            }
        }
        return hash;
    }
    Long solutionPart2(char[][] grid) {
        Long totalCycleNumber = 1_000_000_000L;
        Long currentCycle = 0L;
        Map<Long,Long> gridHistory = new HashMap<>();
        while (currentCycle < totalCycleNumber) {
            currentCycle += 1;
            for (int i = 0; i < 4; i++) {
                grid = tilt(grid);
                grid = rotateCharGridCounterClockwise(grid);
            }
            Long hash = hashGrid(grid);
            if (gridHistory.containsKey(hash)) {
                Long cycleLength = currentCycle - gridHistory.get(hash);
                Long cyclesToSkip = (totalCycleNumber - currentCycle) / cycleLength;
                currentCycle += cyclesToSkip * cycleLength;
            }
            gridHistory.put(hash,currentCycle);
        }
        return scoreGrid(grid);
    }

}
