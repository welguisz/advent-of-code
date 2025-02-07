package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeatingSystem extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,11,false,0);
        String[][] grid = convertToGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Long solutionPart1(String[][] grid) {
        Boolean runAgain = true;
        String[][] currentGrid = grid;
        while(runAgain) {
            Pair<Boolean, String[][]> result = oneStep(currentGrid, false, 4);
            runAgain = !result.getLeft();
            currentGrid = result.getRight();
        }
        return countOccupancy(currentGrid);
    }

    private Long solutionPart2(String[][] grid) {
        Boolean runAgain = true;
        String[][] currentGrid = grid;
        while(runAgain) {
            Pair<Boolean, String[][]> result = oneStep(currentGrid, true, 5);
            runAgain = !result.getLeft();
            currentGrid = result.getRight();
        }
        return countOccupancy(currentGrid);
    }

    private Long countOccupancy(String[][] grid) {
        Long count = 0L;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if(grid[i][j].equals("#")) {
                    count++;
                }
            }
        }
        return count;
    }

    private Pair<Boolean,String[][]> oneStep(String[][] grid, Boolean part2, Integer tolerance) {
        Boolean stable = true;
        String[][] newGrid = new String[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (okayToSitHere(grid, i, j, part2)) {
                    stable = false;
                    newGrid[i][j] = "#";
                } else if (needToStandUp(grid, i, j, part2, tolerance)) {
                    stable = false;
                    newGrid[i][j] = "L";
                } else {
                    newGrid[i][j] = grid[i][j];
                }
            }
        }
        return Pair.of(stable, newGrid);
    }

    private boolean okayToSitHere(String[][] grid, int i, int j, Boolean part2) {
        if (grid[i][j].equals(".")) {
            return false;
        } else if (grid[i][j].equals("#")) {
            return false;
        }
        List<String> aroundMe = part2 ? visibleGrid(grid, i, j) : aroundGrid(grid, i, j);
        for (String nextToMe : aroundMe) {
            if (nextToMe.equals("#")) {
                return false;
            }
        }
        return true;
    }

    private boolean needToStandUp(String[][] grid, int i, int j, Boolean part2, int tolerance) {
        if (grid[i][j].equals(".")) {
            return false;
        } else if (grid[i][j].equals("L")) {
            return false;
        }
        List<String> aroundMe = part2 ? visibleGrid(grid, i, j) : aroundGrid(grid, i, j);
        int count = 0;
        for (String nextToMe : aroundMe) {
            if (nextToMe.equals("#")) {
                count++;
            }
        }
        return (count >= tolerance);
    }

    private List<String> aroundGrid(String[][] grid, int i, int j) {
        List<Pair<Integer, Integer>> offsets = List.of(Pair.of(-1,-1), Pair.of(-1,0),
                Pair.of(-1,1), Pair.of(0,-1), Pair.of(0,1), Pair.of(1,-1),
                Pair.of(1,0), Pair.of(1,1));
        List<String> values = new ArrayList<>();
        for (Pair<Integer, Integer> offset : offsets) {
            int adjacentRow = i + offset.getLeft();
            int adjacentCol = j + offset.getRight();
            if (adjacentRow < 0 || adjacentRow >= grid.length || adjacentCol < 0 || adjacentCol >= grid[adjacentRow].length) {
                continue;
            }
            values.add(grid[adjacentRow][adjacentCol]);
        }
        return values;
    }

    private List<String> visibleGrid(String[][] grid, int i, int j) {
        List<Pair<Integer, Integer>> offsets = List.of(Pair.of(-1,-1), Pair.of(-1,0),
                Pair.of(-1,1), Pair.of(0,-1), Pair.of(0,1), Pair.of(1,-1),
                Pair.of(1,0), Pair.of(1,1));
        List<String> values = new ArrayList<>();
        for (Pair<Integer, Integer> offset : offsets) {
            int adjacentRow = i;
            int adjacentCol = j;
            boolean continueOnward = true;
            while (continueOnward) {
                adjacentRow += offset.getLeft();
                adjacentCol += offset.getRight();
                if (adjacentRow < 0 || adjacentRow >= grid.length || adjacentCol < 0 || adjacentCol >= grid[adjacentRow].length) {
                    continueOnward = false;
                } else if (!grid[adjacentRow][adjacentCol].equals(".")) {
                    continueOnward = false;
                    values.add(grid[adjacentRow][adjacentCol]);
                }
            }
        }
        return values;
    }


}
