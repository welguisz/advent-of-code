package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

import static com.dwelguisz.utilities.Grid.createCharGridMap;
import static com.dwelguisz.utilities.Grid.getStartingPoint;
import static com.dwelguisz.utilities.Grid.DIRECTIONS;

public class GuardGallivant extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 6, false, 0);
        Map<Coord2D, Character> gridHash = createCharGridMap(lines);
        Coord2D startingPoint = getStartingPoint(gridHash, '^');
        timeMarkers[1] = Instant.now().toEpochMilli();
        Set<Coord2D> visited = solutionPart1(gridHash, startingPoint);
        part1Answer = visited.size();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(gridHash, startingPoint, visited);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Set<Coord2D> guardPath(Map<Coord2D, Character> grid, Coord2D startingPoint) {
        Set<Coord2D> visited = new HashSet<>();
        int dirIndex = 0;
        Coord2D curr = startingPoint;
        Set<Pair<Coord2D, Coord2D>> memory = new HashSet<>();
        boolean loop = true;
        while (!memory.contains(Pair.of(curr, DIRECTIONS.get(dirIndex)))) {
            memory.add(Pair.of(curr, DIRECTIONS.get(dirIndex)));
            visited.add(curr);
            Coord2D next = curr.add(DIRECTIONS.get(dirIndex));
            if (memory.contains(Pair.of(next, DIRECTIONS.get(dirIndex)))) {
                break;
            }
            if (!grid.containsKey(next)) {
                return visited;
            } else {
                if (grid.get(next) == '#') {
                    dirIndex = (dirIndex + 1) % 4;
                } else {
                    curr = next;
                }
            }
        }
        return new HashSet<>();
    }

    Set<Coord2D> solutionPart1(Map<Coord2D, Character> gridHash, Coord2D start) {
        return guardPath(gridHash, start);
    }

    long solutionPart2(Map<Coord2D, Character> gridHash, Coord2D start, Set<Coord2D> visited) {
        long createLoop = 0l;
        for (Coord2D obstacle : visited) {
            if (obstacle.equals(start)) {
                continue;
            }
            gridHash.put(obstacle, '#');
            Set<Coord2D> path = guardPath(gridHash, start);
            if (path.isEmpty()) {
                createLoop++;
            }
            gridHash.put(obstacle, '.');
        }
        return createLoop;
    }
}
