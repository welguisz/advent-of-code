package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CrossedWires extends AoCDay {

    Map<String, Integer> values;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 24, false, 0);
        Map<String, Pair<Pair<String, String>, String>> circuit = parseLines(lines);
        OutputCircuit[] network = new OutputCircuit[45];
        for (int i = 0; i < 45; i++) {
            network[i] = createCircuit(circuit, String.format("z%2d",i).replaceAll(" ","0"),5);
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(new HashMap<>(values), new HashMap<>(circuit));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(new HashMap<>(values), new HashMap<>(circuit), network);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    @Value
    class Gate {
        String outputWire;
        String inputWire1;
        String inputWire2;
        String operationValue;
        BiFunction<Integer,Integer,Integer> operation;

        public boolean hasInputWire(String inputWire) {
            return inputWire.equals(inputWire1) || inputWire.equals(inputWire2);
        }

        Integer apply(Integer value1, Integer value2) {
            return operation.apply(value1, value2);
        }

        @Override
        public String toString() {
            return inputWire1 + " " + operationValue + " " + inputWire2 + " -> " + outputWire;
        }
    }

    @Value
    class OutputCircuit {
        Integer number;
        String outputWire;
        Map<String, Gate> gates;
        List<Pair<String, String>> gateOrder;

        static List<String> CORRECT_ORDER = List.of("XOR", "OR", "XOR", "AND", "AND", "XOR", "OR", "XOR", "AND");

        public void addGate(String outputWire, Gate gate) {
            gates.put(outputWire, gate);
            gateOrder.add(Pair.of(outputWire, gate.operationValue));
        }

        public Map<String, Gate> gatesWithInputWires(String wire) {
            return gates.entrySet().stream()
                    .filter(g -> g.getValue().hasInputWire(wire))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        //Bit 0 will be very simple()
        public boolean isFirstBit() {
            Map<String, Long> gateCounts = getGateCounts();
            return (gateCounts.getOrDefault("XOR", 0L) == 1L);
        }

        //Half Adder will look like:
        //and(internalWire1,input1[n-1],input2[n-1])
        //xor(internalWire2,input[n],output[n]
        //xor(output[n],internalWire1,internalWire2)
        public boolean isHalfAdder() {
            Map<String, Long> gateCounts = getGateCounts();
            return (gateCounts.getOrDefault("XOR", 0L) == 2L) && (gateCounts.getOrDefault("AND", 0L) == 1L);
        }

        //Full Adder will look like:
        //
        public boolean isFullAdder() {
            Map<String, Long> gateCounts = getGateCounts();
            return (gateCounts.getOrDefault("XOR", 0L) == 3L) &&
                    (gateCounts.getOrDefault("AND", 0L) == 4L) &&
                    (gateCounts.getOrDefault("OR", 0L) == 2L);

        }
        public boolean isFullAdderInOrder() {
            for (int i = 0; i < 7; i++) {
                if (!CORRECT_ORDER.get(i).equals(gateOrder.get(i).getRight())) {
                    if (i == 1) {
                        if (!CORRECT_ORDER.get(i).equals(gateOrder.get(i + 1).getRight())) {
                            return false;
                        }
                    } else if (i == 2) {
                        if (!CORRECT_ORDER.get(i).equals(gateOrder.get(i - 1).getRight())) {
                            return false;
                        }
                    } else if (i == 5) {
                        if (!CORRECT_ORDER.get(i).equals(gateOrder.get(i + 1).getRight())) {
                            return false;
                        }
                    } else if (i == 6) {
                        if (!CORRECT_ORDER.get(i).equals(gateOrder.get(i - 1).getRight())) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }

        public Map<String, Long> getGateCounts() {
            return gates.entrySet().stream()
                    .collect(Collectors.groupingBy(g -> g.getValue().operationValue, Collectors.counting()));

        }

        public boolean secondBitAdder() {
            Map<String, Long> gateCounts = getGateCounts();
            return (gateCounts.getOrDefault("XOR", 0L) == 3L) &&
                    (gateCounts.getOrDefault("AND", 0L) == 3L) &&
                    (gateCounts.getOrDefault("OR", 0L) == 1L);

        }

        public String printNetwork() {
            Set<String> visited = new HashSet<>();
            Queue<String> queue = new LinkedList<>();
            queue.add(outputWire);
            List<String> netlist = new ArrayList<>();
            Pattern pattern = Pattern.compile("([xy])\\d+");
            while (!queue.isEmpty()) {
                String currentWire = queue.poll();
                if (visited.contains(currentWire)) {
                    continue;
                }
                visited.add(currentWire);
                netlist.add(gates.get(currentWire).toString());
                Matcher matcher1 = pattern.matcher(gates.get(currentWire).inputWire1);
                Matcher matcher2 = pattern.matcher(gates.get(currentWire).inputWire2);
                if (!matcher1.matches() && gates.containsKey(gates.get(currentWire).inputWire1)) {
                    queue.add(gates.get(currentWire).inputWire1);
                }
                if (!matcher2.matches() && gates.containsKey(gates.get(currentWire).inputWire2)) {
                    queue.add(gates.get(currentWire).inputWire2);
                }
            }
            return String.join("\n", netlist);
        }
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

    public OutputCircuit createCircuit(Map<String, Pair<Pair<String, String>, String>> gates, String output, int level) {
        Set<String> visited = new HashSet<>();
        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        queue.add(Pair.of(output, 0));

        OutputCircuit circuit = new OutputCircuit(Integer.parseInt(output.substring(1)),output, new HashMap<>(), new ArrayList<>());
        Pattern pattern = Pattern.compile("([xy])\\d+");
        while (!queue.isEmpty()) {
            Pair<String, Integer> current = queue.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current.getLeft());
            Pair<Pair<String, String>, String> ops = gates.get(current.getLeft());
            String operand1 = ops.getLeft().getLeft();
            String operand2 = ops.getLeft().getRight();
            String operation = ops.getRight();
            if (operation.equals("XOR")) {
                Gate newGate = new Gate(current.getLeft(), operand1, operand2,operation, (a,b) -> a ^ b);
                circuit.addGate(current.getLeft(), newGate);
            } else if (operation.equals("AND")) {
                Gate newGate = new Gate(current.getLeft(), operand1, operand2,operation, (a,b) -> a & b);
                circuit.addGate(current.getLeft(), newGate);
            } else if (operation.equals("OR")) {
                Gate newGate = new Gate(current.getLeft(), operand1, operand2,operation, (a,b) -> a | b);
                circuit.addGate(current.getLeft(), newGate);
            }
            Matcher matcher1 = pattern.matcher(operand1);
            Matcher matcher2 = pattern.matcher(operand2);
            if (!matcher1.matches() && level > current.getRight() + 1) {
                queue.add(Pair.of(operand1, current.getRight() + 1));
            }
            if (!matcher2.matches() && level > current.getRight() + 1) {
                queue.add(Pair.of(operand2, current.getRight() + 1));
            }
        }
        return circuit;
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

    Map<String, Pair<Pair<String, String>, String>> swapOutputs(Map<String, Pair<Pair<String, String>, String>> gates, String output1, String output2) {
        Pair<Pair<String,String>,String> input1 = gates.get(output1);
        gates.put(output1, gates.get(output2));
        gates.put(output2, input1);
        return gates;
    }

    String solutionPart2(
            Map<String, Integer> values,
            Map<String, Pair<Pair<String, String>, String>> circuit,
            OutputCircuit[] network
    ) {
        List<OutputCircuit> circuitsToLookAt = Arrays.stream(network)
                .filter(c -> !c.isFirstBit())
                .filter(c -> !c.secondBitAdder())
                .filter(c -> !c.isHalfAdder())
                .filter(c -> !c.isFullAdder())
                .toList();

        List<String> wiresToSwitch = new ArrayList<>();
        while(!circuitsToLookAt.isEmpty()) {
            wiresToSwitch.addAll(fixNetwork(network, circuitsToLookAt.get(0), circuit));
            circuitsToLookAt = Arrays.stream(network)
                    .filter(c -> !c.isFullAdder())
                    .filter(c -> !c.isHalfAdder())
                    .filter(c -> !c.isFirstBit())
                    .filter(c -> !c.secondBitAdder())
                    .toList();

        }
        return wiresToSwitch.stream().sorted().collect(Collectors.joining(","));
    }

    List<String> fixNetwork (
            OutputCircuit[] network,
            OutputCircuit circuit,
            Map<String, Pair<Pair<String, String>, String>> gates
            ) {
        int bitNumber = circuit.getNumber();
        OutputCircuit lsbCircuit = network[bitNumber-1];
        OutputCircuit msbCircuit = network[bitNumber+1];
        OutputCircuit underInspection = lsbCircuit.isFullAdder() ? msbCircuit : lsbCircuit;
        List<String> possibleSwitches = findWires(circuit, underInspection, gates);
        if (possibleSwitches.size() == 2) {
            swapOutputs(gates, possibleSwitches.get(0), possibleSwitches.get(1));
            for (int i = 0; i < 45; i++) {
                network[i] = createCircuit(gates, String.format("z%2d", i).replaceAll(" ", "0"), 5);
            }
            return possibleSwitches;
        }
        underInspection = !lsbCircuit.isFullAdder() ? msbCircuit : lsbCircuit;
        possibleSwitches = findWires(circuit, underInspection, gates);
        return possibleSwitches;
    }

    List<String> findWires(
            OutputCircuit circuit,
            OutputCircuit underInspection,
            Map<String, Pair<Pair<String, String>, String>> gates
    ) {
        Set<String> possibleWires = new HashSet<>();
        possibleWires.addAll(circuit.gates.keySet());
        possibleWires.addAll(underInspection.gates.keySet());
        Collection<List<String>> combinations = combinations(new ArrayList<>(possibleWires), 2);
        List<Pair<String, String>> interestingPairs = new LinkedList<>();
        for (List<String> combination : combinations) {
            String check0 = combination.get(0);
            String check1 = combination.get(1);
            swapOutputs(gates, check0, check1);
            OutputCircuit newCircuit = createCircuit(gates, String.format("z%2d", circuit.getNumber()).replaceAll(" ","0"), 5);
            OutputCircuit underCircuit = createCircuit(gates, String.format("z%2d", underInspection.getNumber()).replaceAll(" ","0"), 5);
            if (newCircuit.isFullAdder() && underCircuit.isFullAdder()) {
                interestingPairs.add(Pair.of(check0, check1));
            }
            swapOutputs(gates, check0, check1);
        }
        if (interestingPairs.size() == 1) {
            return List.of(interestingPairs.get(0).getLeft(), interestingPairs.get(0).getRight());
        } else if (interestingPairs.size() > 1) {
            return compareMoreCircuits(circuit, gates, interestingPairs);
        }
        System.out.println("Unable to find pairs to switch");
        return new ArrayList<>();
    }

    List<String> compareMoreCircuits(
            OutputCircuit circuit,
            Map<String, Pair<Pair<String, String>, String>> gates,
            List<Pair<String, String>> possibleWires
    ) {
        int bitNumber = circuit.getNumber();
        List<Pair<String, String>> stillInterestingPairs = new ArrayList<>();
        int upperLimit = bitNumber + 3;
        for(Pair<String,String> wires : possibleWires) {
            swapOutputs(gates, wires.getLeft(), wires.getRight());
            OutputCircuit[] newNetwork = new OutputCircuit[upperLimit];
            for (int i = 0; i < upperLimit; i++) {
                newNetwork[i] = createCircuit(gates, String.format("z%2d", i).replaceAll(" ", "0"), 5);
            }
            List<OutputCircuit> lookMore = Arrays.stream(newNetwork)
                    .filter(oc -> oc.number > bitNumber-2)
                    .filter(oc -> oc.number < upperLimit)
                    .filter(oc -> !oc.isFirstBit())
                    .filter(oc -> !oc.isHalfAdder())
                    .filter(oc -> !oc.secondBitAdder())
                    .filter(oc -> !oc.isFullAdderInOrder())
                    .toList();
            if (lookMore.isEmpty()) {
                stillInterestingPairs.add(wires);
            }
            swapOutputs(gates, wires.getLeft(), wires.getRight());
        }
        if (stillInterestingPairs.size() == 1) {
            return List.of(stillInterestingPairs.get(0).getLeft(), stillInterestingPairs.get(0).getRight());
        }
        return new ArrayList<>();
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
