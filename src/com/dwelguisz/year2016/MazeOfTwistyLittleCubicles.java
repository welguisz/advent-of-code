package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class MazeOfTwistyLittleCubicles extends AoCDay {
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

        public List<Node> getNextNodes(List<Node> allVisitedNodes, Boolean part2, Integer maxSteps) {
            if (part2 && (!(getSteps() < maxSteps))) {
                return new ArrayList<>();
            }
            Boolean[] validNextSteps = whichDoorsAreOpen(allVisitedNodes);
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
        public Boolean[] seesWhichDoorsOpen() {
            return new Boolean[]{
                    isDoorOpen(x-1,y),
                    isDoorOpen(x+1,y),
                    isDoorOpen(x,y-1),
                    isDoorOpen(x,y+1)
            };
        }
        public Boolean isDoorOpen(Integer x, Integer y) {
            if (x.equals(1) && y.equals(1)) {
                return true;
            }
            Integer value = x*x + 3*x + 2*x*y + y + y*y + FAVORITE_NUMBER;
            String valueStr = String.format("%s", Integer.toBinaryString(value)).replace(' ', '0');
            Integer sumOnes = Arrays.stream(valueStr.split("")).map(Integer::parseInt).reduce(0,(a,b)->a+b);
            return (sumOnes % 2 == 0);
        }

        public Boolean[] whichDoorsAreOpen(List<Node> alreadyVisited) {
            Boolean[] stepResult = seesWhichDoorsOpen();
            Boolean doorsOpen[] = new Boolean[]{
                    stepResult[0] && isNextLocationValid(-1,0) && haveWeVisitedThisNode(x-1,y, alreadyVisited),
                    stepResult[1] && isNextLocationValid(1,0) && haveWeVisitedThisNode(x+1,y, alreadyVisited),
                    stepResult[2] && isNextLocationValid(0,-1) && haveWeVisitedThisNode(x,y-1, alreadyVisited),
                    stepResult[3] && isNextLocationValid(0,1) && haveWeVisitedThisNode(x,y+1, alreadyVisited)
            };
            return doorsOpen;
        }

        public Boolean onTarget() {
            return distanceToTarget == 0;
        }

        public Boolean isNextLocationValid(int diffX, int diffY) {
            int nextX = x + diffX;
            int nextY = y + diffY;
            if (nextX < 0) {
                return false;
            }
            if (nextY < 0) {
                return false;
            }
            return true;
        }

        public Boolean haveWeVisitedThisNode(Integer x, Integer y, List<Node> alreadyVisited) {
            for(Node visitedNode : visitedNodes) {
                if (visitedNode.x.equals(x) && visitedNode.y.equals(y)) {
                    return false;
                }
            }
            for(Node visitedNode : alreadyVisited) {
                if (visitedNode.x.equals(x) && visitedNode.y.equals(y)) {
                    return false;
                }
            }
            return true;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,13,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(31,39);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(1000, 1000);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(Integer targetX, Integer targetY) {
        PriorityQueue<Node> queue = new PriorityQueue<>(20,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    if (diffLength == 0) {
                        return a.distanceToTarget - b.distanceToTarget;
                    }
                    return diffLength;
                }
        );
        Node initialLoc = new Node(1,1,targetX,targetY,new ArrayList<>());
        List<Node> visitedNodes = new ArrayList<>();
        queue.add(initialLoc);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            visitedNodes.add(currentNode);
            if (currentNode.onTarget()) {
                return currentNode.getSteps();
            }
            List<Node> nextNodes = currentNode.getNextNodes(visitedNodes, false, 50);
            for (Node nextNode : nextNodes) {
                queue.add(nextNode);
            }
        }
        return -1;
    }

    public Integer solutionPart2(Integer targetX, Integer targetY) {
        PriorityQueue<Node> queue = new PriorityQueue<>(20,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    return diffLength;
                }
        );
        Node initialLoc = new Node(1,1,targetX,targetY,new ArrayList<>());
        List<Node> visitedNodes = new ArrayList<>();
        queue.add(initialLoc);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            visitedNodes.add(currentNode);
            List<Node> nextNodes = currentNode.getNextNodes(visitedNodes, true, 50);
            for (Node nextNode : nextNodes) {
                queue.add(nextNode);
            }
        }
        return reducedNodes(visitedNodes).size();
    }

    public List<Node> reducedNodes(List<Node> nodes) {
        List<Node> reduced = new ArrayList<>();
        for (Node node : nodes) {
            if (!alreadyInNode(reduced, node)) {
                reduced.add(node);
            }
        }
        return reduced;
    }

    public String inNodes(List<Node> nodes, Integer x, Integer y, Map<Integer, String> countMap) {
        for (Node node: nodes) {
            if (node.x.equals(x) && node.y.equals(y)) {
                return countMap.get(node.getSteps());
            }
        }
        return " ";
    }

    public Boolean alreadyInNode(List<Node> nodes, Node targetNode) {
        for (Node node : nodes) {
            if (node.x.equals(targetNode.x) && node.y.equals(targetNode.y)) {
                return true;
            }
        }
        return false;
    }
}
