package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeonardoMonorail extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,12,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines, 0);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lines, 1);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(List<String> lines, Integer valueC) {
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", 0);
        registers.put("b", 0);
        registers.put("c", valueC);
        registers.put("d", 0);
        Integer lineNumber = 0;
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
            }
            lineNumber += nextInstr;
        }
        return registers.get("a");
    }
}
