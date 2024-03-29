package com.dwelguisz.utilities.graph.transversal;

import com.dwelguisz.utilities.Coord2D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public abstract class SearchStateNode {
    public Coord2D location;
    public Coord2D direction;
    public Integer subInfo;
    public Map<String, Long> cost;
    public Set<Coord2D> previousSteps;
    public Coord2D previousDirection;
    public SearchStateNode(Coord2D location, Coord2D direction, Integer subInfo) {
        this(location, direction, subInfo, new Coord2D(-1,-1));
    }

    public SearchStateNode(Coord2D location, Coord2D direction, Integer subInfo, Coord2D previousDirection) {
        this.location = location;
        this.direction = direction;
        this.subInfo = subInfo;
        this.previousDirection = previousDirection;
        this.cost = new HashMap<>();
        this.previousSteps = new HashSet<>();

    }

    public abstract Stream<? extends SearchStateNode> getNextNodes(
            Object[][] grid, BiFunction<SearchStateNode, Coord2D, Boolean> func,
            Set<SearchStateNode> visited,
            Map<String, Long> cost);

    public Stream<? extends SearchStateNode> getBackwardsNodes(
            Object[][] grid,
            BiFunction<SearchStateNode, Coord2D, Boolean> func,
            Map<Coord2D, Long> stepsToEnd,
            Long maxSteps
    ) {
        return Stream.of();
    }

    public boolean inGrid(Object[][] grid, Coord2D loc) {
        Integer r = loc.x;
        Integer c = loc.y;
        return (0 <= r && r < grid.length && 0 <= c && c < grid[0].length);
    }

}
