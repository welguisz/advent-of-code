package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class TreetopTreeHouse extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day08/input.txt");
        Integer[][] grid = createGrid(lines);
        Long part1 = solutionPart1(grid);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Long part2 = solutionPart2(grid);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    Integer[][] createGrid(List<String> lines) {
        Integer yLen = lines.size();
        Integer xLen = lines.get(0).length();
        Integer[][] grid = new Integer[yLen][xLen];
        for (int i = 0; i < yLen; i++) {
            for (int j = 0; j < xLen; j++) {
                grid[i][j] = Integer.parseInt(lines.get(i).substring(j,j+1));
            }
        }
        return grid;
    }

    Long solutionPart1(Integer[][] grid) {
        Long sum = 0L;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length;j++) {
                sum += checkVisibility(grid,i,j) ? 1 : 0;
            }
        }
        return sum;
    }

    public boolean checkVisibility(Integer grid[][], Integer i, Integer j) {
        Integer height = grid[i][j];
        List<Integer> heights = new ArrayList<>();
        List<Boolean> visible = new ArrayList<>();
        for (int x = 0; x < i; x++) {
            heights.add(grid[x][j]);
        }
        visible.add(heights.stream().anyMatch(h -> h >= height));
        heights = new ArrayList<>();
        for (int x = i + 1; x < grid.length; x++) {
            heights.add(grid[x][j]);
        }
        visible.add(heights.stream().anyMatch(h -> h >= height));
        heights = new ArrayList<>();
        for (int x = 0; x < j; x++) {
            heights.add(grid[i][x]);
        }
        visible.add(heights.stream().anyMatch(h -> h >= height));
        heights = new ArrayList<>();
        for (int x = j + 1; x < grid[i].length; x++) {
            heights.add(grid[i][x]);
        }
        visible.add(heights.stream().anyMatch(h -> h >= height));
        return visible.stream().anyMatch(v -> !v);
    }

    Long solutionPart2(Integer[][] grid) {
        Integer maxScore = Integer.MIN_VALUE;
        for (int i = 0; i < grid.length;i++) {
            for (int j = 0; j < grid[i].length; j++) {
                maxScore = Integer.max(maxScore, getScenicScore(grid, i, j));
            }
        }
        return maxScore.longValue();
    }

    Integer getScenicScore(Integer[][] grid, Integer i, Integer j) {
        Integer treeCount[] = new Integer[]{0,0,0,0};
        Integer height = grid[i][j];
        for (int x = i-1; x >= 0; x--) {
            treeCount[0]++;
            if (height <= grid[x][j]) {
                break;
            }
        }
        for (int x = i + 1; x < grid.length; x++) {
            treeCount[1]++;
            if (height <= grid[x][j]) {
                break;
            }
        }
        for (int x = j-1; x >= 0; x--) {
            treeCount[2]++;
            if (height<= grid[i][x]) {
                break;
            }
        }
        for (int x = j + 1; x < grid[i].length; x++) {
            treeCount[3]++;
            if (height <= grid[i][x]) {
                break;
            }
        }
        return treeCount[0]*treeCount[1]*treeCount[2]*treeCount[3];
    }

}
