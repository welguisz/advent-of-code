package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.graph.transversal.PathSearch;
import com.dwelguisz.utilities.graph.transversal.SearchStateNode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AoC2023Day23 extends AoCDay {

    Coord2D UP = new Coord2D(-1,0);
    Coord2D RIGHT = new Coord2D(0,1);
    Coord2D DOWN = new Coord2D(1,0);
    Coord2D LEFT = new Coord2D(0,-1);
    Coord2D STARTING_DIRECTION = new Coord2D(-1,-1);
    List<Coord2D> POSSIBLE_NEXT_STEPS = List.of(UP,RIGHT,DOWN,LEFT);
    Map<Character, List<Coord2D>> filter = Map.of(
            '>',List.of(RIGHT),
            'v',List.of(DOWN),
            '<',List.of(LEFT),
            '^',List.of(DOWN),
            '.', POSSIBLE_NEXT_STEPS,
            '#', List.of());
    public abstract class WalkingPathStateBase extends SearchStateNode {
        int hashCode;

        public WalkingPathStateBase(Coord2D location, Coord2D direction, Integer subInfo) {
            super(location, direction, subInfo);
        }

        @Override
        public String toString() {
            return location.toString() + direction.toString() + subInfo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            WalkingPathStateBase other = (WalkingPathStateBase) o;
            return (this.location.equals(other.location) && this.direction.equals(other.location)
                    && this.subInfo.equals(other.subInfo));
        }

        public boolean inGrid(Coord2D loc, Object[][] grid) {
            return (0 <= loc.x && loc.x < grid.length && 0 <= loc.y && loc.y < grid[0].length);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }

    public class WalkingPathState extends WalkingPathStateBase {
        public WalkingPathState(Coord2D loc, Coord2D direction, Set<Coord2D> previousSteps) {
            super(loc,direction,0);
            this.hashCode = Objects.hash(location, this.direction, this.previousSteps);
        }
        public WalkingPathState(Coord2D loc, Coord2D direction, Set<Coord2D> previousSteps, Coord2D previous) {
            super(loc,direction,0);
            if (previousSteps.isEmpty()) {
                this.previousSteps = new HashSet<>();
            } else {
                this.previousSteps = new HashSet<>(previousSteps);
            }
            this.previousSteps.add(previous);
            this.hashCode = Objects.hash(location, this.direction, this.previousSteps);
        }

        @Override
        public Stream<WalkingPathState> getNextNodes(
                Object[][] grid, BiFunction<SearchStateNode, Coord2D, Boolean> func,
                Set<SearchStateNode> visited,
                Map<String, Long> cost
        ) {
            Integer choices = 0;
            Coord2D current = new Coord2D(location.x, location.y);
            List<WalkingPathState> nextLocs;
            return filter.get(grid[current.x][current.y]).stream()
                    .filter(nxt -> {
                        Coord2D tmp = location.add(nxt);
                        if (inGrid(tmp, grid)) {
                            return !grid[tmp.x][tmp.y].equals('#');
                        }
                        return false;
                    })
                    .map(c -> new WalkingPathState(location.add(c), c, previousSteps, location))
                    .filter(s -> !visited.contains(s))
                    .filter(s -> !previousSteps.contains(s.location));
        }

        @Override
        public Stream<? extends SearchStateNode> getBackwardsNodes(
                Object[][] grid,
                BiFunction<SearchStateNode, Coord2D, Boolean> func,
                Map<Coord2D, Long> stepsToEnd,
                Long maxSteps
        ) {
            return Stream.of();
        }

    }

    public class WalkingPathState2 extends WalkingPathStateBase {
        int hashCode;

        public WalkingPathState2(Coord2D loc, Coord2D direction, Set<Coord2D> previousSteps) {
            super(loc, direction, 0);
            if (previousSteps.isEmpty()) {
                this.previousSteps = new HashSet<>();
            } else {
                this.previousSteps = new HashSet<>(previousSteps);
            }
            this.hashCode = Objects.hash(location, this.direction, this.previousSteps);
        }

        public WalkingPathState2(Coord2D loc, Coord2D direction, Set<Coord2D> previousSteps, Coord2D previous) {
            super(loc, direction, 0);
            if (previousSteps.isEmpty()) {
                this.previousSteps = new HashSet<>();
            } else {
                this.previousSteps = new HashSet<>(previousSteps);
            }
            this.previousSteps.add(previous);
            this.hashCode = Objects.hash(location, this.direction, this.previousSteps);
        }

        @Override
        public Stream<WalkingPathState2> getNextNodes(
                Object[][] grid, BiFunction<SearchStateNode, Coord2D, Boolean> func,
                Set<SearchStateNode> visited,
                Map<String, Long> cost
        ) {
            return POSSIBLE_NEXT_STEPS.stream()
                    .filter(nxt -> {
                        Coord2D tmp = location.add(nxt);
                        if (inGrid(tmp, grid)) {
                            return !grid[tmp.x][tmp.y].equals('#');
                        }
                        return false;
                    })
                    .map(c -> new WalkingPathState2(location.add(c), c, previousSteps, location))
                    .filter(s -> !visited.contains(s))
                    .filter(s -> !previousSteps.contains(s.location));
        }
    }
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 23, false, 0);
        char[][] grid = convertToCharGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        System.out.println("Starting Part 2");
        //part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Long solutionPart1(char[][] grid) {
        Character newGrid[][] = new Character[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(grid.length-1,grid[0].length-2);
        WalkingPathState startingState = new WalkingPathState(startingPoint, STARTING_DIRECTION, new HashSet<>());
        return new PathSearch().findLongestPath(startingState,
                (walkingState) -> walkingState.location.equals(endingPoint),
                (crucibleState, nextLoc) -> true,
                newGrid);

    }



    Long solutionPart2(char[][] grid) {
        Character newGrid[][] = new Character[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(grid.length-1,grid[0].length-2);
        WalkingPathState2 startingState = new WalkingPathState2(startingPoint, STARTING_DIRECTION, new HashSet<>());
        return new PathSearch().findLongestPath(startingState,
                (walkingState) -> walkingState.location.equals(endingPoint),
                (crucibleState, nextLoc) -> true,
                newGrid);
    }

}
