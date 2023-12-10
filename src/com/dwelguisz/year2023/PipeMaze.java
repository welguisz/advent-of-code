package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
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

    public static List<Coord2D> DIRECTIONS = List.of(EAST, SOUTH ,WEST, NORTH);
    public static List<String> POSSIBLECHARS = List.of("-7J","|LJ","-FL","|F7");
    public static Map<Pair<Integer, String>, Integer> TRANSFORMS;
    static {
        TRANSFORMS = new HashMap<>();
        TRANSFORMS.put(Pair.of(0, "-"), 0);
        TRANSFORMS.put(Pair.of(0, "7"), 1);
        TRANSFORMS.put(Pair.of(0, "J"), 3);
        TRANSFORMS.put(Pair.of(1, "|"), 1);
        TRANSFORMS.put(Pair.of(1, "L"), 0);
        TRANSFORMS.put(Pair.of(1, "J"), 2);
        TRANSFORMS.put(Pair.of(2, "-"), 2);
        TRANSFORMS.put(Pair.of(2, "F"), 1);
        TRANSFORMS.put(Pair.of(2, "L"), 3);
        TRANSFORMS.put(Pair.of(3, "|"), 3);
        TRANSFORMS.put(Pair.of(3, "F"), 0);
        TRANSFORMS.put(Pair.of(3, "7"), 2);
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 10, false, 0);
        char[][] grid =convertToCharGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Set<Coord2D> findLoop(char[][] grid, Coord2D startingPoint) {
        Integer maxRow = grid.length;
        Integer maxCol = grid[0].length;
        Set<Coord2D> path = new HashSet<>();

        List<Character> nextToMe = DIRECTIONS.stream()
                .map(n -> startingPoint.add(n))
                .filter(n -> n.x >= 0 && n.x <= maxRow && n.y >= 0 && n.y <= maxCol)
                .map(n -> grid[n.x][n.y])
                .collect(Collectors.toList());
        List<Integer> startingDirections = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (POSSIBLECHARS.get(i).contains(""+ nextToMe.get(i))) {
                startingDirections.add(i);
            }
        }

        Integer currentDir = startingDirections.get(0);
        Coord2D current = startingPoint.add(DIRECTIONS.get(currentDir));
        path.add(startingPoint);
        while (!startingPoint.equals(current)) {
            path.add(current);
            currentDir = TRANSFORMS.get(Pair.of(currentDir, ""+grid[current.x][current.y]));
            current = current.add(DIRECTIONS.get(currentDir));
        }
        return path;
    }

    Coord2D findStartingLocation(char[][] grid) {
        List<Integer> rows = IntStream.range(0, grid.length).boxed()
                .filter(i -> Arrays.toString(grid[i]).toString().contains("S"))
                .collect(Collectors.toList());
        Integer rowS = rows.get(0);
        Integer colS = 0;
        while (colS < grid[rowS].length && grid[rowS][colS]!='S') {
            colS++;
        }
        return new Coord2D(rowS, colS);
    }
    Long solutionPart1(char[][] grid) {
        Coord2D startingPoint = findStartingLocation(grid);
        Set<Coord2D> mazePath = findLoop(grid, startingPoint);
        return (long) (mazePath.size() / 2);
    }

    Long solutionPart2(char[][] grid) {
        Integer maxRow = grid.length;
        Integer maxCol = grid[0].length;
        Coord2D startingPoint = findStartingLocation(grid);
        Set<Coord2D> mazePath = findLoop(grid, startingPoint);
        Long count = 0L;
        List<Integer> startingDirections = new ArrayList<>();
        List<Character> nextToMe = DIRECTIONS.stream()
                .map(n -> startingPoint.add(n))
                .filter(n -> n.x >= 0 && n.x <= maxRow && n.y >= 0 && n.y <= maxCol)
                .map(n -> grid[n.x][n.y])
                .collect(Collectors.toList());
        for (int i = 0; i < 4; i++) {
            if (POSSIBLECHARS.get(i).contains(""+ nextToMe.get(i))) {
                startingDirections.add(i);
            }
        }
        for (int i = 0; i < maxRow; i++) {
            Boolean inMaze = false;
            for (int j = 0; j < maxCol; j++) {
                if (mazePath.contains(new Coord2D(i,j))) {
                    if ("|JL".contains("" + grid[i][j]) || (grid[i][j] == 'S' && startingDirections.contains(3))) {
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
