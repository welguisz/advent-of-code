package com.dwelguisz.base;

import com.dwelguisz.year2022.AoC2022Day16;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BinaryOperator;

public abstract class BreadthFirstSearch<T> extends AoCDay{

    T map[][];

    public void setMap(T map[][]) {
        this.map = map;
    }

    public Integer findShortestPath(
            Pair<Integer, Integer> startingPoint,
            Integer maxSteps,
            SearchNode initialNode) {
        return findShortestPath(List.of(startingPoint), maxSteps, List.of(initialNode));
    }

    public Integer findShortestPath(
        List<Pair<Integer, Integer>> startingPoints,
        Integer maxSteps,
        List<SearchNode> initialNodes
    ) {
        PriorityQueue<SearchNode> queue = new PriorityQueue<>(2000,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    return diffLength;
                }
        );
        for (SearchNode node : initialNodes) {
            queue.add(node);
        }
        List<SearchNode> visitedNodes = new ArrayList<>();
        Set<Pair<Integer,Integer>> currentlyInTheQueue = new HashSet<>();
        for(Pair<Integer, Integer> points : startingPoints) {
            currentlyInTheQueue.add(points);
        }
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


    public Integer findShortestPathAllEqual(
            List<AoC2022Day16.Valve> startingPoints,
            List<SearchNode> initialNodes
    ) {
        Integer minSteps = Integer.MAX_VALUE;
        PriorityQueue<SearchNode> queue = new PriorityQueue<>(2000,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    return diffLength;
                }
        );
        for (SearchNode node : initialNodes) {
            queue.add(node);
        }
        List<SearchNode> visitedNodes = new ArrayList<>();
        Set<String> currentlyInTheQueue = new HashSet<>();
        for(AoC2022Day16.Valve points : startingPoints) {
            currentlyInTheQueue.add(points.name);
        }
        while (!queue.isEmpty()) {
            SearchNode currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode.getName());
            visitedNodes.add(currentNode);
            if (currentNode.onTarget()) {
                minSteps = Integer.min(minSteps, currentNode.visitedNodes.size());
            }
            List<SearchNode> nextNodes = currentNode.getNextNodes(map);
            for (SearchNode nextNode : nextNodes) {

                if (currentlyInTheQueue.add(nextNode.getName())) {
                    queue.add(nextNode);
                }
            }
        }
        return minSteps-1;
    }

    public Integer findBestPathInTimeLimit(
            List<AoC2022Day16.Valve> startingPoints,
            List<SearchNode> initialNodes,
            Comparator<SearchNode> comparisonFunction
    ) {
        Integer minSteps = Integer.MIN_VALUE;
        PriorityQueue<SearchNode> queue = new PriorityQueue<>(2000, comparisonFunction);
        for (SearchNode node : initialNodes) {
            queue.add(node);
        }
        List<SearchNode> visitedNodes = new ArrayList<>();
        Set<String> currentlyInTheQueue = new HashSet<>();
        for(AoC2022Day16.Valve points : startingPoints) {
            currentlyInTheQueue.add(points.name);
        }
        while (!queue.isEmpty()) {
            SearchNode currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode.getName());
            visitedNodes.add(currentNode);
            if (currentNode.onTarget()) {
                minSteps = Integer.max(currentNode.getSteps(), minSteps);
            }
            List<SearchNode> nextNodes = currentNode.getNextNodes(map);
            for (SearchNode nextNode : nextNodes) {

                if (currentlyInTheQueue.add(nextNode.getName())) {
                    queue.add(nextNode);
                }
            }
        }
        return minSteps-1;
    }


}
