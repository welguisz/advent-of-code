package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoprocessorConflagration extends AoCDay {
    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2017/day23/input.txt");
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public Long solutionPart1(List<String> lines) {
        Integer instructionPointer = 0;
        Map<String, Long> registers = new HashMap<>();
        registers.put("a",0L);
        registers.put("b",0L);
        registers.put("c",0L);
        registers.put("d",0L);
        registers.put("e",0L);
        registers.put("f",0L);
        registers.put("g",0L);
        registers.put("h",0L);
        Long multiplyInstructionHit = 0L;
        while (instructionPointer < lines.size()) {
            String l = lines.get(instructionPointer);
            String[] lSplit = l.split(" ");
            String instr = lSplit[0];
            String register = lSplit[1];
            Long value = 0L;
            if (registers.containsKey(lSplit[2])) {
                value = registers.get(lSplit[2]);
            } else {
                value = Long.parseLong(lSplit[2]);
            }
            if (instr.equals("set")) {
                registers.put(register, value);
                instructionPointer++;
            } else if (instr.equals("sub")) {
                registers.put(register, registers.get(register) - value);
                instructionPointer++;
            } else if (instr.equals("mul")) {
                registers.put(register, registers.get(register) * value);
                instructionPointer++;
                multiplyInstructionHit++;
            } else if (instr.equals("jnz")) {
                Long regValue = 0L;
                if (registers.containsKey(register)) {
                    regValue = registers.get(register);
                } else {
                    regValue = Long.parseLong(register);
                }
                if (regValue.equals(0L)) {
                    instructionPointer++;
                } else {
                    instructionPointer += value.intValue();
                }
            }
        }
        return multiplyInstructionHit;
    }

    public Long solutionPart2() {
        Long hValue = 0L;
        Integer b = 79*100 - (-100000);
        Integer c = b - (-17000);
        for (int x = b; x <= c; x += 17) {
            for (int i = 2; i < x; i++) {
                if (x % i == 0) {
                    hValue++;
                    break;
                }
            }
        }
        return hValue;
    }
}
