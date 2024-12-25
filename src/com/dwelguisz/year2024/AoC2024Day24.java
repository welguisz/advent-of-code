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
        part2Answer = solutionPart2(new HashMap<>(values), new HashMap<>(circuit), lines);
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

    String solutionPart2(Map<String, Integer> values, Map<String, Pair<Pair<String, String>, String>> circuit, List<String> lines) {
        long xValue = createNumber(values, "x");
        long yValue = createNumber(values, "y");
        //This is where you mess with
        //swapOutputs(circuit, "z06", "hwk");
        swapOutputs(circuit, "tnt", "qmd");
        swapOutputs(circuit, "z31", "hpc");
        swapOutputs(circuit, "z37", "cgr");
        long expectedValue = xValue + yValue;
        long currentAnswer = solutionPart1(new HashMap<>(values), new HashMap<>(circuit));
        long notEqualBits = expectedValue ^ currentAnswer;
        if (notEqualBits == 0L) {
            return List.of("z06","hwk","tnt","qmd","z31","hpc","z37","cgr").stream().sorted().collect(Collectors.joining(","));
        }
        int firstBitWrong = findFirstBitWrong(notEqualBits);
        List<Integer> bitsToCheck = new ArrayList<>();
        long temp = notEqualBits;
        while (temp > 0) {
            int wrongBit = findFirstBitWrong(temp);
            bitsToCheck.add(wrongBit);
            temp &= ~createMask(wrongBit,wrongBit+1);
        }
        List<Pair<Integer,Integer>> bitsToLookAt = new ArrayList<>();
        int bitTemp = -3;
        int bitStart = 0;
        int bitEnd = -1;
        for(int i = 0; i < bitsToCheck.size(); i++) {
            if (bitsToCheck.get(i) - bitTemp > 1) {
                if (bitEnd > 0) {
                    bitsToLookAt.add(Pair.of(bitStart, bitEnd));
                }
                bitStart = bitsToCheck.get(i);
            } else {
                bitEnd = bitsToCheck.get(i);
            }
            bitTemp = bitsToCheck.get(i);
        }
        bitsToLookAt.add(Pair.of(bitStart, bitEnd));

        List<List<Pair<String, String>>> possibleGates = new ArrayList<>();
        for (int i = 0; i < bitsToLookAt.size(); i++) {
            Pair<Integer, Integer> bit = bitsToLookAt.get(i);
            int previousBitEnd = i == 0 ? 0 : bitsToLookAt.get(i-1).getRight();
            int nextBitStart = (i == bitsToLookAt.size()-1) ? 64 : bitsToLookAt.get(i+1).getLeft();
            int bitDiff = bit.getRight() - bit.getLeft();
            bitStart = bit.getLeft();
            possibleGates.add(findSwitchedWrites(circuit, bitStart, bitDiff, previousBitEnd, nextBitStart, expectedValue));
        }


        //        swapOutputs("z06", "hwk");
//        swapOutputs("tnt", "qmd");
//        swapOutputs("z31","hpc");
//        swapOutputs("z37","cgr");
        return "to be implemented";
    }

    String createString(Pair<String, String> g, Pair<String, String> g1, Pair<String, String> g2, Pair<String, String> g3) {
        List<String> tmp = new ArrayList<>();
        tmp.add(g.getLeft());
        tmp.add(g1.getLeft());
        tmp.add(g2.getLeft());
        tmp.add(g3.getLeft());
        tmp.add(g.getRight());
        tmp.add(g1.getRight());
        tmp.add(g2.getRight());
        tmp.add(g3.getRight());
        return tmp.stream().sorted().collect(Collectors.joining(","));
    }

    List<Pair<String, String>> findSwitchedWrites(
            Map<String, Pair<Pair<String,String>,String>> graph,
            int currentBit,
            int bitLength,
            int previousBitEnd,
            int nextBitStart,
            long expectedValue
    ) {
        System.out.println("Working on bit: " + currentBit);
        Pair<Integer,Set<String>> interestingWires1 = findInterestingWires(
                graph,
                String.format("z%2d", currentBit+1).replaceAll(" ", "0"),
                4
        );
        System.out.println("-----");
        Pair<Integer, Set<String>> nextInterestingWires =
                findInterestingWires(
                        graph,
                        String.format("z%2d", currentBit).replaceAll(" ", "0"),
                        2);
        Set<String> interestingWires = new HashSet<>();
        interestingWires.addAll(interestingWires1.getRight());
        interestingWires.addAll(nextInterestingWires.getRight());
        Collection<List<String>> combinations = combinations(new ArrayList<>(interestingWires), 2);
        List<Pair<String, String>> interestingPairs = new LinkedList<>();
        for (List<String> check : combinations) {
            swapOutputs(graph, check.get(0), check.get(1));
            //System.out.println("Swapping " + check.get(0) + " to " + check.get(1));
            if (check.get(0).equals("z06") && check.get(1).equals("hwk")) {
                System.out.println("Stop here");
            }
            long currentAnswer = solutionPart1(new HashMap<>(values), new HashMap<>(graph));
            if (currentAnswer == -1) {
                swapOutputs(graph, check.get(0), check.get(1));
                continue;
            }
            long notEqualBits = (expectedValue ^ currentAnswer) & createMask(previousBitEnd, nextBitStart);
            if (notEqualBits == 0L) {
                interestingPairs.add(Pair.of(check.get(0), check.get(1)));
            }
            swapOutputs(graph, check.get(0), check.get(1));
        }
        System.out.println("--------------");
        return interestingPairs;
    }

    Pair<Integer, Set<String>> findInterestingWires(Map<String, Pair<Pair<String, String>, String>> gates, String output, int level) {
        Set<Pair<String, Integer>> visited = new HashSet<>();
        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        queue.add(Pair.of(output, 0));
        while (!queue.isEmpty()) {
            Pair<String, Integer> current = queue.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            Pair<Pair<String, String>, String> ops = gates.get(current.getLeft());
            String operand1 = ops.getLeft().getLeft();
            String operand2 = ops.getLeft().getRight();
            String operation = ops.getRight();
            System.out.println(operation.toLowerCase()+"("+current.getLeft()+", " +operand1+", "+operand2+");");
            if (!values.containsKey(operand1) && level > current.getRight() + 1) {
                queue.add(Pair.of(operand1, current.getRight() + 1));
            }
            if (!values.containsKey(operand2) && level > current.getRight() + 1) {
                queue.add(Pair.of(operand2, current.getRight() + 1));
            }
        }
        Set<String> outputs = visited.stream().map(Pair::getLeft).collect(Collectors.toSet());
        Integer max = visited.stream().map(Pair::getRight).max(Integer::compareTo).get();
        return Pair.of(max, outputs);
    }

    long createMask(int start, int end) {
        Long temp = 0L;
        for (int i = 0; i < start; i++) {
            temp &= ~(1L << i);
        }
        for (int i = start; i < end; i++) {
            temp |= 1L << i;
        }
        for (int i = end; i < 64; i++) {
            temp &= ~(1L << i);
        }
        return temp;
    }

    int findFirstBitWrong(long x) {
        for (int i = 0; i < 64; i++) {
            long tmp = (x & (1L << i));
            if (tmp != 0L) {
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
