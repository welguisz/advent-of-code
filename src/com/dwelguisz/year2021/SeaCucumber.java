package com.dwelguisz.year2021;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class SeaCucumber {
    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day25/input.txt");
        String[][] grid = createGrid(lines);
        Integer part1 = solutionPart1(grid);
        System.out.println(String.format("Solution Part1: %d",part1));
    }

    public static Integer solutionPart1(String[][] grid) {
        Integer count = 0;
        boolean atLeastOneMoved = true;
        while (atLeastOneMoved) {
            Pair<Boolean, String[][]> result = oneStep(grid);
            atLeastOneMoved = result.getLeft();
            grid = result.getRight();
            count++;
        }
        return count;
    }

    public static Pair<Boolean, String[][]> oneStep(String[][] grid)  {
        Boolean atLeastOneMoved = false;
        String[][] newGrid = copy2DArray(grid);
        // move to the right
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!grid[i][j].equals(">")) {
                    continue;
                }
                Integer target = (j + 1) % grid[0].length;
                if (!grid[i][target].equals(".")) {
                    continue;
                }
                newGrid[i][j] = ".";
                newGrid[i][target] = ">";
                atLeastOneMoved = true;
            }
        }
        grid = copy2DArray(newGrid);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!grid[i][j].equals("v")) {
                    continue;
                }
                Integer target = (i + 1) % grid.length;
                if (!grid[target][j].equals(".")) {
                    continue;
                }
                newGrid[i][j] = ".";
                newGrid[target][j] = "v";
                atLeastOneMoved = true;
            }
        }
        return Pair.of(atLeastOneMoved, newGrid);
    }

    public static String[][] copy2DArray(String [][] source) {
        String[][] newGrid = Arrays.stream(source)
                .map((String[] row) -> row.clone())
                .toArray((int length) -> new String[length][]);
        return newGrid;
    }

    public static String[][] createGrid(List<String> lines) {
        String[][] grid = new String[lines.size()][lines.get(0).length()];
        Integer i = 0;
        for(String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.substring(j,j+1);
            }
            i++;
        }
        return grid;
    }
}
