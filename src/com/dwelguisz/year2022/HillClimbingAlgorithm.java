package com.dwelguisz.year2022;

import com.dwelguisz.base.BreadthFirstSearch;
import com.dwelguisz.year2022.helper.HillSearchNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HillClimbingAlgorithm extends BreadthFirstSearch<String> {

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day12/input.txt");
        String grid[][] = convertToGrid(lines);
        setMap(grid);
        Integer part1 = solutionPart1(grid);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        Integer part2 = solutionPart2(grid);
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

    Integer solutionPart1(String[][] grid) {
        Pair<Integer, Integer> startingPoint = Pair.of(0,0);
        Pair<Integer, Integer> goalPoint = Pair.of(0,0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].equals("S")) {
                    startingPoint = Pair.of(i,j);
                }
                if (grid[i][j].equals("E")) {
                    goalPoint = Pair.of(i,j);
                }
            }
        }
        HillSearchNode node = new HillSearchNode(startingPoint, goalPoint, new ArrayList<>(), new HashSet<>());
        return findShortestPath(startingPoint, goalPoint, Integer.MAX_VALUE, node);
    }

    Integer solutionPart2(String[][] grid) {
        List<Pair<Integer,Integer>> startingPoints = new ArrayList<>();
        Pair<Integer, Integer> goalPoint = Pair.of(0,0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].equals("S")) {
                    startingPoints.add(0,Pair.of(i,j));
                }
                if (grid[i][j].equals("a")) {
                    startingPoints.add(Pair.of(i,j));
                }
                if (grid[i][j].equals("E")) {
                    goalPoint = Pair.of(i,j);
                }
            }
        }
        Integer minValue = Integer.MAX_VALUE;
        for (Pair<Integer, Integer> s : startingPoints) {
            HillSearchNode node = new HillSearchNode(s, goalPoint, new ArrayList<>(), new HashSet<>());
            Integer cur = findShortestPath(s, goalPoint, minValue, node);
            minValue = Integer.min(minValue, cur);
        }
        return minValue;
    }

}
