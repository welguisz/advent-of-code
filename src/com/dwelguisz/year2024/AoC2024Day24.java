package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.invoke.SwitchPoint;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class AoC2024Day24 extends AoCDay {

    Map<String, Integer> values;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 24, false, 0);
        Map<String, Pair<Pair<String, String>, String>> circuit = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(new HashMap<>(values), new HashMap<>(circuit));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(new HashMap<>(values), new HashMap<>(circuit));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Map<String, Pair<Pair<String, String>, String>> parseLines(List<String> lines) {
        values = new HashMap<>();
        Map<String, Pair<Pair<String, String>, String>> gates = new HashMap<>();
        boolean beforeSplit = true;
        for (String line : lines) {
            if (line.isEmpty()) {
                beforeSplit = false;
            } else if (beforeSplit) {
                String[] split = line.split(":\\s+");
                values.put(split[0], Integer.parseInt(split[1]));
            } else {
                String[] splits = line.split("\\s+");
                gates.put(splits[4], Pair.of(Pair.of(splits[0], splits[2]), splits[1]));
            }
        }
        return gates;
    }

    long solutionPart1(Map<String, Integer> values, Map<String, Pair<Pair<String, String>, String>> gates) {
        while (!gates.isEmpty()) {
            Map<String, Pair<Pair<String,String>,String>> newGates = new HashMap<>();
            for(Map.Entry<String, Pair<Pair<String, String>, String>> gate : gates.entrySet()) {
                if (gate.getValue() == null) {
                    return Long.MAX_VALUE;
                }
                Pair<String, String> inputs = gate.getValue().getLeft();
                String operation = gate.getValue().getRight();
                String result = gate.getKey();
                if (values.containsKey(inputs.getLeft()) && values.containsKey(inputs.getRight())) {
                    if (inputs.equals(Pair.of("bqk", "frj"))) {
                        System.out.println("stop here");
                    }
                    if (operation.equals("XOR")) {
                        values.put(result, values.get(inputs.getLeft()) ^ values.get(inputs.getRight()));
                    } else if (operation.equals("AND")) {
                        values.put(result, values.get(inputs.getLeft()) & values.get(inputs.getRight()));
                    } else if (operation.equals("OR")) {
                        values.put(result, values.get(inputs.getLeft()) | values.get(inputs.getRight()));
                    }
                } else {
                    newGates.put(gate.getKey(), gate.getValue());
                }
            }
            if (gates.size() == newGates.size()) {
                return -1;
            }
            gates = new HashMap<>(newGates);
        }
        return createNumber(values, "z");
    }

    Long createNumber(Map<String, Integer> values, String startsWith) {
        Map<Integer, Integer> bits = values.entrySet().stream()
                .filter(e -> e.getKey().startsWith(startsWith))
                .collect(Collectors.toMap(e -> Integer.parseInt(e.getKey().substring(startsWith.length())), e -> e.getValue()));
        return bits.entrySet().stream()
                .map(e -> ((long) e.getValue() <<e.getKey()) )
                .reduce(0L, Long::sum);
    }

    Map<String, Pair<Pair<String, String>, String>> swapOutputs(Map<String, Pair<Pair<String, String>, String>> gates, Pair<String,String> output) {
        return swapOutputs(gates, output.getLeft(), output.getRight());
    }

    Map<String, Pair<Pair<String, String>, String>> swapOutputs(Map<String, Pair<Pair<String, String>, String>> gates, String output1, String output2) {
        Pair<Pair<String,String>,String> input1 = gates.get(output1);
        gates.put(output1, gates.get(output2));
        gates.put(output2, input1);
        return gates;
    }

    String solutionPart2(Map<String, Integer> values, Map<String, Pair<Pair<String, String>, String>> circuit) {
        long xValue = createNumber(values, "x");
        long yValue = createNumber(values, "y");
        long expectedValue = xValue + yValue;
        long currentAnswer = solutionPart1(new HashMap<>(values), new HashMap<>(circuit));
        long notEqualBits = expectedValue ^ currentAnswer;
        int firstBitWrong = findFirstBitWrong(notEqualBits);
        CircuitState initialState = new CircuitState(Pair.of("z00","z00"), firstBitWrong, new ArrayList<>(), circuit, values);
        PriorityQueue<CircuitState> queue = new PriorityQueue<>(100, (a,b) -> b.currentBit - a.currentBit);
        queue.add(initialState);
        Set<Integer> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            CircuitState currentState = queue.poll();
            long testAnswer = solutionPart1(new HashMap<>(currentState.values), new HashMap<>(currentState.graph));
            long ne = expectedValue ^ testAnswer;
            if (ne == 0L) {
                return String.join(",", currentState.getWires());
            }
            if (visited.contains(currentState.currentBit)) {
                continue;
            }
            visited.add(currentState.currentBit);
            queue.addAll(currentState.nextStates(expectedValue));
        }
//        swapOutputs("z06", "hwk");
//        swapOutputs("tnt", "qmd");
//        swapOutputs("z31","hpc");
//        swapOutputs("z37","cgr");
        return "to be implemented";
    }

    @Value
    class CircuitState {
        Pair<String, String> underInspection;
        int currentBit;
        List<Pair<String, String>> changedWires;
        Map<String, Pair<Pair<String, String>, String>> graph;
        Map<String, Integer> values;

        List<String> getWires() {
            List<String> wires = new ArrayList<>();
            for(Pair<String, String> wire : changedWires) {
                wires.add(wire.getLeft());
                wires.add(wire.getRight());
            }
            return wires;
        }

        List<CircuitState> nextStates(long expectedValue) {
            for (Pair<String, String> wire : changedWires) {
                swapOutputs(graph, wire.getLeft(), wire.getRight());
            }
            long currentAnswer = solutionPart1(new HashMap<>(values), new HashMap<>(graph));
            long notEqualBits = expectedValue ^ currentAnswer;
            int firstBitWrong = findFirstBitWrong(notEqualBits);
            if (firstBitWrong < currentBit) {
                return List.of();
            }
            List<Pair<Pair<String,String>,Integer>> possibleValues = findSwitchedWrites(expectedValue);
            List<CircuitState> nextStates = new ArrayList<>();
            for (Pair<Pair<String,String>,Integer> v : possibleValues) {
                List<Pair<String,String>> newPath = new ArrayList<>(changedWires);
                newPath.add(v.getLeft());
                nextStates.add(new CircuitState(v.getLeft(), v.getRight(), newPath, new HashMap<>(graph), new HashMap<>(values)));
            }
            return nextStates;
        }

        List<Pair<Pair<String, String>, Integer>> findSwitchedWrites(long expectedValue) {
            Set<String> interestingWires = findInterestingWires(graph, String.format("z%2d", currentBit).replaceAll(" ", "0"), 4);
            interestingWires.addAll(findInterestingWires(graph, String.format("z%2d", currentBit - 1).replaceAll(" ", "0"), 4));
            Collection<List<String>> combinations = combinations(new ArrayList<>(interestingWires), 2);
            List<Pair<Pair<String, String>,Integer>> interestingPairs = new LinkedList<>();
            for (List<String> check : combinations) {
                swapOutputs(graph, check.get(0), check.get(1));
                //System.out.println("Swapping " + check.get(0) + " to " + check.get(1));
                if (check.get(0).equals("z06") && check.get(1).equals("qws")) {
                    //System.out.println("Stop here");
                }
                long currentAnswer = solutionPart1(new HashMap<>(values), new HashMap<>(graph));
                if (currentAnswer == -1) {
                    //System.out.println(" --- Failed. Got into a loop");
                    swapOutputs(graph, check.get(0), check.get(1));
                    continue;
                }
                long notEqualBits = expectedValue ^ currentAnswer;
                int firstBitWrong = findFirstBitWrong(notEqualBits);
                if (firstBitWrong > currentBit) {
                    interestingPairs.add(Pair.of(Pair.of(check.get(0), check.get(1)), firstBitWrong));
                }
                swapOutputs(graph, check.get(0), check.get(1));
            }
            return interestingPairs;

        }

        Set<String> findInterestingWires(Map<String, Pair<Pair<String, String>, String>> gates, String output, int level) {
            Set<String> visited = new HashSet<>();
            Queue<Pair<String, Integer>> queue = new LinkedList<>();
            queue.add(Pair.of(output, 0));
            while (!queue.isEmpty()) {
                Pair<String, Integer> current = queue.poll();
                if (visited.contains(current.getLeft())) {
                    continue;
                }
                visited.add(current.getLeft());
                Pair<Pair<String, String>, String> ops = gates.get(current.getLeft());
                String operand1 = ops.getLeft().getLeft();
                String operand2 = ops.getLeft().getRight();
                if (!values.containsKey(operand1) && level > current.getRight() + 1) {
                    queue.add(Pair.of(operand1, current.getRight() + 1));
                }
                if (!values.containsKey(operand2) && level > current.getRight() + 1) {
                    queue.add(Pair.of(operand2, current.getRight() + 1));
                }
            }
            return visited;
        }
    }

    int findFirstBitWrong(long x) {
        for (int i = 0; i < 64; i++) {
            int tmp = (int) (x & (1L << i));
            if (tmp != 0) {
                return i;
            }
        }
        return -1;
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
