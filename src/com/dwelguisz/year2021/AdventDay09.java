package com.dwelguisz.year2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay09 {

    static int[][] grid;
    static int height;
    static int width;

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2021/day9/input.txt");
        createRows(instructions);
        int part1 = findAnswer(false);
        int part2 = findAnswer(true);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static int findAnswer(boolean part2) {
        List<Integer> basinCounts = new ArrayList<>();
        int total = 0;
        boolean[][] basinGrid = new boolean[width][height];
        for(int y = 0; y < height; y++ ) {
            for(int x = 0; x < width; x++) {
                basinGrid[x][y] = false;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] next = nextToMe(x,y);
                if (isMin(grid[x][y], next[0], next[1], next[2], next[3])) {
                    if (part2) {
                        basinCounts.add(getBasinCount(x, y, grid, basinGrid));
                    } else {
                        total += grid[x][y] + 1;
                    }
                }
            }
        }
        if (!part2) {
            return total;
        }
        basinCounts.sort(Integer::compareTo);
        basinCounts.sort(Collections.reverseOrder());
        return basinCounts.get(0) * basinCounts.get(1) * basinCounts.get(2);
    }

    static Integer getBasinCount(int x, int y, int[][] grid, boolean[][] basinGrid) {
        if (basinGrid[x][y] || grid[x][y] == 9) {
            return 0;
        }
        int count = 0;
        count += 1;
        basinGrid[x][y] = true;
        if (x > 0) {
            count += getBasinCount(x-1, y, grid, basinGrid);
        }
        if (y > 0) {
            count += getBasinCount(x, y-1, grid, basinGrid);
        }
        if (x < width - 1) {
            count += getBasinCount(x+1, y, grid, basinGrid);
        }
        if (y < height - 1) {
            count += getBasinCount(x, y+1, grid, basinGrid);
        }
        return count;
    }

    public static int[] nextToMe(int x, int y) {
        int roof = (x == 0) ? 12 : grid[x-1][y];
        int floor =(x == height-1) ? 12 : grid[x+1][y];
        int left = (y == 0) ? 12 : grid[x][y-1];
        int right = (y == width-1) ? 12 : grid[x][y+1];
        return new int[]{roof,floor,left,right};

    }

    public static boolean isMin(int current, int roof, int floor, int left, int right) {
        return ((current < roof ) && (current < floor) && (current < left)  && (current < right));
    }

    public static void createRows(List<String> input) {
        List<List<Integer>> rows = new ArrayList<>();
        for(String in: input) {
            List<Integer> row = Arrays.stream(in.split("")).map(str -> parseInt(str)).collect(Collectors.toList());
            rows.add(row);
        }
        height = rows.size();
        width = rows.get(0).size();
        grid = new int[width][height];
        int currentY = 0;
        for(final List<Integer> row : rows) {
            for(int x = 0; x < row.size(); x++) {
                grid[x][currentY] = rows.get(currentY).get(x);
            }
            currentY++;
        }
    }
}
