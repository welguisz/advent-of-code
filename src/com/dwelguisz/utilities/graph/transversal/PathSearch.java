package com.dwelguisz.utilities.graph.transversal;

import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PathSearch {

    Set<SearchStateNode> visited;
    Map<String, Long> cost;
    public Long findShortestPath(
            SearchStateNode initialState,
            Function<SearchStateNode, Boolean> endCondition,
            BiFunction<SearchStateNode, Coord2D, Boolean> func,
            Object[][]grid
    ) {
        visited = new HashSet<>();
        cost = new HashMap<>();
        PriorityQueue<SearchStateNode> stateQ = new PriorityQueue<>(2000, (a, b) ->
                Math.toIntExact(cost.get(a.toString()) - cost.get(b.toString()))
        );
        cost.put(initialState.toString(), 0L);
        stateQ.add(initialState);
        while(!stateQ.isEmpty()) {
            SearchStateNode currentState = stateQ.poll();
            if (visited.contains(currentState)) {
                continue;
            }
            if (endCondition.apply(currentState)) {
                return cost.get(currentState.toString());
            }
            Set<SearchStateNode> nextState = currentState.getNextNodes(grid, func, visited, cost).collect(Collectors.toSet());
            Long value = cost.get(currentState.toString());
            nextState.stream().forEach(s -> cost.put(s.toString(), value+Integer.parseInt(""+grid[s.location.x][s.location.y])));
            stateQ.addAll(nextState);
        }
        return -1L;
    }

    public List<Pair<Coord2D, Long>> findLongestPath(
            SearchStateNode initialState,
            Function<SearchStateNode, Boolean> endCondition,
            BiFunction<SearchStateNode, Coord2D, Boolean> func,
            Object[][]grid
    ) {
        visited = new HashSet<>();
        cost = new HashMap<>();
        PriorityQueue<SearchStateNode> stateQ = new PriorityQueue<>(2000, (a,b) -> b.previousSteps.size() - a.previousSteps.size());
        cost.put(initialState.toString(), 0L);
        stateQ.add(initialState);
        Long maxSteps = 0L;
        List<Pair<Coord2D,Long>> importantInfo = new ArrayList<>();
        while(!stateQ.isEmpty()) {
            SearchStateNode currentState = stateQ.poll();
            if (visited.contains(currentState)) {
                continue;
            }
            //visited.add(currentState);
            if (endCondition.apply(currentState)) {
                importantInfo.add(Pair.of(currentState.location, (long) currentState.previousSteps.size()));
                continue;
            }
            Set<SearchStateNode> nextState = currentState.getNextNodes(grid, func, visited, cost)
                    .filter(s -> !grid[s.location.x][s.location.y].equals('#'))
                    .collect(Collectors.toSet());

            stateQ.addAll(nextState);
            visited.add(currentState);
        }
        return importantInfo;
    }
}
