package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GearRatios extends AoCDay {

    Map<Coord2D, List<Integer>> gears;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 3, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private boolean checkNeighbors(
            final Integer startRow,
            final Integer startCol,
            final Integer endRow,
            final Integer endCol,
            final Integer number,
            final char[][] grid
    ) {
        for (Integer row = startRow ; row <= endRow; row++) {
            for (Integer col = startCol; col <= endCol; col++) {
                if (row >= 0 && row < grid.length && col >= 0 && col < grid[row].length) {
                    char gridValue = grid[row][col];
                    if ((!Character.isDigit(gridValue)) && (gridValue != '.')) {
                        if (gridValue == '*') {
                            gears.computeIfAbsent(new Coord2D(row, col), k -> new ArrayList<>()).add(number);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private Long solutionPart1(List<String> lines) {
        Long total = 0L;
        char[][] grid = convertToCharGrid(lines);
        gears = new HashMap<>();
        int rowNum = 0;
        for (String line : lines) {
            //Find any number and combine into one group
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(line);
            //Go through each match and do some tests
            while (matcher.find()) {
                //Get the starting location and ending location of the number, so it will check all locations
                // Example:
                //  ....1234....
                // Will check the following box:
                //   (rowNum-1,startOfMatch-1) ---> (rowNum-1,endOfMatch) //endOfMatch is the location of the next char
                //       V                                 V
                //   (rowNum+1,startOfMatch-1) -->  (rowNum+1,endOfMatch)
                int startNum = matcher.start();
                int endNum = matcher.end();
                Integer num = Integer.parseInt(matcher.group());
                //Do the actual checking of neighbors
                if (checkNeighbors(rowNum-1, startNum-1, rowNum+1, endNum, num, grid)) {
                    total += num;
                }
            }
            rowNum++;
        }
        return total;
    }

    public Long solutionPart2() {
        return gears.values().stream()
                .filter(v -> v.size()==2)
                .mapToLong(v -> v.get(0) * v.get(1))
                .sum();
    }

}
