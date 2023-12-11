package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PipeMaze extends AoCDay {

    public static Coord2D NORTH = new Coord2D(-1,0);
    public static Coord2D SOUTH = new Coord2D(1,0);
    public static Coord2D EAST = new Coord2D(0,1);
    public static Coord2D WEST = new Coord2D(0,-1);

    public static Map<Pair<Coord2D, String>, Coord2D> NEXT_DIRECTION;
    static {
        NEXT_DIRECTION = new HashMap<>();
        NEXT_DIRECTION.put(Pair.of(EAST, "-"), EAST);
        NEXT_DIRECTION.put(Pair.of(EAST, "7"), SOUTH);
        NEXT_DIRECTION.put(Pair.of(EAST, "J"), NORTH);
        NEXT_DIRECTION.put(Pair.of(SOUTH, "|"), SOUTH);
        NEXT_DIRECTION.put(Pair.of(SOUTH, "L"), EAST);
        NEXT_DIRECTION.put(Pair.of(SOUTH, "J"), WEST);
        NEXT_DIRECTION.put(Pair.of(WEST, "-"), WEST);
        NEXT_DIRECTION.put(Pair.of(WEST, "F"), SOUTH);
        NEXT_DIRECTION.put(Pair.of(WEST, "L"), NORTH);
        NEXT_DIRECTION.put(Pair.of(NORTH, "|"), NORTH);
        NEXT_DIRECTION.put(Pair.of(NORTH, "F"), EAST);
        NEXT_DIRECTION.put(Pair.of(NORTH, "7"), WEST);
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 10, false, 0);
        char[][] grid =convertToCharGrid(lines);
        findLoop(grid, findStartingLocation(grid,'S'));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Coord2D> possibleStartingSteps;
    Set<Coord2D> path;
    void findLoop(char[][] grid, Coord2D startingPoint) {
        path = new HashSet<>();

        possibleStartingSteps = NEXT_DIRECTION.keySet().stream()
                .map(p -> Pair.of(startingPoint.add(p.getLeft()), p.getRight()))
                .filter(p -> grid[p.getLeft().x][p.getLeft().y] == p.getRight().charAt(0))
                .map(point -> point.getLeft().add(startingPoint.multiply(-1)))
                .collect(Collectors.toList());

        Coord2D currentStep = possibleStartingSteps.get(0);
        Coord2D current = startingPoint.add(currentStep);
        path.add(startingPoint);
        while (!startingPoint.equals(current)) {
            path.add(current);
            currentStep = NEXT_DIRECTION.get(Pair.of(currentStep, ""+grid[current.x][current.y]));
            current = current.add(currentStep);
        }
    }

    Long solutionPart1() {
        return (long) (path.size() / 2);
    }

    Long solutionPart2(char[][] grid) {
        Integer maxRow = grid.length;
        Integer maxCol = grid[0].length;
        Long count = 0L;
        for (int i = 0; i < maxRow; i++) {
            Boolean inMaze = false;
            for (int j = 0; j < maxCol; j++) {
                if (path.contains(new Coord2D(i,j))) {
                    if ("|JL".contains("" + grid[i][j]) || (grid[i][j] == 'S' && possibleStartingSteps.contains(NORTH))) {
                        inMaze = !inMaze;
                    }
                } else {
                    count += inMaze ? 1 : 0;
                }
            }

        }
        return count;
    }
}
