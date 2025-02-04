package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    Integer getDepth(String line) {
        Pattern pattern = Pattern.compile("(?<depth>\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group("depth"));
        }
        return null;
    }

    Coord2D getTarget(String line) {
        Pattern pattern = Pattern.compile("(?<xCoord>\\d+),(?<yCoord>\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return new Coord2D(Integer.parseInt(matcher.group("xCoord")), Integer.parseInt(matcher.group("yCoord")));
        }
        return null;
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,22,false,0);
        Integer depth = getDepth(lines.get(0));
        Coord2D target = getTarget(lines.get(1));
        createMaze(depth, target);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(target);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "Not yet implemented"; //solutionPart2(target, depth);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    //1100 too high
    //1083 too high
    public void createMaze(Integer depth, Coord2D target) {
        geologicIndex = new HashMap<>();
        erosionLevel = new HashMap<>();
        riskLevel = new HashMap<>();
        for (Integer y = 0; y < 1600; y++) {
            for (Integer x = 0; x < 1600; x++) {
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
