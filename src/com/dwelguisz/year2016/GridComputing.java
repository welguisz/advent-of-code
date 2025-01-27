package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class GridComputing extends AoCDay {

    public static Long KILOBYTE = 1024L;
    public static Long MEGABYTE = KILOBYTE * KILOBYTE;
    public static Long GIGABYTE = MEGABYTE * KILOBYTE;
    public static Long TERABYTE = GIGABYTE * KILOBYTE;


    public static class Node {
        public String name;
        public Integer x;
        public Integer y;
        public Long size;
        public Long used;
        public Long avail;
        public Integer diskUsagePercentage;

        public Node(String name, Integer x, Integer y, Long size, Long used, Long avail, Integer diskUsagePercentage) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.size = size;
            this.used = used;
            this.avail = avail;
            this.diskUsagePercentage = diskUsagePercentage;
        }

    }

    Map<String, Node> gridNodes;
    List<Pair<Node,Node>> allowedNodes;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,22,false,0);
        createNodes(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
        //215 wrong -- too high
    }

    public Integer solutionPart2() {
        int xSize = gridNodes.entrySet().stream().map(e -> e.getValue()).max(Comparator.comparing(n -> n.x)).get().x;
        int ySize = gridNodes.entrySet().stream().map(e -> e.getValue()).max(Comparator.comparing(n -> n.y)).get().y;
        List<Node> wall = new ArrayList<>();
        Node emptyNode = null;
        Node[][] nodes = new Node[xSize+1][ySize+1];
        gridNodes.entrySet().forEach(n -> nodes[n.getValue().x][n.getValue().y] = n.getValue());
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node n = nodes[x][y];
                if (x == 0 && y == 0) {
                    System.out.print("S");
                } else if (x == xSize && y == 0) {
                    System.out.print("G");
                } else if (n.used == 0L) {
                    emptyNode = n;
                    System.out.print("E");
                } else if (n.size > 250 * TERABYTE) {
                    wall.add(n);
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        Integer minX = wall.stream().map(n -> n.x).min(Integer::compareTo).get() - 1;
        Node wallNode = nodes[minX][wall.get(0).y];
        int result = Math.abs(emptyNode.x - wallNode.x) + Math.abs(emptyNode.y - wallNode.y);
        result += Math.abs(wallNode.x - xSize) + wallNode.y;
        return result + 5 * (xSize - 1);
    }

    public Integer solutionPart1() {
        allowedNodes = new ArrayList<>();
        List<String> gridNames = gridNodes.keySet().stream().collect(Collectors.toList());
        List<List<String>> possiblePairs = combinations(gridNames, 2);
        for (List<String> possiblePair : possiblePairs) {
            allowedNodes.addAll(viablePair(possiblePair));
        }
        return allowedNodes.size();
    }

    public List<Pair<Node,Node>> viablePair(List<String> possiblePair) {
        Node sourceNode = gridNodes.get(possiblePair.get(0));
        Node destinationNode = gridNodes.get(possiblePair.get(1));
        List<Pair<Node,Node>> values = new ArrayList<>();
        if ((sourceNode.used != 0L) && (!sourceNode.name.equals(destinationNode.name)) && (sourceNode.used <= destinationNode.avail)) {
            values.add(Pair.of(sourceNode, destinationNode));
        }
        if ((destinationNode.used != 0L) && (!sourceNode.name.equals(destinationNode.name)) && (destinationNode.used <= sourceNode.avail)) {
            values.add(Pair.of(destinationNode, sourceNode));
        }
        return values;
    }

    public void createNodes(List<String> lines) {
        gridNodes = new HashMap<>();
        for(String line : lines) {
            if (line.contains("gridcenter") || line.contains("Filesystem")) {
                continue;
            }
            String info[] = line.split("\\s{1,}");
            String name = info[0].split("/")[3];
            String tmp[] = name.split("-");
            Integer x = Integer.parseInt(tmp[1].substring(1));
            Integer y = Integer.parseInt(tmp[2].substring(1));
            Long size = getBytes(info[1]);
            Long used = getBytes(info[2]);
            Long avail = getBytes(info[3]);
            Integer diskPercentage = Integer.parseInt(info[4].substring(0,info[4].length()-1));
            gridNodes.put(name, new Node(name,x,y,size,used,avail,diskPercentage));
        }
    }

    public Long getBytes(String size) {
        String tmp = size.substring(size.length()-1);
        Long multipler = 0L;
        if (tmp.equals("K")) {
            multipler = KILOBYTE;
        } else if (tmp.equals("M")) {
            multipler = MEGABYTE;
        } else if (tmp.equals("G")) {
            multipler = GIGABYTE;
        } else if (tmp.equals("T")) {
            multipler = TERABYTE;
        }
        String base = size.substring(0, size.length()-1);
        return multipler * Integer.parseInt(base);
    }

    public List<List<String>> combinations(List<String> inputSet, int k) {
        List<List<String>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0);
        return results;
    }

    public void combinationsInternal(List<String> inputSet, int k, List<List<String>> results, ArrayList<String> accumulator, int index) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;
        if (accumulator.size() == k) {
            results.add(new ArrayList<>(accumulator));
        } else if (needToAccumulate <= canAccumulate) {
            combinationsInternal(inputSet, k, results, accumulator, index + 1);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet,k,results,accumulator,index+1);
            accumulator.remove(accumulator.size()-1);
        }
    }

}
