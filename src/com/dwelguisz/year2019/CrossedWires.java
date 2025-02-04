package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class CrossedWires extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,3,false,0);
        Map<Coord2D, Map<Integer, List<Integer>>> circuitBoard = createCircuitBoard(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(circuitBoard);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(circuitBoard);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Map<Coord2D, Map<Integer, List<Integer>>> createCircuitBoard(List<String> lines) {
        Map<Coord2D, Map<Integer, List<Integer>>> circuitBoard = new HashMap<>();
        Map<String, Coord2D> deltas = new HashMap<>();
        deltas.put("U",new Coord2D(0,1));
        deltas.put("R",new Coord2D(1,0));
        deltas.put("D",new Coord2D(0,-1));
        deltas.put("L",new Coord2D(-1,0));
        Integer lineNumber = 0;
        for (String line : lines) {
            int stepNumber = 1;
            Coord2D currentPos = new Coord2D(0,0);
            List<String> instructions = Arrays.stream(line.split(",")).toList();
            for (String instruction : instructions) {
                String direction = instruction.substring(0,1);
                Coord2D step = deltas.get(direction);
                Integer steps = Integer.parseInt(instruction.substring(1));
                for (int i = 0; i < steps; i++) {
                    currentPos = currentPos.add(step);
                    circuitBoard.computeIfAbsent(currentPos, k -> new HashMap<>())
                            .computeIfAbsent(lineNumber, k -> new ArrayList<>()).add(stepNumber);
                    stepNumber++;
                }
            }
            lineNumber++;
        }
        return circuitBoard;
    }

    public Integer solutionPart1(Map<Coord2D, Map<Integer, List<Integer>>> circuitBoard) {
        List<Integer> crossPoints = new ArrayList<>();
        for(Map.Entry<Coord2D, Map<Integer, List<Integer>>> entry : circuitBoard.entrySet()) {
            if (entry.getValue().size() == 2) {
                crossPoints.add(Math.abs(entry.getKey().y) + Math.abs(entry.getKey().x));
            }
        }
        return Collections.min(crossPoints);
    }

    public Integer solutionPart2(Map<Coord2D, Map<Integer, List<Integer>>> circuitBoard) {
        Map<Integer, Integer> stepCount = new HashMap<>();
        for(Map.Entry<Coord2D, Map<Integer, List<Integer>>> entry : circuitBoard.entrySet()) {
            if (entry.getValue().size() == 2) {
                Integer manhattanDistance = Math.abs(entry.getKey().y) + Math.abs(entry.getKey().x);
                Integer stepCountNow = Collections.min(entry.getValue().get(0)) + Collections.min(entry.getValue().get(1));
                stepCount.put(stepCountNow, manhattanDistance);
            }
        }
        return Collections.min(stepCount.keySet());
    }

}
