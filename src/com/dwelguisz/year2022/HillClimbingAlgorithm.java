package com.dwelguisz.year2022;

import com.dwelguisz.base.BreadthFirstSearch;
import com.dwelguisz.base.SearchNode;
import com.dwelguisz.year2022.helper.HillSearchNode;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HillClimbingAlgorithm extends BreadthFirstSearch<String> {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,12,false,0);
        String grid[][] = convertToGrid(lines);
        setMap(grid);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
        return findShortestPath(startingPoint, Integer.MAX_VALUE, node);
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
        List<SearchNode> nodes = new ArrayList<>();
        for (Pair<Integer, Integer> s : startingPoints) {
            nodes.add(new HillSearchNode(s, goalPoint, new ArrayList<>(), new HashSet<>()));
        }
        return findShortestPath(startingPoints, minValue, nodes);
    }

}
