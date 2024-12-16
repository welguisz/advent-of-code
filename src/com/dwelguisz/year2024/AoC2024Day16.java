package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.utilities.Grid.createCharGridMap;

public class AoC2024Day16 extends AoCDay {

    ReindeerPathState bestPath;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 16, true, 0);
        Map<Coord2D, Character> grid = createCharGridMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Map<Coord2D, List<Coord2D>> possibleTurns = Map.of(
            new Coord2D(0,1), List.of(new Coord2D(-1,0), new Coord2D(1, 0), new Coord2D(0, 1)),
            new Coord2D(0,-1), List.of(new Coord2D(-1,0), new Coord2D(1, 0), new Coord2D(0, -1)),
            new Coord2D(1, 0), List.of(new Coord2D(0, -1), new Coord2D(1, 0), new Coord2D(0, 1)),
            new Coord2D(-1, 0), List.of(new Coord2D(0, -1), new Coord2D(-1, 0), new Coord2D(0, 1))
    );

    Map<Coord2D, List<Coord2D>> reversePossibleTurns = Map.of(
            new Coord2D(0,1), List.of(new Coord2D(-1,0), new Coord2D(1, 0), new Coord2D(0, -1)),
            new Coord2D(0,-1), List.of(new Coord2D(-1,0), new Coord2D(1, 0), new Coord2D(0, 1)),
            new Coord2D(1, 0), List.of(new Coord2D(0, -1), new Coord2D(-1, 0), new Coord2D(0, 1)),
            new Coord2D(-1, 0), List.of(new Coord2D(0, -1), new Coord2D(1, 0), new Coord2D(0, 1))
    );

    public class ReindeerPathState {
        @Getter
        Coord2D location;
        @Getter
        Coord2D direction;
        @Getter
        Integer turns;
        @Getter
        Integer steps;
        @Getter
        Set<ReindeerPathState> bestSeats;
        @Getter
        List<ReindeerPathState> path;
        private int hashCode;

        public ReindeerPathState(Coord2D location, Coord2D direction) {
            this(location, direction, 0, 0);
        }
        public ReindeerPathState(Coord2D location, Coord2D direction, Integer turns, Integer steps) {
            this(location, direction, turns, steps, new HashSet<>(), new ArrayList<>());
        }

        public ReindeerPathState(Coord2D location, Coord2D direction, Integer turns, Integer steps, Set<ReindeerPathState> bestSeats, List<ReindeerPathState> path) {
            this.location = location;
            this.direction = direction;
            this.turns = turns;
            this.steps = steps;
            this.bestSeats = bestSeats;
            this.path = path;
            this.hashCode = Objects.hash(location, direction, turns, steps);
        }

        @Override
        public String toString() {
            return "location: " + location + ", direction: " + direction + ", turns: " + turns + ", steps: " + steps + ", score: " + getScore();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReindeerPathState that = (ReindeerPathState) o;
            return location.equals(that.location) && direction.equals(that.direction) && turns.equals(that.turns) && steps.equals(that.steps);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        List<ReindeerPathState> getNextState(Map<Coord2D, Character> grid) {
            Set<ReindeerPathState> t = new HashSet<>(bestSeats);
            t.add(this);
            List<ReindeerPathState> ps = new ArrayList<>(path);
            ps.add(this);
            return possibleTurns.get(direction).stream()
                    .map(n -> {
                        if (n.equals(direction)) {
                            return new ReindeerPathState(location.add(n), direction, turns, steps+1, t, ps);
                        } else {
                            return new ReindeerPathState(location, n, turns+1, steps, t, ps);
                        }
                    })
                    .filter(s -> grid.get(s.getLocation()) != '#')
                    .toList();
        }

        List<ReindeerPathState> getNextStatePart2(
                ReindeerPathState bestState,
                Map<Coord2D, Character> grid) {
            Set<ReindeerPathState> t = new HashSet<>(bestSeats);
            t.add(this);
            List<ReindeerPathState> ps = new ArrayList<>(path);
            ps.add(this);
            return possibleTurns.get(direction).stream()
                    .map(n -> {
                        if (n.equals(direction)) {

                            return new ReindeerPathState(location.add(n), direction, turns, steps+1, t, ps);
                        } else {
                            return new ReindeerPathState(location, n, turns+1, steps, t, ps);
                        }
                    })
                    .filter(p -> p.possibleBestSeat(bestState))
                    .filter(p -> grid.get(p.getLocation()) != '#')
                    .toList();

        }

        public Integer getScore() {
            return turns*1000+steps;
        }

        public boolean possibleBestSeat(ReindeerPathState bestPath) {
            return (steps <= bestPath.getSteps()) && (turns <= bestPath.getTurns());
        }

        public boolean addBestSeats(Set<ReindeerPathState> newSeats) {
            return bestSeats.addAll(newSeats);
        }
    }

    long solutionPart1(Map<Coord2D, Character> grid) {
        Coord2D startingPoint = grid.entrySet().stream()
                .filter(e -> e.getValue() == 'S')
                .map(e -> e.getKey())
                .toList().get(0);
        Coord2D endingPoint = grid.entrySet().stream()
                .filter(e -> e.getValue() == 'E')
                .map(e -> e.getKey())
                .toList().get(0);
        ReindeerPathState currentState = new ReindeerPathState(startingPoint, new Coord2D(0,1), 0, 0, new HashSet<>(), new ArrayList<>());
        PriorityQueue<ReindeerPathState> queue = new PriorityQueue<>(200, Comparator.comparingInt(ReindeerPathState::getScore));
        queue.add(currentState);
        while (!queue.isEmpty()) {
            ReindeerPathState current = queue.poll();
            Coord2D location = current.getLocation();
            Coord2D direction = current.getDirection();
            if (grid.get(location) == '#') {
                continue;
            }
            if (location.equals(endingPoint)) {
                bestPath = current;
                return current.getScore();
            }
            queue.addAll(current.getNextState(grid));
        }
        return 0L;
    }

    long solutionPart2(Map<Coord2D, Character> grid) {
        Coord2D startingPoint = grid.entrySet().stream()
                .filter(e -> e.getValue() == 'S')
                .map(e -> e.getKey())
                .toList().get(0);
        Coord2D endingPoint = grid.entrySet().stream()
                .filter(e -> e.getValue() == 'E')
                .map(e -> e.getKey())
                .toList().get(0);
        ReindeerPathState currentState = new ReindeerPathState(startingPoint, new Coord2D(0,1));
        PriorityQueue<ReindeerPathState> queue = new PriorityQueue<>(200, Comparator.comparingInt(ReindeerPathState::getScore));
        queue.add(currentState);
        List<ReindeerPathState> bestSeats = new ArrayList<>();
        Set<Pair<Coord2D, Coord2D>> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            ReindeerPathState current = queue.poll();
            Coord2D location = current.getLocation();
            Coord2D direction = current.getDirection();
            if (grid.get(location) == '#') {
                continue;
            }
            System.out.println("Current Location:" + current);
            if (bestPath.getPath().contains(current)) {
                bestPath.addBestSeats(current.getBestSeats());
            }
            if (visited.contains(Pair.of(location, direction))) {
                continue;
            }
            visited.add(Pair.of(current.getLocation(), current.getDirection()));
            queue.addAll(current.getNextStatePart2(bestPath, grid));
        }
        Set<Coord2D> onPath = bestSeats.stream().map(ReindeerPathState::getLocation).collect(Collectors.toSet());
        printGrid(grid, onPath);
        return onPath.size();
    }

    void printGrid(Map<Coord2D, Character> grid, Set<Coord2D> onPath) {
        Integer maxX = grid.keySet().stream().mapToInt(k -> k.x).max().getAsInt();
        Integer maxY = grid.keySet().stream().mapToInt(k -> k.y).max().getAsInt();
        for (int x = 0; x <= maxX; x++) {
            StringBuffer buffer = new StringBuffer();
            for (int y = 0; y <= maxY; y++) {
                if (onPath.contains(new Coord2D(x, y))) {
                    buffer.append("O");
                } else {
                    buffer.append(grid.get(new Coord2D(x, y)));
                }
            }
            System.out.println(buffer.toString());
        }
    }

}
