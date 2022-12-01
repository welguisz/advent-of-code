package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockSignal extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day25/input.txt");
        Integer value = -1;
        Boolean found = false;
        while (!found) {
            if (value % 1000 == 0) {
                System.out.println("Trying value: " + value);
            }
            value++;
            found = solutionPart1(lines, value);
        }
        System.out.println(String.format("Part 1 Answer: %d",value));
    }

    public static List<Integer> CLOCK_VALUES = List.of(0,1);

    public Boolean solutionPart1(List<String> lines, Integer valueA) {
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", valueA);
        registers.put("b", 0);
        registers.put("c", 0);
        registers.put("d", 0);
        Integer lineNumber = 0;
        List<Integer> outputForRegsiterA = new ArrayList<>();
        while (lineNumber < lines.size()) {
            String line = lines.get(lineNumber);
            Integer nextInstr = 1;
            String parseLine[] = line.split(" ");
            String opCode = parseLine[0];
            if (opCode.equals("cpy")) {
                Integer value = 0;
                if (registers.containsKey(parseLine[1])) {
                    value = registers.get(parseLine[1]);
                } else {
                    value = Integer.parseInt(parseLine[1]);
                }
                registers.put(parseLine[2],value);
            } else if (opCode.equals("inc")) {
                Integer value = registers.get(parseLine[1]);
                value++;
                registers.put(parseLine[1],value);
            } else if (opCode.equals("dec")) {
                Integer value = registers.get(parseLine[1]);
                value--;
                registers.put(parseLine[1],value);
            } else if (opCode.equals("jnz")) {
                Integer value = 0;
                if (registers.containsKey(parseLine[1])) {
                    value = registers.get(parseLine[1]);
                } else {
                    value = Integer.parseInt(parseLine[1]);
                }
                if (!value.equals(0)) {
                    nextInstr = Integer.parseInt(parseLine[2]);
                }
            } else if (opCode.equals("out")) {
                Integer value = registers.get(parseLine[1]);
                outputForRegsiterA.add(value);
                if (outputForRegsiterA.size() % 10 == 0) {
                    Integer previousVal = null;
                    for (Integer val : outputForRegsiterA) {
                        if (previousVal == null) {
                            previousVal = val;
                            if (!CLOCK_VALUES.contains(val)) {
                                return false;
                            }
                        } else {
                            if (!CLOCK_VALUES.contains(val)) {
                                return false;
                            }
                            if (!previousVal.equals(val)) {
                                previousVal = val;
                            } else {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
            lineNumber += nextInstr;
        }
        return false;
    }
}
