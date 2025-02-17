package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
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

    Map<Coord2D, MazePoint> caveMap;
    Coord2D currentCaveEndpoint;
    public static Coord2D NORTH = new Coord2D(-1,0);
    public static Coord2D EAST = new Coord2D(0,1);
    public static Coord2D SOUTH = new Coord2D(1,0);
    public static Coord2D WEST = new Coord2D(0,-1);
    public static List<Coord2D> NEIGHBORS = List.of(SOUTH, EAST, WEST, NORTH);
    // 0-> rocky // 0,1 -> climbing gear, torch
    // 1-> wet // 0,2 -> climbing gear, nothing
    // 2-> narrow // 1,2 -> torch, nothing
    public static List<List<Integer>> ALLOWED_TOOLS = List.of(List.of(0,1),List.of(0,2),List.of(1,2));

    @Value
    public static class MazePoint {
        Coord2D coord;
        int geologicIndex;
        int erosionLevel;
        int riskLevel;
    }

    @Value
    public static class ModeMazeNode {
        Coord2D location;
        @EqualsAndHashCode.Exclude int time;
        int tool;
        @EqualsAndHashCode.Exclude List<ModeMazeNode> previousNodes;


        public ModeMazeNode(Coord2D location, int tool, int time, List<ModeMazeNode> previousNodes) {
            this.location = location;
            this.time = time;
            this.tool = tool;
            this.previousNodes = previousNodes;
        }

        List<ModeMazeNode> getNextNodes(int depth, Set<ModeMazeNode> seen, ModeMaze maze, Coord2D target) {
            List<ModeMazeNode> nextNodes = new ArrayList<>();
            List<Coord2D> nextLocs = NEIGHBORS.stream()
                    .map(location::add)
                    .filter(n -> n.x >= 0)
                    .filter(n -> n.y >= 0)
                    .filter(n -> n.x < target.x+50)
                    .filter(n -> n.y < target.y+50)
                    .toList();
            for (Coord2D n : nextLocs) {
                if (!maze.caveMap.containsKey(n)) {
                    System.out.println("Building to " + n);
                    maze.createMaze(depth, n);
                }
                int terrian = maze.caveMap.get(n).riskLevel;
                List<Integer> allowedTools = ALLOWED_TOOLS.get(terrian);
                List<ModeMazeNode> nextPrevious = new ArrayList<>(previousNodes);
                nextPrevious.add(this);
                if (allowedTools.contains(this.tool)) {
                    nextNodes.add(new ModeMazeNode(n, tool, this.time + 1, nextPrevious));
                }
            }
            for (int tool : ALLOWED_TOOLS.get(maze.caveMap.get(location).riskLevel)) {
                List<ModeMazeNode> nextPrevious = new ArrayList<>(previousNodes);
                nextPrevious.add(this);
                if (tool != this.tool) {
                    nextNodes.add(new ModeMazeNode(location, tool, this.time + 7, nextPrevious));
                }
            }
            return nextNodes.stream().filter(n -> !seen.contains(n)).collect(Collectors.toList());
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
        caveMap = new HashMap<>();
        currentCaveEndpoint = new Coord2D(0,0);
        int depth = getDepth(lines.get(0));
        Coord2D target = getTarget(lines.get(1));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(depth, target);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(target, depth);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public MazePoint addMazePoint(int x, int y, int depth, Coord2D target) {
        int geologicIndex = 0;
        if (x == 0 && y == 0) {
            geologicIndex = 0;
        } else if (x == 0) {
            geologicIndex = y * 48271;
        } else if (y == 0) {
            geologicIndex = x * 16807;
        } else if (y == target.y && (x == target.x)) {
            geologicIndex = 0;
        } else {
            geologicIndex = caveMap.get(new Coord2D(x - 1, y)).erosionLevel * caveMap.get(new Coord2D(x, y - 1)).erosionLevel;
        }
        int erosionLevel = (geologicIndex + depth) % 20183;
        int riskLevel = erosionLevel % 3;
        return new MazePoint(new Coord2D(x,y), geologicIndex, erosionLevel, riskLevel);
    }

    //1100 too high
    //1083 too high
    public void createMaze(Integer depth, Coord2D target) {
        for (int y = 0; y <= target.y+50; y++) {
            for (int x = 0; x <= target.x+50; x++) {
                Coord2D currentLoc = new Coord2D(x, y);
                if (!caveMap.containsKey(currentLoc)) {
                    caveMap.put(currentLoc, addMazePoint(x, y, depth, target));
                }
            }
        }
    }

    public Long solutionPart1(int depth, Coord2D target) {
        createMaze(depth, target);
        return caveMap.entrySet().stream()
                .filter(e -> e.getKey().x <= target.x && e.getKey().y <= target.y)
                .mapToLong(e -> e.getValue().riskLevel)
                .sum();
    }

    public Integer solutionPart2(Coord2D target, int depth) {
        ModeMazeNode initialNode = new ModeMazeNode(new Coord2D(0,0),1,0, new ArrayList<>());
        PriorityQueue<ModeMazeNode> queue = new PriorityQueue<>(2000,
                Comparator.comparingInt(a -> a.time)
        );
        queue.add(initialNode);
        Set<ModeMazeNode> visitedNodes = new HashSet<>();
        Set<ModeMazeNode> currentlyInTheQueue = new HashSet<>();
        currentlyInTheQueue.add(initialNode);
        while (!queue.isEmpty()) {
            ModeMazeNode currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode);
            if (visitedNodes.contains(currentNode)) {
                continue;
            }
            visitedNodes.add(currentNode);

            if (currentNode.location.equals(target)) {
                if (currentNode.tool == 1) {
                    return currentNode.time;
                } else {
                    List<ModeMazeNode> previous = new ArrayList<>(currentNode.previousNodes);
                    previous.add(currentNode);
                    queue.add(new ModeMazeNode(currentNode.location, 1, currentNode.time+7,previous));
                    continue;
                }
            }
            List<ModeMazeNode> nextNodes = currentNode.getNextNodes(depth, visitedNodes, this, target);
            queue.addAll(nextNodes);
        }
        return -1;
    }
}
