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
        List<Pair<Coord2D, Integer>> visited = solutionPart1(gridHash, startingPoint);
        part1Answer = visited.stream().map(Pair::getLeft).distinct().count();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(gridHash, visited);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Pair<Coord2D,Integer>> guardPath(Map<Coord2D, Character> grid, Pair<Coord2D, Integer> startingInfo) {
        return guardPath(grid, startingInfo.getLeft(), startingInfo.getRight());
    }

    List<Pair<Coord2D,Integer>> guardPath(Map<Coord2D, Character> grid, Coord2D startingPoint, int startingDirection) {
        Set<Coord2D> visited = new HashSet<>();
        int dirIndex = startingDirection;
        Coord2D curr = startingPoint;
        Set<Pair<Coord2D, Coord2D>> memory = new HashSet<>();
        List<Pair<Coord2D,Integer>> visitedOrder = new ArrayList<>();
        while (!memory.contains(Pair.of(curr, DIRECTIONS.get(dirIndex)))) {
            memory.add(Pair.of(curr, DIRECTIONS.get(dirIndex)));
            visited.add(curr);
            visitedOrder.add(Pair.of(curr, dirIndex));
            Coord2D next = curr.add(DIRECTIONS.get(dirIndex));
            if (memory.contains(Pair.of(next, DIRECTIONS.get(dirIndex)))) {
                break;
            }
            if (!grid.containsKey(next)) {
                return visitedOrder;
            } else {
                if (grid.get(next) == '#') {
                    dirIndex = (dirIndex + 1) % 4;
                } else {
                    curr = next;
                }
            }
        }
        return new ArrayList<>();
    }

    List<Pair<Coord2D,Integer>> solutionPart1(Map<Coord2D, Character> gridHash, Coord2D start) {
        return guardPath(gridHash, start, 0);
    }

    long solutionPart2(Map<Coord2D, Character> gridHash, List<Pair<Coord2D,Integer>> visited) {
        long createLoop = 0L;
        Set<Coord2D> alreadyVisited = new HashSet<>();
        for (int i = 0; i < visited.size()-1; i++) {
            Pair<Coord2D, Integer> nextStop = visited.get(i+1);
            Coord2D obstacle = nextStop.getLeft();
            if (alreadyVisited.contains(obstacle)) {
                continue;
            }
            alreadyVisited.add(obstacle);
            gridHash.put(obstacle, '#');
            if (guardPath(gridHash, visited.get(i)).isEmpty()) {
                createLoop++;
            }
            gridHash.put(obstacle, '.');
        }
        return createLoop;
    }
}
