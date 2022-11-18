package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.List;

public class LikeAGIFForYourYard extends AoCDay {
    Integer[][] grid;
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day18/input.txt");
        createGrid(lines, false);
        Integer part1 = solutionPart1();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        createGrid(lines, true);
        Integer part2 = solutionPart1();
        System.out.println(String.format("Part 1 Answer: %d", part2));
    }

    public Integer solutionPart1() {
        for (int i = 0; i < 100; i++) {
            updateGrid();
        }
        return countLights();
    }
    public void createGrid(List<String> lines, boolean part2) {
        grid = new Integer[100][100];
        Integer row = 0;
        for (String line : lines) {
            String temp[] = line.split("");
            int col = 0;
            for (String t : temp) {
                grid[row][col] = (t.equals("#")) ? 1 : 0;
                col++;
            }
            row++;
        }
        if (part2) {
            grid[0][0] = 1;
            grid[0][99] = 1;
            grid[99][0] = 1;
            grid[99][99] = 1;
        }
    }

    public void updateGrid() {
        Integer[][] newGrid = new Integer[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                newGrid[i][j] = newLight(i,j);
            }
        }
        newGrid[0][0] = 1;
        newGrid[0][99] = 1;
        newGrid[99][0] = 1;
        newGrid[99][99] = 1;
        grid = newGrid;
    }

    public Integer newLight(int x, int y) {
        int negativeX = x - 1;
        int negativeY = y - 1;
        int positiveX = x + 1;
        int positiveY = y + 1;
        int sum = 0;
        if (negativeX >= 0) {
            if(negativeY >= 0) {
                sum += grid[negativeX][negativeY];
            }
            sum += grid[negativeX][y];
            if (positiveY < 100) {
                sum += grid[negativeX][positiveY];
            }
        }
        if (negativeY >= 0) {
            sum += grid[x][negativeY];
        }
        if (positiveY < 100) {
            sum += grid[x][positiveY];
        }
        if (positiveX < 100) {
            if(negativeY >= 0) {
                sum += grid[positiveX][negativeY];
            }
            sum += grid[positiveX][y];
            if (positiveY < 100) {
                sum += grid[positiveX][positiveY];
            }

        }
        if (grid[x][y] == 1) {
            if (sum == 2 || sum == 3) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (sum == 3) {
                return 1;
            }
        }
        return 0;
    }

    public void printGrid() {
        for (int i = 0; i < 100; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < 100; j++) {
                if (grid[i][j] == 0) {
                    sb.append('.');
                } else {
                    sb.append('#');
                }
            }
            System.out.println(sb);
        }
    }
    public Integer countLights() {
        Integer sum = 0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                sum += grid[i][j];
            }
        }
        return sum;
    }
}
