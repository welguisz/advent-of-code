package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeonardoMonorail extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day12/input.txt");
        Integer part1 = solutionPart1(lines, 0);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart1(lines, 1);
        System.out.println(String.format("Part 1 Answer: %d",part2));
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
