package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockSignal extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,25,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();

        Integer value = -1;
        Boolean found = false;
        while (!found) {
            value++;
            found = solutionPart1(lines, value);
        }
        part1Answer = value;
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "Click the link to finish";
        timeMarkers[3] = Instant.now().toEpochMilli();
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
