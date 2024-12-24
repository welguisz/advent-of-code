package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AoC2024Day24 extends AoCDay {

    Map<String, Integer> values;
    Map<String, Pair<Pair<String, String>, String>> gates;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 24, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void parseLines(List<String> lines) {
        values = new HashMap<>();
        gates = new HashMap<>();
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
    }

    long solutionPart1() {
        Long answer = 0L;
        while (!gates.isEmpty()) {
            Map<String, Pair<Pair<String,String>,String>> newGates = new HashMap<>();
            for(Map.Entry<String, Pair<Pair<String, String>, String>> gate : gates.entrySet()) {
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
            gates = new HashMap<>(newGates);
        }
        return createNumber(values, "z");
    }

    Long createNumber(Map<String, Integer> values, String startsWith) {
        Integer[] bits = new Integer[64];
        for (int i = 0; i < 64; i++) {
            bits[i] = 0;
        }
        for (Map.Entry<String, Integer> value : values.entrySet()) {
            if (value.getKey().startsWith(startsWith)) {
                Integer num = Integer.parseInt(value.getKey().substring(1));
                bits[num] = value.getValue();
            }
        }
        Long answer = 0L;
        for (int i = bits.length-1; i >= 0; i--) {
            answer <<= 1;
            answer |= bits[i] & 0x01;
        }
        return answer;
    }

    void swapOutputs(String output1, String output2) {
        Pair<Pair<String,String>,String> input1 = gates.get(output1);
        gates.put(output1, gates.get(output2));
        gates.put(output2, input1);
    }


    String solutionPart2(List<String> lines) {
        parseLines(lines);
        Long xValue = createNumber(values, "x");
        Long yValue = createNumber(values, "y");
        swapOutputs("z06", "hwk");
        swapOutputs("tnt", "qmd");
        swapOutputs("z31","hpc");
        swapOutputs("z37","cgr");
        long newAnswer = solutionPart1();
        parseLines(lines);
        swapOutputs("z06", "hwk");
        swapOutputs("tnt", "qmd");
        swapOutputs("z31","hpc");
        swapOutputs("z37", "cgr");
        Long expectedValue = xValue + yValue;
        Long possibleTrips = expectedValue ^ newAnswer;

        Long[] bits = new Long[64];
        for (int i = 0; i < 64; i++) {bits[i] = 0L;}
        for (int t = 0; t < 64; t++) {
            bits[t] = possibleTrips & 1;
            possibleTrips >>= 1;
            if (bits[t] == 1) {
                System.out.println("Look at bit " + t);
                printGraph(String.format("z%2d", t).replace(" ", "0"), 4);
                System.out.println("-------");
            }
        }
        return List.of("z06","hwk","tnt","qmd","z31","hpc","z37","cgr").stream().sorted().collect(Collectors.joining(","));
    }

    void printGraph(String output, int level) {
        Set<String> visited = new HashSet<>();
        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        queue.add(Pair.of(output,0));
        while (!queue.isEmpty()) {
            Pair<String,Integer> current = queue.poll();
            if(visited.contains(current.getLeft())) {
                continue;
            }
            visited.add(current.getLeft());
            Pair<Pair<String,String>,String> ops = gates.get(current.getLeft());
            String operand1 = ops.getLeft().getLeft();
            String operand2 = ops.getLeft().getRight();
            String operation = ops.getRight();
            System.out.println(operand1 + " " + operation + " " + operand2 + " -> " + current.getLeft());
            if (!values.containsKey(operand1) && level > current.getRight() + 1) {
                queue.add(Pair.of(operand1, current.getRight()+1));
            }
            if (!values.containsKey(operand2) && level > current.getRight() + 1) {
                queue.add(Pair.of(operand2, current.getRight()+1));
            }
        }
    }

}
