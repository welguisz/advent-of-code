package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TwoStepsForward extends AoCDay {

    public static class Node {
        List<String> UNLOCKED_COMBOS = List.of("b", "c", "d", "e", "f");
        public String baseStr;
        public String steps;
        Integer x;
        public Integer y;
        public Integer targetX;
        public Integer targetY;
        public Integer sizeX;
        public Integer sizeY;
        public Integer distanceToTarget;
        public Node(String baseStr, String steps, Integer x, Integer y, Integer targetX, Integer targetY, Integer sizeX, Integer sizeY) {
            this.baseStr = baseStr;
            this.steps = steps;
            this.x = x;
            this.y = y;
            this.targetX = targetX;
            this.targetY = targetY;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            distanceToTarget = (targetX-x)*(targetX-x) + (targetY-y)*(targetY-y);
        }

        public Integer getSteps() {
            return steps.length();
        }

        public List<Node> getNextNodes() {
            Boolean[] validNextSteps = whichDoorsAreOpen();
            List<Node> newNodes = new ArrayList<>();
            if (validNextSteps[0]) {
                newNodes.add(new Node(baseStr, steps+"U",x-1,y,targetX,targetY,sizeX,sizeY));
            }
            if (validNextSteps[1]) {
                newNodes.add(new Node(baseStr, steps+"D",x+1,y,targetX,targetY,sizeX,sizeY));
            }
            if (validNextSteps[2]) {
                newNodes.add(new Node(baseStr, steps+"L",x,y-1,targetX,targetY,sizeX,sizeY));
            }
            if (validNextSteps[3]) {
                newNodes.add(new Node(baseStr, steps+"R",x,y+1,targetX,targetY,sizeX,sizeY));
            }
            return newNodes;
        }
        public String seesWhichDoorsOpen() {
            String str = baseStr + steps;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] md5sum = md.digest(str.getBytes());
                return String.format("%032X", new BigInteger(1, md5sum)).toLowerCase().substring(0,4);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Bad algorithm selected");
            }
            return "0000";
        }


        public Boolean isDoorOpen(String dir) {
            return UNLOCKED_COMBOS.contains(dir);
        }

        public Boolean[] whichDoorsAreOpen() {
            String stepResult = seesWhichDoorsOpen();
            Boolean doorsOpen[] = new Boolean[]{
                    isDoorOpen(stepResult.substring(0,1)) && isNextLocationValid(-1,0),
                    isDoorOpen(stepResult.substring(1,2)) && isNextLocationValid(1,0),
                    isDoorOpen(stepResult.substring(2,3)) && isNextLocationValid(0,-1),
                    isDoorOpen(stepResult.substring(3,4)) && isNextLocationValid(0,1)
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
            if (nextX >= sizeX) {
                return false;
            }
            if (nextY >= sizeY) {
                return false;
            }
            return true;
        }

    }
    public static String TEST_STRING0 = "ihgpwlah"; //370
    public static String TEST_STRING1 = "kglvqrro"; //492
    public static String TEST_STRING2 ="ulqzkmiv"; //830
    public static String BASE_STRING = "pgflpeqp";
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,17,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0), 3, 3, 4, 4);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines.get(0), 3, 3, 4, 4);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public String solutionPart1(String baseStr, Integer targetX, Integer targetY, Integer sizeX, Integer sizeY) {
        PriorityQueue<Node> queue = new PriorityQueue<>(20,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    if (diffLength == 0) {
                        return a.distanceToTarget - b.distanceToTarget;
                    }
                    return diffLength;
                }
                );
        Node initialLoc = new Node(baseStr,"",0,0,targetX,targetY,sizeX,sizeY);
        queue.add(initialLoc);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if (currentNode.onTarget()) {
                return currentNode.steps;
            }
            List<Node> nextNodes = currentNode.getNextNodes();
            for (Node nextNode : nextNodes) {
                queue.add(nextNode);
            }
        }
        return "Bad";
    }

    public Integer solutionPart2(String baseStr, Integer targetX, Integer targetY, Integer sizeX, Integer sizeY) {
        PriorityQueue<Node> queue = new PriorityQueue<>(20,
                (a,b) -> {
                    Integer diffLength = a.getSteps() - b.getSteps();
                    if (diffLength == 0) {
                        return a.distanceToTarget - b.distanceToTarget;
                    }
                    return diffLength;
                }
        );
        Node initialLoc = new Node(baseStr,"",0,0,targetX,targetY,sizeX,sizeY);
        queue.add(initialLoc);
        String currentSteps = "";
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if (currentNode.onTarget()) {
                currentSteps = currentNode.steps;
                continue;
            }
            List<Node> nextNodes = currentNode.getNextNodes();
            for (Node nextNode : nextNodes) {
                queue.add(nextNode);
            }
        }
        return currentSteps.length();
    }


}
