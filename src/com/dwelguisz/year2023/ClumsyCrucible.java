package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.graph.transversal.PathSearch;
import com.dwelguisz.utilities.graph.transversal.SearchStateNode;

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

    public class CrucibleState extends SearchStateNode {

        private int hashCode;

        public CrucibleState(Coord2D location, Coord2D direction, Integer currentSteps) {
            super(location, direction, currentSteps);
            this.hashCode = Objects.hash(location, direction, currentSteps);
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
            CrucibleState other = (CrucibleState) o;
            return (this.location.equals(other.location) && this.direction.equals(other.location)
                    && this.subInfo.equals(other.subInfo));
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public Stream<CrucibleState> getNextNodes(
                Object[][] grid, BiFunction<SearchStateNode, Coord2D, Boolean> func,
                Set<SearchStateNode> visited,
                Map<String, Long> cost
                ) {
            return POSSIBLE_NEXT_STEPS.stream()
                    .filter(nxd -> !nxd.equals(STARTING_DIRECTION) &&
                            !direction.multiply(-1).equals(nxd) &&
                            func.apply(this, nxd) &&
                            inGrid(grid, location.add(nxd))
                    )
                    .map(p -> {
                        Coord2D nextLoc = location.add(p);
                        Integer steps = direction.equals(p) ? subInfo + 1 : 1;
                        visited.add(this);
                        return new CrucibleState(nextLoc, p,steps);}
                    )
                    .filter(s -> !cost.containsKey(s.toString()));
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 17, false, 0);
        char[][] grid = convertToCharGrid(lines);
        Character[][] newGrid = new Character[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(newGrid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(newGrid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Long solutionPart1(Character[][] grid) {
        return new PathSearch().findShortestPath(
                new CrucibleState(new Coord2D(0,0), STARTING_DIRECTION, 1),
                (crucibleState) -> crucibleState.location.equals(new Coord2D(grid.length-1, grid[0].length-1)),
                (crucibleState, nextLoc) -> crucibleState.subInfo <= 3,
                grid
        );
    }

    Long solutionPart2(Character[][] grid) {
        return new PathSearch().findShortestPath(
                new CrucibleState(new Coord2D(0,0), STARTING_DIRECTION, 1),
                (crucibleState) -> crucibleState.location.equals(new Coord2D(grid.length-1, grid[0].length-1)),
                (crucibleState, nextLoc) -> (crucibleState.subInfo <= 10) &&
                        (crucibleState.direction.equals(nextLoc) ||
                                (crucibleState.subInfo >= 4) ||
                crucibleState.direction.equals(STARTING_DIRECTION)),
                grid
        );
    }
}
