package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class CrossedWires extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2019/day03/input.txt");
        Map<Pair<Integer, Integer>, Map<Integer, List<Integer>>> circuitBoard = createCircuitBoard(lines);
        Integer part1 = solutionPart1(circuitBoard);
        Integer part2 = solutionPart2(circuitBoard);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    public Map<Pair<Integer, Integer>, Map<Integer, List<Integer>>> createCircuitBoard(List<String> lines) {
        Map<Pair<Integer, Integer>, Map<Integer, List<Integer>>> circuitBoard = new HashMap<>();
        Map<String, Pair<Integer, Integer>> deltas = new HashMap<>();
        deltas.put("U",Pair.of(0,1));
        deltas.put("R",Pair.of(1,0));
        deltas.put("D",Pair.of(0,-1));
        deltas.put("L",Pair.of(-1,0));
        Integer lineNumber = 0;
        for (String line : lines) {
            int stepNumber = 1;
            Pair<Integer,Integer> currentPos = Pair.of(0,0);
            List<String> instructions = Arrays.stream(line.split(",")).collect(Collectors.toList());
            for (String instruction : instructions) {
                String direction = instruction.substring(0,1);
                Pair<Integer, Integer> step = deltas.get(direction);
                Integer steps = Integer.parseInt(instruction.substring(1));
                for (int i = 0; i < steps; i++) {
                    currentPos = Pair.of(currentPos.getLeft() + step.getLeft(), currentPos.getRight() + step.getRight());
                    Map<Integer, List<Integer>> value = circuitBoard.getOrDefault(currentPos,new HashMap<>());
                    List<Integer> valueList = value.getOrDefault(lineNumber, new ArrayList<>());
                    valueList.add(stepNumber);
                    value.put(lineNumber, valueList);
                    circuitBoard.put(currentPos,value);
                    stepNumber++;
                }
            }
            lineNumber++;
        }
        return circuitBoard;
    }

    public Integer solutionPart1(Map<Pair<Integer, Integer>, Map<Integer, List<Integer>>> circuitBoard) {
        List<Integer> crossPoints = new ArrayList<>();
        for(Map.Entry<Pair<Integer,Integer>, Map<Integer, List<Integer>>> entry : circuitBoard.entrySet()) {
            if (entry.getValue().size() == 2) {
                crossPoints.add(Math.abs(entry.getKey().getRight()) + Math.abs(entry.getKey().getLeft()));
            }
        }
        return Collections.min(crossPoints);
    }

    public Integer solutionPart2(Map<Pair<Integer, Integer>, Map<Integer, List<Integer>>> circuitBoard) {
        Map<Integer, Integer> stepCount = new HashMap<>();
        for(Map.Entry<Pair<Integer,Integer>, Map<Integer, List<Integer>>> entry : circuitBoard.entrySet()) {
            if (entry.getValue().size() == 2) {
                Integer manhattanDistance = Math.abs(entry.getKey().getRight()) + Math.abs(entry.getKey().getLeft());
                Integer stepCountNow = Collections.min(entry.getValue().get(0)) + Collections.min(entry.getValue().get(1));
                stepCount.put(stepCountNow, manhattanDistance);
            }
        }
        return Collections.min(stepCount.keySet());
    }

}
