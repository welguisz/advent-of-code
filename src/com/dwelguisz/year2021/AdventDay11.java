package com.dwelguisz.year2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay11 {

    static Integer MAX_ROW = 10;
    static Integer[][] grid = new Integer[MAX_ROW][MAX_ROW];

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent_of_code/src/resources/year2021/day11/input.txt");
        int x = 0;
        for (String line : lines) {
            List<Integer> row = Arrays.stream(line.split("")).map(str -> parseInt(str)).collect(Collectors.toList());
            for (int y = 0; y < row.size(); y++) {
                grid[x][y] = row.get(y);
            }
            x++;
        }
        Long part1 = solutionPart1();
        Long part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static Long solutionPart1() {
        Long total = 0L;
        for (int i = 0; i < 100; i++) {
            total += increaseEnergyLevel();
            resetGrid();
        }
        //drawBoard();
        return total;
    }

    public static Long solutionPart2() {
        Long step = 100L;
        while (!allFlash()) {
            increaseEnergyLevel();
            resetGrid();
            step++;
        }
        return step;
    }

    public static boolean allFlash() {
        for (int x = 0; x < MAX_ROW; x++) {
            for (int y = 0; y < MAX_ROW; y++) {
                if (grid[x][y] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Long increaseEnergyLevel() {
        Long total = 0L;
        for (int x = 0; x < MAX_ROW; x++) {
            for (int y = 0; y < MAX_ROW; y++) {
                total += increaseEnergyLevelOneSpot(x, y);
            }
        }
        return total;
    }

    public static void drawBoard() {
        for (int x = 0; x < MAX_ROW; x++) {
            StringBuffer line = new StringBuffer();
            for (int y = 0; y < MAX_ROW; y++) {
                line.append(grid[x][y]);
            }
            System.out.println(line.toString());
        }
    }

    public static Long increaseEnergyLevelOneSpot(int x, int y) {
        Long total = 0L;
        grid[x][y]++;
        if (grid[x][y] == 10) {
            total++;
            int lowX = (x == 0) ? -1 : x-1;
            int lowY = (y == 0) ? -1 : y-1;
            int highX = (x == 9) ? -1 : x+1;
            int highY = (y == 9 ) ? -1 : y+1;
            if (lowX >= 0 ) {
                if (lowY >= 0) {
                    total += increaseEnergyLevelOneSpot(lowX, lowY);
                }
                total += increaseEnergyLevelOneSpot(lowX, y);
                if (highY >= 0) {
                    total += increaseEnergyLevelOneSpot(lowX, highY);
                }
            }
            if (lowY >= 0) {
                total += increaseEnergyLevelOneSpot(x, lowY);
            }
            if (highY >= 0) {
                total += increaseEnergyLevelOneSpot(x, highY);
            }
            if (highX >= 0) {
                if (lowY >= 0) {
                    total += increaseEnergyLevelOneSpot(highX, lowY);
                }
                total += increaseEnergyLevelOneSpot(highX, y);
                if (highY >= 0) {
                    total += increaseEnergyLevelOneSpot(highX, highY);
                }
            }
        }
        return total;
    }

    public static void resetGrid() {
        for (int x = 0; x < MAX_ROW; x++) {
            for (int y = 0; y < MAX_ROW; y++) {
                if(grid[x][y] > 9) {
                    grid[x][y] = 0;
                }
            }
        }
    }

}
