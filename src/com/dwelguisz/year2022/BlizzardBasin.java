package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.SearchNode;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class BlizzardBasin extends AoCDay {

    String[][] map;
    List<Set<Coord2D>> freePath;

    public void solve() {
        System.out.println("Day 24 ready to.");
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day24/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        parsedLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1();
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Set<Coord2D> calculateBlizzards(Integer time) {
        Integer yModulo = map.length - 2;
        Integer xModulo = map[0].length - 2;
        Set<Coord2D> blizzards = new HashSet<>();
        for(int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length;x++) {
                if (map[y][x].equals("^")) {
                    blizzards.add(new Coord2D(modulo(y-time-1,yModulo)+1,x));
                } else if (map[y][x].equals("v")) {
                    blizzards.add(new Coord2D(modulo(y+time-1,yModulo)+1,x));
                } else if (map[y][x].equals("<")) {
                    blizzards.add(new Coord2D(y,modulo(x-time-1,xModulo)+1));
                } else if (map[y][x].equals(">")) {
                    blizzards.add(new Coord2D(y,modulo(x+time-1,xModulo)+1));
                }
            }
        }
        return blizzards;
    }

    public Integer modulo(Integer a, Integer b) {
        return (a >= 0) ? a % b : b - (Math.abs(a) % b);
    }

    Set<Coord2D> findFreeSpaces(Set<Coord2D> blizzards) {
        Set<Coord2D> spaces = new HashSet<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length;x++) {
                if (map[y][x].equals("#")) {
                    continue;
                }
                spaces.add(new Coord2D(y,x));
            }
        }
        spaces.removeAll(blizzards);
        return spaces;
    }
    void parsedLines(List<String> lines) {
        map = convertToGrid(lines);
        freePath = new ArrayList<>();
        Integer sx = lines.get(0).indexOf(".");
        Integer ex = lines.get(lines.size()-1).indexOf(".");
        //Blizzards will repeat since they will go in the same direction. So Blizzards going in the vertical
        //positions will repeat after map.length -2 (walls).  Blizzards going in the horizontal direction will
        //repeat after map[0].length -2.  Will create blizzards. After creating the blizzards through time, create
        //a sequence of maps that are available during each time period.
        int maxBlizzards = (lines.size()-2) * (lines.get(0).length()-2);
        for (int i = 0; i < maxBlizzards; i++) {
            Set<Coord2D> blizzardAtTimeI = calculateBlizzards(i);
            freePath.add(findFreeSpaces(blizzardAtTimeI));
        }
    }


    Integer solutionPart1() {
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(map.length-1,map[0].length-2);
        BlizzardSearchNode node = new BlizzardSearchNode(freePath.get(0),startingPoint,endingPoint,0);
        return findShortestPath(300, node);
    }

    Integer solutionPart2() {
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(map.length-1,map[0].length-2);
        BlizzardSearchNode node = new BlizzardSearchNode(freePath.get(0),startingPoint,endingPoint,0);
        Integer time =  findShortestPath(300, node);
        node = new BlizzardSearchNode(freePath.get(time%freePath.size()),endingPoint,startingPoint,time);
        time = findShortestPath(300,node);
        node = new BlizzardSearchNode(freePath.get(time%freePath.size()),startingPoint,endingPoint,time);
        return findShortestPath(300,node);
    }

    public class BlizzardSearchNode extends SearchNode<String> {
        final Set<Coord2D> freeSpace;
        final Coord2D groupLocation;
        final Coord2D targetLocation;
        final Integer time;
        private int hashCode;

        public BlizzardSearchNode (Set<Coord2D> freeSpace, Coord2D groupLocation, Coord2D targetLocation, Integer time) {
            this.freeSpace = freeSpace;
            this.groupLocation = groupLocation;
            this.targetLocation = targetLocation;
            this.time = time;
            this.hashCode = Objects.hash(freeSpace, groupLocation, targetLocation, time);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public String getName() {
            return String.format("Group at %s at time %d", groupLocation.toString(), time);
        }

        @Override
        public List<SearchNode> getNextNodes(String[][] map) {
            return null;
        }

        public List<BlizzardSearchNode> getNextNodes(Set<Coord2D> freeSpace) {
            List<Coord2D> possibleLocs = List.of(groupLocation,
                    groupLocation.add(new Coord2D(-1,0)),
                    groupLocation.add(new Coord2D(0,1)),
                    groupLocation.add(new Coord2D(1,0)),
                    groupLocation.add(new Coord2D(0,-1)));
            possibleLocs = possibleLocs.stream()
                    .filter(n -> freeSpace.contains(n))
                    .collect(Collectors.toList());
            return possibleLocs.stream().map(n -> new BlizzardSearchNode(freeSpace, n, targetLocation, time+1)).collect(Collectors.toList());
        }

        @Override
        public Integer getSteps() {
            return time;
        }

        @Override
        public Boolean onTarget() {
            return groupLocation.equals(targetLocation);
        }

    }
    public Integer findShortestPath(
            Integer maxSteps,
            BlizzardSearchNode initialNode
    ) {
        PriorityQueue<BlizzardSearchNode> queue = new PriorityQueue<>(2000,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    return diffLength;
                }
        );
        queue.add(initialNode);
        Set<BlizzardSearchNode> currentlyInTheQueue = new HashSet<>();
        currentlyInTheQueue.add(initialNode);
        while (!queue.isEmpty()) {
            BlizzardSearchNode currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode);
            if (currentNode.getSteps() > 300) {
                continue;
            }
            if (currentNode.onTarget()) {
                return currentNode.getSteps() + 1;
            }
            List<BlizzardSearchNode> nextNodes = currentNode.getNextNodes(freePath.get((currentNode.getSteps()+1)%freePath.size()));
            for (BlizzardSearchNode nextNode : nextNodes) {
                    queue.add(nextNode);
            }
        }
        return Integer.MAX_VALUE;
    }


}
