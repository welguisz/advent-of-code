package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class SomeAssemblyRequired extends AoCDay {

    static Integer NOT_SET_INPUT = 10000000;
    static List<String> allowedInstructions;
    static Pattern pattern = Pattern.compile("\\d+");
    static {
        allowedInstructions = new ArrayList<>();
        allowedInstructions.add("AND");
        allowedInstructions.add("OR");
        allowedInstructions.add("LSHIFT");
        allowedInstructions.add("RSHIFT");
        allowedInstructions.add("NOT");
    }

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2015/day07/input.txt");
        Map<String, Integer> values = new HashMap<>();
        values = part1(instructions, values);
        System.out.println(values);
        Integer part1 = values.get("a");
        Map<String, Integer> newValues = new HashMap<>();
        newValues.put("b", part1);
        newValues = part1(instructions, newValues);
        Integer part2 = newValues.get("a");
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    static Map<String, Integer> part1(List<String> instructions, Map<String, Integer> values) {
        while (!instructions.isEmpty()) {
            List<String> remainingInstruction = new ArrayList<>();
            for(String instruction : instructions) {
                String[] puts = instruction.split(" -> ");
                String output = puts[puts.length - 1];
                Integer instructionLocation = -1;
                String[] parseInput = puts[0].split(" ");
                for (int i = 0; i < parseInput.length; i++) {
                    if (allowedInstructions.contains(parseInput[i])) {
                        instructionLocation = i;
                        break;
                    }
                }
                if (instructionLocation == -1) {
                    int x = getValue(parseInput[0], values);
                    if (x == NOT_SET_INPUT) {
                        remainingInstruction.add(instruction);
                        continue;
                    }
                    if (!values.containsKey(output)) {
                        values.put(output, x);
                    }
                } else if (instructionLocation == 0) { // not
                    int x = getValue(parseInput[1], values);
                    if (x == NOT_SET_INPUT) {
                        remainingInstruction.add(instruction);
                        continue;
                    }
                    if (!values.containsKey(output)) {
                        values.put(output, notOperation(getValue(parseInput[1], values)));
                    }
                } else if (instructionLocation == 1) { // and, or, lshift, rshift
                    int x = getValue(parseInput[0], values);
                    int y = getValue(parseInput[2], values);
                    if ((x == NOT_SET_INPUT) || (y == NOT_SET_INPUT)) {
                        remainingInstruction.add(instruction);
                        continue;
                    }
                    int temp = 0;
                    if (parseInput[1].equals("AND")) {
                        temp = andOperation(x, y);
                    } else if (parseInput[1].equals("OR")) {
                        temp = orOperation(x, y);
                    } else if (parseInput[1].equals("LSHIFT")) {
                        temp = leftShift(x, y);
                    } else if (parseInput[1].equals("RSHIFT")) {
                        temp = rightShift(x, y);
                    }
                    if (!values.containsKey(output)) {
                        values.put(output, temp);
                    }
                }
            }
            instructions = remainingInstruction;
        }
        return values;
    }

    static Integer getValue(String instruction, Map<String, Integer> values) {
        String yStr = instruction;
        if (isNumber(yStr)) {
            return parseInt(yStr);
        }
        return values.getOrDefault(yStr, NOT_SET_INPUT);
    }

    static boolean isNumber(String str) {
        return pattern.matcher(str).matches();
    }

    static Integer make16bit(int x) {
        return x & 65535;
    }

    static Integer andOperation(int x, int y) {
        return make16bit(x & y);
    }

    static Integer orOperation(int x, int y) {
        return make16bit(x | y);
    }

    static Integer notOperation(int x) {
        return make16bit(~x);
    }

    static Integer leftShift(int x, int y) {
        return make16bit (x << y);
    }

    static Integer rightShift(int x, int y) {
        return make16bit(x >> y);
    }

}
