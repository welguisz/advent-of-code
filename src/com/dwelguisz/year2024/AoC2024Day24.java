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
        //This is where you mess with different items.  For example, my first issue was with a bit. When I inspected it, it was:
        //and(z06,y06,x06).
        //Next, I looked at z07 network and that network looked like:
        //xor(z07, jqn, grj);
        //xor(jqn, x07, y07);
        //or(grj, spj, hwk);
        //and(spj, swj, rjv);
        //xor(hwk, swj, rjv);
        //or(swj, tcn, jsd);
        //xor(rjv, y06, x06);
        //------
        //z06 needs to come from a xor gate, so by process of elimination, we can ignore:
        // * rjv -> because it does not have a carry
        // * jqn -> inputs are x07 and y07, so it can't be this.
        // * z07 -> can't be this because it is an output and tracing back z07, we can see its input is x07,y07,and the
        //          ripple carry.
        //Let's check hwk. Now we have:
        //xor(z06, swj, rjv)
        //and(hwk, x06, y06)
        // Let's trace the new z06.
        //xor(z06, swj, rjv); --> brings it together, so this works for z06
        //or(swj, tcn, jsd);  --> swj is a carry
        //xor(rjv, y06, x06);  --> this is the half-adder porition for z06
        // Let's trace the new z07
        //xor(z07, jqn, grj); //full adder here
        //xor(jqn, x07, y07); //half adder here
        //or(grj, spj, hwk);  //Carry logic here
        //and(hwk, x06, y06)  //carry bit for 6th bit
        //and(spj, swj, rjv); //full carry here
        //or(swj, tcn, jsd);
        //xor(rjv, y06, x06);
        swapOutputs(circuit, "z06", "hwk");
        //Working on bit: 25
        //xor(z26, khj, mbh);
        //or(khj, qmd, vhp);
        //xor(mbh, y26, x26);
        //xor(qmd, y25, x25);
        //and(vhp, tnt, nbs);
        //and(tnt, y25, x25);
        //or(nbs, ptr, spw);
        //-----
        //xor(z25, nbs, tnt);
        //or(nbs, ptr, spw);
        //and(tnt, y25, x25);
        //Again same thing here, the carry bit is in the adder logic. So we need to
        //swap `tnt` with either `qmd`, or `mbh`. Eliminate `mbh` because that logic
        //is for `z26`. So, let's go with 'qmd'
        swapOutputs(circuit, "tnt", "qmd");
        //Working on bit: 31
        //xor(z32, hpc, qrw);
        //xor(hpc, vkh, dtq);
        //xor(qrw, y32, x32);
        //xor(vkh, y31, x31);
        //or(dtq, rtq, kcn);
        //and(rtq, y30, x30);
        //and(kcn, rmb, rpt);
        //-----
        //or(z31, mjr, hgw);
        //and(mjr, dtq, vkh);
        //and(hgw, y31, x31);
        //or gate outputting to z31. We need to use an xor gate.  So options:
        // z32, hpc, qrw, xvh
        // Can't use z32 since that will mess up the next bit.
        // Can't use qrw since that is using x32 and y32.
        // Can't use vkh since that is good logic for z31.
        // So it has to be hpc.
        swapOutputs(circuit, "z31", "hpc");
        //Working on bit: 37
        //xor(z38, fsp, hbm);
        //xor(fsp, y38, x38);
        //or(hbm, vbq, cgr);
        //and(vbq, y37, x37);
        //xor(cgr, gqc, vqv);
        //or(gqc, ksr, jvs);
        //xor(vqv, x37, y37);
        //-----
        //and(z37, gqc, vqv);
        //or(gqc, ksr, jvs);
        //xor(vqv, x37, y37);
        //We are anding and we are supposed to be xor. So again, let's look for xor gates:
        //So possible values: vqv, cgr, fsp,z38
        //Can't be vqv because that is coming into our logic for the carry.
        //Can't be fsp since that is using bit38 logic
        //Can't be z38 since that is the output for bit38,
        //So it has to be cgr
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
