package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeardYouLikeRegisters extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day08/input.txt");
        Long part1 = solutionPart1(lines);
        Long part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long solutionPart1(List<String> lines) {
        Map<String, Long> registers = new HashMap<>();
        for(String line : lines) {
            String lineSplit[] = line.split(" if ");
            String instruction[] = lineSplit[0].split(" ");
            String conditional[] = lineSplit[1].split(" ");
            Boolean conditionResult = checkCondition(registers, conditional);
            if (conditionResult) {
                String name = instruction[0];
                String action = instruction[1];
                Long insValue = Long.parseLong(instruction[2]);
                Long value = registers.getOrDefault(name, 0L);
                if (action.equals("inc")) {
                    value += insValue;
                } else {
                    value -= insValue;
                }
                registers.put(name, value);
            }
        }
        Long value = Long.MIN_VALUE;
        for(Map.Entry<String, Long> entry : registers.entrySet()) {
            if (entry.getValue() > value) {
                value = entry.getValue();
            }
        }
        return value;
    }

    public Long solutionPart2(List<String> lines) {
        Map<String, Long> registers = new HashMap<>();
        Long value = Long.MIN_VALUE;
        for(String line : lines) {
            String lineSplit[] = line.split(" if ");
            String instruction[] = lineSplit[0].split(" ");
            String conditional[] = lineSplit[1].split(" ");
            Boolean conditionResult = checkCondition(registers, conditional);
            if (conditionResult) {
                String name = instruction[0];
                String action = instruction[1];
                Long insValue = Long.parseLong(instruction[2]);
                Long tempValue = registers.getOrDefault(name, 0L);
                if (action.equals("inc")) {
                    tempValue += insValue;
                } else {
                    tempValue -= insValue;
                }
                if (tempValue > value) {
                    value = tempValue;
                }
                registers.put(name, tempValue);
            }
        }
        return value;
    }


    public Boolean checkCondition(Map<String, Long> registers, String[] conditional) {
        String registerName = conditional[0];
        Long conditionalCheck = Long.parseLong(conditional[2]);
        Long value = registers.getOrDefault(registerName, 0L);
        switch (conditional[1]) {
            case ">" : {
                return (value > conditionalCheck);
            }
            case "<" : {
                return (value < conditionalCheck);
            }
            case ">=": {
                return (value >= conditionalCheck);
            }
            case "<=": {
                return (value <= conditionalCheck);
            }
            case "==": {
                return (value.equals(conditionalCheck));
            }
            case "!=": {
                return (!value.equals(conditionalCheck));
            }
            default: {
                return false;
            }
        }
    }
}
