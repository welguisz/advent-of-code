package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.graph.transversal.PathSearch;
import com.dwelguisz.utilities.graph.transversal.SearchStateNode;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ALongWalk extends AoCDay {

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
            Set<Coord2D> currentSteps = new HashSet<>(previousSteps);
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
                    .filter(s -> !currentSteps.contains(s.location));
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
        part2Answer = solutionPart1(grid, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Coord2D> findIntersections (Character [][] grid, boolean part2) {
        List<Coord2D> intersections = new ArrayList<>();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                List<Coord2D> options = !part2 ? filter.get(grid[r][c]) : grid[r][c] == '#' ? List.of() : POSSIBLE_NEXT_STEPS;
                int finalR = r;
                int finalC = c;
                long size = options.stream()
                            .map(nxt -> new Coord2D(finalR +nxt.x, finalC +nxt.y))
                        .filter(nxt -> new WalkingPathState(nxt,LEFT,new HashSet<>(),false).inGrid(grid, nxt))
                        .filter(nxt -> grid[nxt.x][nxt.y] != '#')
                        .count();
                if (size > 2l) {
                    intersections.add(new Coord2D(r,c));
                }
            }
        }
        return intersections;
    }

    public class GraphNode {
        Coord2D location;
        Map<Coord2D, Long> edge;

        public GraphNode(Coord2D location) {
            this.location = location;
            this.edge = new HashMap<>();
        }

        public void addNode(Coord2D location, Long stepCount) {
            edge.put(location, stepCount);
        }

        @Override
        public int hashCode(){
            return Objects.hash(location, edge);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            GraphNode other = (GraphNode) o;
            return (this.location.equals(other.location)) && edge.equals(other.edge);
        }
    }

    Map<Coord2D, GraphNode> graph;
    public void buildGraph(char[][] grid, boolean part2) {
        Character newGrid[][] = new Character[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }
        graph = new HashMap<>();
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(grid.length-1,grid[0].length-2);
        List<Coord2D> importantPoints = new ArrayList<>();
        importantPoints.add(startingPoint);
        importantPoints.addAll(findIntersections(newGrid, part2));
        importantPoints.add(endingPoint);
        int currentPoint = 0;
        List<Coord2D> checkedPoints = new ArrayList<>();
        while(checkedPoints.size() < importantPoints.size()) {
            Coord2D importantPoint = importantPoints.get(currentPoint);
            checkedPoints.add(importantPoint);
            WalkingPathState startingState = new WalkingPathState(importantPoint, STARTING_DIRECTION, new HashSet<>(), part2);
            List<Pair<Coord2D,Long>> edges = new PathSearch().findLongestPath(
                    startingState,
                    (walkingState) -> importantPoints.contains(walkingState.location) && !walkingState.location.equals(importantPoint),
                    (walkingState, nextLoc) -> true,
                    newGrid
            );
            GraphNode node = new GraphNode(importantPoint);
            edges.stream().forEach(p -> node.addNode(p.getLeft(), p.getRight()));
            graph.put(importantPoint, node);
            currentPoint++;
        }
    }

    Long solutionPart1(char[][] grid, boolean part2) {
        buildGraph(grid, part2);
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(grid.length-1,grid[0].length-2);
        return walkTheNodes(startingPoint, endingPoint, new ArrayList<>());
    }

    public Long walkTheNodes(Coord2D currentPoint, Coord2D endingPoint, List<Coord2D> visited) {
        if (currentPoint.equals(endingPoint)) {
            return 0L;
        }
        GraphNode currentNode = graph.get(currentPoint);
        List<Coord2D> newVisited = new ArrayList<>(visited);
        newVisited.add(currentPoint);
        List<Long> differentSteps = new ArrayList<>();
        for (Coord2D point : currentNode.edge.keySet()) {
            if (newVisited.contains(point)) {
                continue;
            }
            Long distance = walkTheNodes(point, endingPoint, newVisited);
            if (distance >= 0L) {
                differentSteps.add(distance + currentNode.edge.get(point));
            }
        }
        if (differentSteps.isEmpty()) {
            return -1L;
        }
        Long maxSteps = differentSteps.stream().mapToLong(l -> l).max().getAsLong();
        return maxSteps;
    }
}
