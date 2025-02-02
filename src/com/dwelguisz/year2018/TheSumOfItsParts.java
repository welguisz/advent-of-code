package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TheSumOfItsParts extends AoCDay {
    public static class Node {
        String id;
        List<Node> beforeSteps;
        List<Node> afterSteps;
        Boolean completed;
        Boolean inProgress;
        Integer delay;
        public Node(String id, Integer delay) {
            this.id = id;
            this.beforeSteps = new ArrayList<>();
            this.afterSteps = new ArrayList<>();
            this.completed = false;
            this.inProgress = false;
            this.delay = (id.charAt(0) - 64)  + delay;
        }

        public void addBefore(Node node) {
            beforeSteps.add(node);
        }

        public void addAfter(Node node) {
            afterSteps.add(node);
        }
    }

    public static class Elf {
        Integer id;
        Node currentNodeWorkingOn;
        Integer timeToComplete;

        public Elf(Integer id) {
            this.id = id;
            currentNodeWorkingOn = null;
            timeToComplete = -1;
        }

        public boolean workDone(Integer time) {
            return time >= timeToComplete;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,7,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines, 5, 60);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public String solutionPart1(List<String> lines) {
        Map<String, Node> nodeMap = createMap(lines, 0);
        String completedSteps = "";
        Boolean completed = false;
        while (!completed) {
            List<String> ableToWorkOn = new ArrayList<>();
            for (Map.Entry<String, Node> node : nodeMap.entrySet()) {
                if (node.getValue().beforeSteps.isEmpty() && !node.getValue().completed) {
                    ableToWorkOn.add(node.getKey());
                }
            }
            ableToWorkOn = ableToWorkOn.stream().sorted().collect(Collectors.toList());
            String nodeToWorkOn = ableToWorkOn.get(0);
            Node currentNode = nodeMap.get(nodeToWorkOn);
            currentNode.completed = true;
            for (Node after : currentNode.afterSteps) {
                after.beforeSteps.remove(currentNode);
            }
            completedSteps += nodeToWorkOn;
            completed = nodeMap.entrySet().stream().map(e -> e.getValue()).allMatch(n -> n.completed);
        }
        return completedSteps;
    }

    public Integer solutionPart2(List<String> lines, Integer elves, Integer delay) {
        Map<String, Node> nodeMap = createMap(lines, delay);
        Boolean completed = false;
        List<Elf> elvesWorking = new ArrayList<>();
        Integer currentTime = 0;
        for (int i = 0; i < elves; i++) {
            elvesWorking.add(new Elf(i));
        }

        while (!completed) {
            List<String> availableNodes = findNodesToWorkOn(nodeMap);
            List<Integer> freeElfs = findFreeElves(elvesWorking, currentTime);
            updateElfsandNodes(freeElfs, elvesWorking, nodeMap, availableNodes, currentTime);
            List<Integer> elvesFinishingWork = findElvesFinishingNodes(elvesWorking, currentTime+1);
            completeWork(elvesFinishingWork, elvesWorking, nodeMap);
            completed = nodeMap.entrySet().stream().map(e -> e.getValue()).allMatch(n -> n.completed);
            currentTime++;
        }
        return currentTime;
    }

    public void updateElfsandNodes(List<Integer> freeElves, List<Elf> allElves, Map<String, Node> nodeMap, List<String> availableNodes, Integer currentTime) {
        while (!freeElves.isEmpty() && !availableNodes.isEmpty()) {
            Integer currentElfId = freeElves.remove(0);
            String currentNodeId = availableNodes.remove(0);
            Elf currentElf = allElves.get(currentElfId);
            Node currentNode = nodeMap.get(currentNodeId);
            currentElf.timeToComplete = currentTime + currentNode.delay;
            currentElf.currentNodeWorkingOn = currentNode;
            currentNode.inProgress = true;
        }
    }

    public List<String> findNodesToWorkOn(Map<String, Node> nodeMap) {
        List<String> ableToWorkOn = new ArrayList<>();
        for (Map.Entry<String, Node> node : nodeMap.entrySet()) {
            if (node.getValue().beforeSteps.isEmpty() && !node.getValue().completed && !node.getValue().inProgress) {
                ableToWorkOn.add(node.getKey());
            }
        }
        return ableToWorkOn.stream().sorted().collect(Collectors.toList());
    }

    public List<Integer> findFreeElves(List<Elf> allElves, final Integer currentTime) {
        return allElves.stream()
                .filter(elf -> elf.workDone(currentTime))
                .map(elf -> elf.id)
                .collect(Collectors.toList());
    }

    public List<Integer> findElvesFinishingNodes(List<Elf> allElves, final Integer currentTime) {
        return allElves.stream()
                .filter(elft -> elft.workDone(currentTime))
                .filter(elft -> elft.currentNodeWorkingOn != null)
                .map(elf -> elf.id)
                .collect(Collectors.toList());
    }

    public void completeWork(List<Integer> elvesDone, List<Elf> allElves, Map<String, Node> nodeMap) {
        for (Integer elfId : elvesDone) {
            Elf currentElf = allElves.get(elfId);
            String nodeId = currentElf.currentNodeWorkingOn.id;
            Node currentNode = nodeMap.get(nodeId);
            currentNode.completed = true;
            currentNode.inProgress = false;
            for (Node after : currentNode.afterSteps) {
                after.beforeSteps.remove(currentNode);
            }
            currentElf.currentNodeWorkingOn = null;

        }
    }

    public Map<String, Node> createMap(List<String> lines, Integer delay) {
        Map<String, Node> nodeMap = new HashMap<>();
        for (String line : lines) {
            String tmp[] = line.split(" ");
            String finishedStep = tmp[1];
            String nextStep = tmp[7];
            Node beforeStep = nodeMap.getOrDefault(finishedStep, new Node(finishedStep, delay));
            Node afterStep = nodeMap.getOrDefault(nextStep, new Node(nextStep, delay));
            beforeStep.addAfter(afterStep);
            afterStep.addBefore(beforeStep);
            nodeMap.put(finishedStep, beforeStep);
            nodeMap.put(nextStep, afterStep);
        }
        return nodeMap;
    }
}
