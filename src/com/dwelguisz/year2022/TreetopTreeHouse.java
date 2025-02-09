package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TreetopTreeHouse extends AoCDay {
    List<Pair<Integer, Integer>> visibleTrees;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,8,false,0);
        Integer[][] grid = createGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
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

    Integer solutionPart1(Integer[][] grid) {
        visibleTrees = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length;j++) {
                if (checkVisibility(grid,i,j)) {
                    visibleTrees.add(Pair.of(i,j));
                }
            }
        }
        return visibleTrees.size();
    }

    public boolean checkVisibility(Integer grid[][], Integer i, Integer j) {
        Integer height = grid[i][j];
        List<Boolean> visible = new ArrayList<>();
        visible.add(IntStream.range(0,i).map(x -> grid[x][j]).anyMatch(h -> h >= height));
        visible.add(IntStream.range(i+1,grid.length).map(x -> grid[x][j]).anyMatch(h -> h >= height));
        visible.add(IntStream.range(0,j).map(x -> grid[i][x]).anyMatch(h -> h >= height));
        visible.add(IntStream.range(j+1,grid[i].length).map(x -> grid[i][x]).anyMatch(h -> h >= height));
        return visible.stream().anyMatch(v -> !v);
    }

    Long solutionPart2(Integer[][] grid) {
        Integer maxScore = Integer.MIN_VALUE;
        Pair<Integer, Integer> treeLocation = Pair.of(0,0);
        for (Pair<Integer, Integer> tree : visibleTrees) {
            Integer scenicScore = getScenicScore(grid, tree.getLeft(), tree.getRight());
            if (maxScore < scenicScore) {
                maxScore = scenicScore;
                treeLocation = Pair.of(tree.getLeft(), tree.getRight());
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
