package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;


public class SeaCucumber extends AoCDay {

    String[][] grid;
    Boolean atLeastOneMoved;

    public SeaCucumber() {
        super();
        grid = new String[1][1];
        atLeastOneMoved = true;
    }

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day25/input.txt");
        grid = createGrid(lines);
        Integer part1 = solutionPart1();
        System.out.println(String.format("Solution Part1: %d",part1));
    }

    public Integer solutionPart1() {
        Integer count = 0;
        while (atLeastOneMoved) {
            oneStep();
            count++;
        }
        return count;
    }

    public void oneStep()  {
        String[][] newGrid = copy2DArray(grid);
        atLeastOneMoved = false;
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
        grid = copy2DArray(newGrid);
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
