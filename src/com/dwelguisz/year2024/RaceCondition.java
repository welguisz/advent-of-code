package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.utilities.Grid.createCharGridMap;

public class RaceCondition extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 20, false, 0);
        Map<Coord2D, Character> grid = createCharGridMap(lines);
        List<Coord2D> quickestPath = findQuickestPath(grid);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(quickestPath);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(quickestPath);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    @EqualsAndHashCode
    class shortestPath {
        @Getter
        Coord2D location;
        @Getter
        List<Coord2D> path;
        @Getter
        int count;

        public shortestPath(Coord2D location, List<Coord2D> path, int count) {
            this.location = location;
            this.path = path;
            this.count = count;
        }


        public List<shortestPath> getNextNodes(Map<Coord2D, Character> grid, Set<Coord2D> visited) {
            List<Coord2D> newPath = new ArrayList<>(path);
            newPath.add(location);
            return NEIGHBORS.stream().map(n -> n.add(location))
                    .filter(n -> grid.get(n) != '#')
                    .filter(n -> !visited.contains(n))
                    .map(l -> new shortestPath(l, newPath, count+1))
                    .toList();
        }

    }
    List<Coord2D> NEIGHBORS = List.of(new Coord2D(-1, 0), new Coord2D(1, 0), new Coord2D(0, -1), new Coord2D(0, 1));

    List<Pair<Coord2D, Coord2D>> possibleCheats (List<Coord2D> path, int jump) {
        int length = path.size();
        List<Pair<Coord2D, Coord2D>> checks = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (path.get(j).manhattanDistance(path.get(i)) == jump) {
                    checks.add(Pair.of(path.get(i), path.get(j)));
                }
            }
        }
        return checks;
    }

    List<Pair<Integer, Integer>> possibleCheatsPart2 (List<Coord2D> path, int jump) {
        int length = path.size();
        List<Pair<Integer, Integer>> checks = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                Integer md = path.get(j).manhattanDistance(path.get(i));
                if (md <= jump) {
                    checks.add(Pair.of(i-j,md));
                }
            }
        }
        return checks;
    }

    List<Coord2D> findQuickestPath(Map<Coord2D, Character> grid) {
        Coord2D startingPoint = grid.entrySet().stream().filter(e -> e.getValue() == 'S').findFirst().get().getKey();
        Coord2D endingPoint = grid.entrySet().stream().filter(e -> e.getValue() == 'E').findFirst().get().getKey();
        shortestPath path = new shortestPath(startingPoint, new ArrayList<>(), 0);
        PriorityQueue<shortestPath> queue = new PriorityQueue<>(200, (a,b) -> a.getCount() - b.getCount());
        queue.add(path);
        Set<Coord2D> visited = new HashSet<>();
        List<Coord2D> quickestPath = new ArrayList<>();
        while (!queue.isEmpty()) {
            shortestPath current = queue.poll();
            if (current.getLocation().equals(endingPoint)) {
                quickestPath = current.getPath();
                quickestPath.add(endingPoint);
                return quickestPath;
            }
            if (visited.contains(current.getLocation())) {
                continue;
            }
            visited.add(current.getLocation());
            queue.addAll(current.getNextNodes(grid, visited));
        }
        return List.of();
    }

    long solutionPart1(final List<Coord2D> quickestPath) {
        final List<Coord2D> quickP = new ArrayList<>(quickestPath);
        List<Pair<Coord2D, Coord2D>> cheatPoints = possibleCheats(quickP, 2);
        Map<Integer, Long> savings = cheatPoints.stream()
                .map(p -> quickP.indexOf(p.getLeft()) - quickP.indexOf(p.getRight()))
                .filter(l -> l > 0)
                .map(l -> l - 2)
                .filter(l -> l >= 100)
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
        return savings.values().stream().reduce(0L, Long::sum);
    }

    long solutionPart2(final List<Coord2D> quickestPath) {
        final List<Coord2D> quickP = new ArrayList<>(quickestPath);
        List<Pair<Integer, Integer>> cheatPoints = possibleCheatsPart2(quickP, 20);
        Map<Integer, Long> savings = cheatPoints.stream()
                .filter(p -> p.getLeft() > 0)
                .map(p -> p.getLeft() - p.getRight())
                .filter(l -> l >= 100)
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
        return savings.values().stream().reduce(0L, Long::sum);
    }
}
