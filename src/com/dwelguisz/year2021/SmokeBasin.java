package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class SmokeBasin extends AoCDay {

    int[][] grid;
    int height;
    int width;

    public SmokeBasin() {
        super();
        grid = new int[1][1];
        height = 1;
        width = 1;
    }

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day9/input.txt");
        createRows(instructions);
        int part1 = findAnswer(false);
        int part2 = findAnswer(true);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public int findAnswer(boolean part2) {
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

    Integer getBasinCount(int x, int y, int[][] grid, boolean[][] basinGrid) {
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

    public int[] nextToMe(int x, int y) {
        int roof = (x == 0) ? 12 : grid[x-1][y];
        int floor =(x == height-1) ? 12 : grid[x+1][y];
        int left = (y == 0) ? 12 : grid[x][y-1];
        int right = (y == width-1) ? 12 : grid[x][y+1];
        return new int[]{roof,floor,left,right};

    }

    public boolean isMin(int current, int roof, int floor, int left, int right) {
        return ((current < roof ) && (current < floor) && (current < left)  && (current < right));
    }

    public void createRows(List<String> input) {
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
