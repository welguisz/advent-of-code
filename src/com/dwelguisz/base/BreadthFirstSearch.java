package com.dwelguisz.base;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class BreadthFirstSearch<T> extends AoCDay{

    T map[][];

    public void setMap(T map[][]) {
        this.map = map;
    }

    public Integer findShortestPath(
            Pair<Integer, Integer> startingPoint,
            Integer maxSteps,
            SearchNode initialNode) {
        PriorityQueue<SearchNode> queue = new PriorityQueue<>(2000,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    return diffLength;
                }
        );
        SearchNode initialLoc = initialNode;
        List<SearchNode> visitedNodes = new ArrayList<>();
        Set<Pair<Integer,Integer>> currentlyInTheQueue = new HashSet<>();
        queue.add(initialLoc);
        currentlyInTheQueue.add(startingPoint);
        while (!queue.isEmpty()) {
            SearchNode currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode);
            visitedNodes.add(currentNode);
            if (currentNode.getSteps() > maxSteps) {
                continue;
            }
            if (currentNode.onTarget()) {
                return currentNode.getSteps();
            }
            List<SearchNode> nextNodes = currentNode.getNextNodes(map);
            for (SearchNode nextNode : nextNodes) {

                if (currentlyInTheQueue.add(nextNode.getLocationPair())) {
                    queue.add(nextNode);
                }
            }
        }
        return Integer.MAX_VALUE;
    }
}
