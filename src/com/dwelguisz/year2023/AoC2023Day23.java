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

    public class WalkingPathState extends SearchStateNode {
        final Boolean part2;
        final int hashCode;
        public WalkingPathState(Coord2D loc, Coord2D direction, Set<Coord2D> previousSteps, Boolean part2) {
            super(loc,direction,0);
            this.part2 = part2;
            this.hashCode = Objects.hash(location, this.direction, previousSteps);
        }
        public WalkingPathState(Coord2D loc, Coord2D direction, Set<Coord2D> previousSteps, Coord2D previous, Boolean part2) {
            super(loc,direction,0);
            this.part2 = part2;
            if (previousSteps.isEmpty()) {
                this.previousSteps = new HashSet<>();
            } else {
                this.previousSteps = new HashSet<>(previousSteps);
            }
            this.previousSteps.add(previous);
            this.hashCode = Objects.hash(location, this.direction, this.previousSteps);
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
            WalkingPathState other = (WalkingPathState) o;
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
        @Override
        public Stream<WalkingPathState> getNextNodes(
                Object[][] grid, BiFunction<SearchStateNode, Coord2D, Boolean> func,
                Set<SearchStateNode> visited,
                Map<String, Long> cost
        ) {
            Coord2D current = new Coord2D(location.x, location.y);
            List<Coord2D> nextSteps = part2 ? POSSIBLE_NEXT_STEPS : filter.get(grid[current.x][current.y]);
            return nextSteps.stream()
                    .filter(nxt -> {
                        Coord2D tmp = location.add(nxt);
                        if (inGrid(tmp, grid)) {
                            return !grid[tmp.x][tmp.y].equals('#');
                        }
                        return false;
                    })
                    .map(c -> new WalkingPathState(location.add(c), c, previousSteps, location, part2))
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

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 23, false, 0);
        char[][] grid = convertToCharGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        System.out.println("Starting Part 2");
        //part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Long solutionPart1(char[][] grid, boolean part2) {
        Character newGrid[][] = new Character[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(grid.length-1,grid[0].length-2);
        WalkingPathState startingState = new WalkingPathState(startingPoint, STARTING_DIRECTION, new HashSet<>(), part2);
        return new PathSearch().findLongestPath(startingState,
                (walkingState) -> walkingState.location.equals(endingPoint),
                (crucibleState, nextLoc) -> true,
                newGrid);

    }
}
