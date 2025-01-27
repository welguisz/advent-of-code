package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class AirDuctSpleunking extends AoCDay {

    String[][] map;
    Map<Integer, Pair<Integer,Integer>> numberMap;
    Map<String, Integer> distances;
    List<Pair<Integer, Integer>> numberLocations;
    Set<String> places;
    Integer maxSteps = 0;

    public static class Node {
        Integer x;
        public Integer y;
        public Integer targetX;
        public Integer targetY;
        public Integer distanceToTarget;
        public List<Node> visitedNodes;
        public static Integer FAVORITE_NUMBER = 1358;
        public Node(Integer x, Integer y, Integer targetX, Integer targetY, List<Node> visitedNodes) {
            this.x = x;
            this.y = y;
            this.targetX = targetX;
            this.targetY = targetY;
            this.visitedNodes = visitedNodes;
            distanceToTarget = (targetX-x)*(targetX-x) + (targetY-y)*(targetY-y);
        }

        public Integer getSteps() {
            return visitedNodes.size();
        }

        public List<Node> getNextNodes(String[][] map) {
            Boolean[] validNextSteps = whichDoorsAreOpen(map);
            List<Node> newNodes = new ArrayList<>();
            List<Node> newVisited = new ArrayList<>(visitedNodes);
            newVisited.add(this);
            if (validNextSteps[0]) {
                newNodes.add(new Node(x-1,y,targetX,targetY,newVisited));
            }
            if (validNextSteps[1]) {
                newNodes.add(new Node(x+1,y,targetX,targetY,newVisited));
            }
            if (validNextSteps[2]) {
                newNodes.add(new Node(x,y-1,targetX,targetY,newVisited));
            }
            if (validNextSteps[3]) {
                newNodes.add(new Node(x,y+1,targetX,targetY,newVisited));
            }
            return newNodes;
        }
        public Boolean[] seesWhichDoorsOpen(String[][] map) {
            return new Boolean[]{
                    isDoorOpen(x-1,y, map),
                    isDoorOpen(x+1,y, map),
                    isDoorOpen(x,y-1, map),
                    isDoorOpen(x,y+1, map)
            };
        }
        public static List<String> OPEN_SPACE = List.of("0","1","2","3","4","5","6","7","8","9",".");
        public Boolean isDoorOpen(Integer x, Integer y, String[][] map) {
            return OPEN_SPACE.contains(map[x][y]);
        }

        public Boolean[] whichDoorsAreOpen(String[][] map) {
            Boolean[] stepResult = seesWhichDoorsOpen(map);
            Boolean doorsOpen[] = new Boolean[]{
                    stepResult[0] && haveWeVisitedThisNode(x-1,y),
                    stepResult[1] && haveWeVisitedThisNode(x+1,y),
                    stepResult[2] && haveWeVisitedThisNode(x,y-1),
                    stepResult[3] && haveWeVisitedThisNode(x,y+1)
            };
            return doorsOpen;
        }

        public Boolean onTarget() {
            return distanceToTarget == 0;
        }

        public Boolean haveWeVisitedThisNode(Integer x, Integer y) {
            for(Node visitedNode : visitedNodes) {
                if (visitedNode.x.equals(x) && visitedNode.y.equals(y)) {
                    return false;
                }
            }
            return true;
        }
    }


    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,24,false,0);
        createMap(lines);
        findShortestPathBetweenNumbers();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1() {
        int min = Integer.MAX_VALUE;
        Set<String> tempPlaces = new HashSet<>(places);
        tempPlaces.remove("0");
        for (List<String> permtmp : Collections2.permutations(tempPlaces)) {
            List<String> perm = new ArrayList<>(permtmp);
            perm.add(0,"0");
            int len = 0;
            for (int i = 0; i < perm.size()-1; i++) {
                len += distances.getOrDefault(perm.get(i)+"to"+perm.get(i+1),10000);
            }
            if (len < min) {
                min = len;
            }
        }
        return min;
    }

    public Integer solutionPart2() {
        int min = Integer.MAX_VALUE;
        Set<String> tempPlaces = new HashSet<>(places);
        tempPlaces.remove("0");
        for (List<String> permtmp : Collections2.permutations(tempPlaces)) {
            List<String> perm = new ArrayList<>(permtmp);
            perm.add(0, "0");
            perm.add("0");
            int len = 0;
            for (int i = 0; i < perm.size() - 1; i++) {
                len += distances.getOrDefault(perm.get(i) + "to" + perm.get(i + 1), 10000);
            }
            if (len < min) {
                min = len;
            }
        }
        return min;
    }

    public void findShortestPathBetweenNumbers() {
        places = new HashSet<>();
        distances = new HashMap<>();
        for (int x = 0; x < numberMap.size(); x++) {
            for (int y = x + 1; y < numberMap.size(); y++) {
                Pair<Integer, Integer> startingPoint = numberMap.get(x);
                Pair<Integer, Integer> endPoint = numberMap.get(y);
                Integer shortestDistance = findShortestDistance(startingPoint, endPoint);
                places.add(""+x);
                places.add(""+y);
                if (shortestDistance < maxSteps) {
                    distances.put("" + x + "to" + y, shortestDistance);
                    distances.put("" + y + "to" + x, shortestDistance);
                }
            }
        }
    }

    public Integer findShortestDistance(Pair<Integer, Integer> startingPoint, Pair<Integer, Integer> endPoint) {
        PriorityQueue<Node> queue = new PriorityQueue<>(2000,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    return diffLength;
                }
        );
        Integer queueSize = 0;
        Node initialLoc = new Node(startingPoint.getLeft(), startingPoint.getRight(), endPoint.getLeft(),endPoint.getRight(),new ArrayList<>());
        List<Node> visitedNodes = new ArrayList<>();
        List<Node> currentlyInTheQueue = new ArrayList<>();
        queue.add(initialLoc);
        currentlyInTheQueue.add(initialLoc);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode);
            visitedNodes.add(currentNode);
            if (currentNode.onTarget()) {
                return currentNode.getSteps();
            }
            List<Node> nextNodes = currentNode.getNextNodes(map);
            for (Node nextNode : nextNodes) {
                if (!alreadyInNode(currentlyInTheQueue, nextNode)) {
                    queue.add(nextNode);
                    currentlyInTheQueue.add(nextNode);
                }
            }
        }
        return maxSteps*maxSteps;
    }

    public Boolean alreadyInNode(List<Node> nodes, Node targetNode) {
        for (Node node : nodes) {
            if (node.x.equals(targetNode.x) && node.y.equals(targetNode.y)) {
                return true;
            }
        }
        return false;
    }

    public void createMap(List<String> lines) {
        List<List<String>> tempMap = new ArrayList<>();
        numberLocations = new ArrayList<>();
        for (String line : lines) {
            tempMap.add(Arrays.stream(line.split("")).collect(Collectors.toList()));
        }
        int xSize = tempMap.size();
        int ySize = tempMap.get(0).size();
        map = new String[xSize][ySize];
        numberMap = new HashMap<>();
        for(int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                map[x][y] = tempMap.get(x).get(y);
                if (!map[x][y].equals("#") && !map[x][y].equals(".")) {
                    numberMap.put(Integer.parseInt(map[x][y]),Pair.of(x,y));
                    numberLocations.add(Pair.of(x,y));
                }
                if (!map[x][y].equals("#")) {
                    maxSteps++;
                }
            }
        }
    }
}
