package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HillClimbingAlgorithm extends AoCDay {

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
    
        public Boolean[] allowedMovement(String[][] map) {
            Integer currentLoc = getHeight(map,x,y);

            Integer up = getHeight(map,x-1,y);
            Integer down = getHeight(map,x+1,y);
            Integer left = getHeight(map,x,y-1);
            Integer right = getHeight(map,x,y+1);
            return new Boolean[]{currentLoc +2 > up, currentLoc + 2 > down, currentLoc + 2 > left, currentLoc + 2 > right};
        }
        
        public Integer getHeight(String[][] map, Integer x, Integer y) {
            if (x < 0 || x >= map.length || y < 0 || y >= map[x].length) {
                return 37;
            }
            String height = map[x][y];
            if (height.equals("S")) {
                return 1;
            } else if (height.equals("E")) {
                return 26;
            } else {
                return Integer.valueOf(height.charAt(0)) - 96;
            }
        }
        
        public List<Node> getNextNodes(String[][] map) {
            Boolean[] validNextSteps = validMoves(map);
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
        public Boolean[] validMoves(String[][] map) {
            Boolean[] stepResult = allowedMovement(map);
            return new Boolean[]{
                    stepResult[0] && haveWeVisitedThisNode(x-1,y),
                    stepResult[1] && haveWeVisitedThisNode(x+1,y),
                    stepResult[2] && haveWeVisitedThisNode(x,y-1),
                    stepResult[3] && haveWeVisitedThisNode(x,y+1)
            };
        }
    
        public Boolean onTarget() {
            return distanceToTarget == 0;
        }
    
        public Boolean haveWeVisitedThisNode(Integer x, Integer y) {
            return !visitedNodes.stream().anyMatch(v -> v.x.equals(x) && v.y.equals(y));
        }
    }


    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day12/input.txt");
        String[][] grid = convertToGrid(lines);
        Integer part1 = solutionPart1(grid);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        Integer part2 = solutionPart2(grid, part1);
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

    Integer solutionPart1(String[][] grid) {
        Pair<Integer, Integer> startingPoint = Pair.of(0,0);
        Pair<Integer, Integer> goalPoint = Pair.of(0,0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].equals("S")) {
                    startingPoint = Pair.of(i,j);
                }
                if (grid[i][j].equals("E")) {
                    goalPoint = Pair.of(i,j);
                }
            }
        }
        return findShortestDistance(startingPoint, goalPoint, grid, Integer.MAX_VALUE);
    }

    Integer solutionPart2(String[][] grid, Integer maxSteps) {
        List<Pair<Integer,Integer>> startingPoints = new ArrayList<>();
        Pair<Integer, Integer> goalPoint = Pair.of(0,0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].equals("S")) {
                    startingPoints.add(Pair.of(i,j));
                }
                if (grid[i][j].equals("a")) {
                    startingPoints.add(Pair.of(i,j));
                }
                if (grid[i][j].equals("E")) {
                    goalPoint = Pair.of(i,j);
                }
            }
        }
        Integer minValue = maxSteps+10;
        System.out.println("Number of starting Points: " + startingPoints.size());
        Integer count = 0;
        for (Pair<Integer, Integer> s : startingPoints) {
            if (count %100 == 0) {
                System.out.println("Trying point #" + count);
            }
            Integer cur = findShortestDistance(s, goalPoint, grid, minValue);
            minValue = Integer.min(minValue, cur);
            count++;
        }
        return minValue;
    }

    public Integer findShortestDistance(Pair<Integer, Integer> startingPoint, Pair<Integer, Integer> endPoint, String map[][], Integer maxSteps) {
        PriorityQueue<Node> queue = new PriorityQueue<>(2000,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    return diffLength;
                }
        );
        Node initialLoc = new Node(startingPoint.getLeft(), startingPoint.getRight(), endPoint.getLeft(),endPoint.getRight(),new ArrayList<>());
        List<Node> visitedNodes = new ArrayList<>();
        List<Node> currentlyInTheQueue = new ArrayList<>();
        queue.add(initialLoc);
        currentlyInTheQueue.add(initialLoc);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            currentlyInTheQueue.remove(currentNode);
            visitedNodes.add(currentNode);
            if (currentNode.getSteps() > maxSteps) {
                continue;
            }
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
        return Integer.MAX_VALUE;
    }

    public Boolean alreadyInNode(List<Node> nodes, Node targetNode) {
        return nodes.stream().anyMatch(n -> n.x.equals(targetNode.x) && n.y.equals(targetNode.y));
    }


}
