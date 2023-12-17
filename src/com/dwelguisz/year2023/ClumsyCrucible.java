package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClumsyCrucible extends AoCDay {

    Coord2D UP = new Coord2D(-1,0);
    Coord2D RIGHT = new Coord2D(0,1);
    Coord2D DOWN = new Coord2D(1,0);
    Coord2D LEFT = new Coord2D(0,-1);
    Coord2D STARTING_DIRECTION = new Coord2D(-1,-1);

    List<Coord2D> POSSIBLE_NEXT_STEPS = List.of(UP,RIGHT,DOWN,LEFT, STARTING_DIRECTION);

    Set<State> visited;
    Map<String, Long> heatMap;
    public class State {
        public Coord2D location;
        Coord2D direction;
        Integer currentSteps;

        private int hashCode;

        public State(Coord2D location, Coord2D direction, Integer currentSteps) {
            this.location = location;
            this.direction = direction;
            this.currentSteps = currentSteps;
            this.hashCode = Objects.hash(location, direction, currentSteps);
        }

        @Override
        public String toString() {
            return location.toString() + direction.toString() + currentSteps;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            State other = (State) o;
            return (this.location.equals(other.location) && this.direction.equals(other.location)
                    && this.currentSteps.equals(other.currentSteps));
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        boolean inGrid(Coord2D nextDir, char[][]grid) {
            Coord2D nextLoc = location.add(nextDir);
            Integer r = nextLoc.x;
            Integer c = nextLoc.y;
            return (0 <= r && r < grid.length && 0 <= c && c < grid[0].length);
        }

        public Stream<State> nextSteps(char[][] grid, BiFunction<State, Coord2D, Boolean> func) {
            return POSSIBLE_NEXT_STEPS.stream()
                    .filter(nxd -> !nxd.equals(STARTING_DIRECTION) &&
                            !direction.multiply(-1).equals(nxd) &&
                            func.apply(this, nxd) &&
                            inGrid(nxd, grid)
                    )
                    .map(p -> {
                Coord2D nextLoc = location.add(p);
                Integer steps = direction.equals(p) ? currentSteps + 1 : 1;
                visited.add(this);
                return new State(nextLoc, p,steps);}
                    )
                    .filter(s -> !heatMap.containsKey(s.toString()));
        }
    }
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 17, false, 0);
        char[][] grid = convertToCharGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Long solutionPart1(char[][] grid) {
        return findShortestPath(
                new Coord2D(0,0),
                (state, nextLoc) -> state.currentSteps <= 3,
                grid
        );
    }

    Long solutionPart2(char[][] grid) {
        return findShortestPath(
                new Coord2D(0,0),
                (state, nextLoc) -> (state.currentSteps <= 10) &&
                        (state.direction.equals(nextLoc) ||
                                (state.currentSteps >= 4) ||
                                state.direction.equals(STARTING_DIRECTION)),
                grid
        );
    }


    public Long findShortestPath(
            Coord2D startingLocation,
            BiFunction<State, Coord2D, Boolean> func,
            char[][]grid
    ) {
        visited = new HashSet<>();
        heatMap = new HashMap<>();
        PriorityQueue<State> stateQ = new PriorityQueue<>(500, (a,b) -> {
            if (heatMap.get(a.toString()) - heatMap.get(b.toString()) != 0L) {
                return Math.toIntExact(heatMap.get(a.toString()) - heatMap.get(b.toString()));
            }
            Coord2D locA = a.location;
            Coord2D locB = b.location;
            if (locA.x - locB.x != 0) {
                return locA.x - locB.x;
            } else if (locA.y-locB.y != 0) {
                return locA.y - locB.y;
            }
            return POSSIBLE_NEXT_STEPS.indexOf(a.direction) - POSSIBLE_NEXT_STEPS.indexOf(b.direction);
        });
        State initialState = new State(startingLocation, STARTING_DIRECTION, 1);
        heatMap.put(initialState.toString(), 0L);
        stateQ.add(initialState);
        Coord2D endLoc = new Coord2D(grid.length-1,grid[0].length-1);
        while(!stateQ.isEmpty()) {
            State currentState = stateQ.poll();
            if (visited.contains(currentState)) {
                continue;
            }
            if (currentState.location.equals(endLoc)) {
                return heatMap.get(currentState.toString());
            }
            Set<State> nextState = currentState.nextSteps(grid, func).collect(Collectors.toSet());
            Long value = heatMap.get(currentState.toString());
            nextState.stream().forEach(s -> heatMap.put(s.toString(), value+Integer.parseInt(""+grid[s.location.x][s.location.y])));
            stateQ.addAll(nextState);
        }
        return -1L;
    }
}
