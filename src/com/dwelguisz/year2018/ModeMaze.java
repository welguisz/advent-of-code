package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class ModeMaze extends AoCDay {

    Map<Coord2D, Integer> geologicIndex;
    Map<Coord2D, Integer> erosionLevel;
    Map<Coord2D, Integer> riskLevel;
    public static Coord2D NORTH = new Coord2D(-1,0);
    public static Coord2D EAST = new Coord2D(0,1);
    public static Coord2D SOUTH = new Coord2D(1,0);
    public static Coord2D WEST = new Coord2D(0,-1);
    public static List<Coord2D> NEIGHBORS = List.of(SOUTH, EAST, WEST, NORTH);
    // 0-> rocky // 0,1 -> climbing gear, torch
    // 1-> wet // 0,2 -> climbing gear, nothing
    // 2-> narrow // 1,2 -> torch, nothing
    public static List<List<Integer>> ALLOWED_TOOLS = List.of(List.of(0,1),List.of(0,2),List.of(1,2));
    public static List<String> TOOL = List.of("Climbing gear", "Torch", "Nothing");

    public static class SeenNode {
        Coord2D location;
        Integer tool;
        public final int hashCode;
        public SeenNode(Coord2D location, Integer tool) {
            this.location = location;
            this.tool = tool;
            this.hashCode = Objects.hash(location, tool);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            SeenNode other = (SeenNode) o;
            return (this.location.equals(other.location)) && (this.tool.equals(other.tool));
        }
    }

    public static class ModeMazeNode {
        Coord2D location;
        Integer time;
        Integer tool;
        final int hashCode;


        public ModeMazeNode(Coord2D location, Integer tool, Integer time) {
            this.location = location;
            this.time = time;
            this.tool = tool;
            this.hashCode = Objects.hash(location, tool);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            ModeMazeNode other = (ModeMazeNode) o;
            return (this.location.equals(other.location)) && (this.tool.equals(other.tool));
        }

        List<ModeMazeNode> getNextNodes(Integer depth, Map<Coord2D, Integer> riskLevel, Set<SeenNode> seen) {
            List<ModeMazeNode> nextNodes = new ArrayList<>();
            List<Coord2D> nextLocs = NEIGHBORS.stream()
                    .map(n -> location.add(n))
                    .filter(n -> n.x >= 0)
                    .filter(n -> n.y >= 0)
                    .filter(n -> n.x < depth)
                    .filter(n -> n.y < depth)
                    .collect(Collectors.toList());
            for (Coord2D n : nextLocs) {
                Integer terrian = riskLevel.get(n);
                List<Integer> allowedTools = ALLOWED_TOOLS.get(terrian);
                if (allowedTools.contains(this.tool)) {
                    nextNodes.add(new ModeMazeNode(n, tool, this.time + 1));
                } else {
                    for (Integer tool : allowedTools) {
                        Integer currentRiskLevel = riskLevel.get(location);
                        if (ALLOWED_TOOLS.get(currentRiskLevel).contains(tool)) {
                            nextNodes.add(new ModeMazeNode(n, tool, time + 8));
                        }
                    }
                }
            }
            return nextNodes.stream().filter(n -> !seen.contains(new SeenNode(n.location, n.tool))).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return String.format("%d %s %d", time,location.toString(),this.tool);
        }
    }

    public void solve() {
        Boolean test = false;
        Integer depth = test ? 510 : 4080;
        Coord2D target = test ? new Coord2D(10,10) : new Coord2D (785,14);
        Long parseTime = Instant.now().toEpochMilli();
        createMaze(depth, target);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(target);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(target, depth);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    //1100 too high
    //1083 too high
    public void createMaze(Integer depth, Coord2D target) {
        geologicIndex = new HashMap<>();
        erosionLevel = new HashMap<>();
        riskLevel = new HashMap<>();
        for (Integer y = 0; y < 1100; y++) {
            for (Integer x = 0; x < 1100; x++) {
                Coord2D currentLoc = new Coord2D(y,x);
                if (x.equals(0) && y.equals(0)) {
                    geologicIndex.put(currentLoc,0);
                } else if (x.equals(0)) {
                    geologicIndex.put(currentLoc,y * 48271);
                } else if (y.equals(0)) {
                    geologicIndex.put(currentLoc, x * 16807);
                } else if (y.equals(target.x) && (x.equals(target.y))) {
                    geologicIndex.put(currentLoc,0);
                } else {
                    geologicIndex.put(currentLoc,erosionLevel.get(new Coord2D(y-1,x))*erosionLevel.get(new Coord2D(y,x-1)));
                }
                erosionLevel.put(currentLoc, (geologicIndex.get(currentLoc)+depth) % 20183);
                riskLevel.put(currentLoc, erosionLevel.get(currentLoc) % 3);
            }
        }
    }

    public Long solutionPart1(Coord2D target) {
        Long riskCount = 0L;
        for (Integer y = 0; y <= target.x; y++) {
            for (Integer x = 0; x <= target.y; x++) {
                riskCount += riskLevel.get(new Coord2D(y,x));
            }
        }
        return riskCount;
    }

    public Long solutionPart2(Coord2D target, Integer depth) {
        ModeMazeNode initialNode = new ModeMazeNode(new Coord2D(0,0),1,0);
        PriorityQueue<ModeMazeNode> queue = new PriorityQueue<>(2000,
                (a,b) -> {
                    Integer diffLength = a.time - b.time;
                    return diffLength;
                }
        );
        queue.add(initialNode);
        Set<SeenNode> visitedNodes = new HashSet<>();
        Set<ModeMazeNode> currentlyInTheQueue = new HashSet<>();
        currentlyInTheQueue.add(initialNode);
        Coord2D transpose = new Coord2D(target.y,target.x);
        Integer step = 0;
        while (!queue.isEmpty()) {
            ModeMazeNode currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode);
            if (visitedNodes.contains(new SeenNode(currentNode.location, currentNode.tool))) {
                continue;
            }
            if (step == 100) {
                //break;
            }
            //System.out.println(currentNode.toString());
            visitedNodes.add(new SeenNode(currentNode.location, currentNode.tool));

            if (currentNode.location.equals(transpose)  && currentNode.tool.equals(1)) {
                if (currentNode.tool.equals(1)) {
                    return currentNode.time.longValue();
                } else {
                    queue.add(new ModeMazeNode(currentNode.location, 1, currentNode.time+7));
                    continue;
                }
            }
            List<ModeMazeNode> nextNodes = currentNode.getNextNodes(depth, riskLevel, visitedNodes);
            for (ModeMazeNode nextNode : nextNodes) {
                if (currentlyInTheQueue.add(nextNode)) {
                    queue.add(nextNode);
                }
            }
            step++;
        }
        return -1L;
    }
}
