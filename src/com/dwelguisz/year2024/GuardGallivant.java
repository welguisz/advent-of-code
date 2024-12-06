package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

public class GuardGallivant extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 6, false, 0);
        char[][] grid = convertToCharGrid(lines);
        Map<Coord2D, Character> gridHash = createGridHash(grid);
        Coord2D startingPoint = gridHash.entrySet().stream().filter(entry -> entry.getValue() == '^').findFirst().get().getKey();
        timeMarkers[1] = Instant.now().toEpochMilli();
        Set<Coord2D> visited = solutionPart1(gridHash, startingPoint);
        part1Answer = visited.size();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(gridHash, startingPoint, visited);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Coord2D> DIRECTIONS = List.of(
            new Coord2D(-1,-0),
            new Coord2D(0, 1),
            new Coord2D(1, 0),
            new Coord2D(0, -1))
            ;

    Map<Coord2D, Character> createGridHash(char[][] grid) {
        Map<Coord2D, Character> gridHash = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                gridHash.put(new Coord2D(i, j), grid[i][j]);
            }
        }
        return gridHash;
    }

    Set<Coord2D> solutionPart1(Map<Coord2D, Character> gridHash, Coord2D start) {
        Set<Coord2D> visited = new HashSet<>();
        int dirIndex = 0;
        Coord2D curr = start;
        while (true) {
            visited.add(curr);
            Coord2D next = curr.add(DIRECTIONS.get(dirIndex));
            if (!gridHash.containsKey(next)) {
                return visited;
            } else {
                if (gridHash.get(next) == '#') {
                    dirIndex = (dirIndex + 1) % 4;
                } else {
                    curr = next;
                }
            }
        }
    }

    long solutionPart2(Map<Coord2D, Character> gridHash, Coord2D start, Set<Coord2D> visited) {
        long createLoop = 0l;
        for (Coord2D obstacle : visited) {
            if (obstacle.equals(start)) {
                continue;
            }
            gridHash.put(obstacle, '#');
            Coord2D curr = start;
            int dirIndex = 0;
            boolean loop = true;
            Set<Pair<Coord2D, Coord2D>> memory = new HashSet<>();
            while (!memory.contains(Pair.of(curr, DIRECTIONS.get(dirIndex)))) {
                memory.add(Pair.of(curr, DIRECTIONS.get(dirIndex)));
                Coord2D next = curr.add(DIRECTIONS.get(dirIndex));
                if ( !gridHash.containsKey(next)) {
                    loop = false;
                    break;
                }
                if (gridHash.get(next) == '#') {
                    dirIndex = (dirIndex + 1) % 4;
                } else {
                    curr = next;
                }
            }
            if (loop) {
                createLoop++;
            }
            gridHash.put(obstacle, '.');
        }
        return createLoop;
    }
}
