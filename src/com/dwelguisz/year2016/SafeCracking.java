package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SafeCracking extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day23/input.txt");
        //Integer part1 = solutionPart1(lines, 7);
        Integer part1 = quickPart1(7);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = quickPart1(12);
        System.out.println(String.format("Part 1 Answer: %d",part2));
    }

    public Integer quickPart1(Integer startingValue) {
        Integer a = startingValue;
        Integer b = a-1;
        Boolean toggledJump = false;
        while (!toggledJump) {
            a *= b;
            b--;
            toggledJump = (b == 1);
        }
        return a + (93*81);
    }

    public static List<String> ONE_ARGUMENT_INSTRUCTIONS = List.of("inc", "dec", "tgl");
    public static List<String> TWO_ARGUMENT_INSTRUCTIONS = List.of("cpy", "jnz");

    public Integer solutionPart1(List<String> originalLines, Integer valueA) {
        List<String> lines = new ArrayList<>(originalLines);
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", valueA);
        registers.put("b", 0);
        registers.put("c", 0);
        registers.put("d", 0);
        Integer lineNumber = 0;
        Integer instructionCount = 0;
        Map<Integer, Integer> instructionMapCount = new HashMap<>();
        while (lineNumber < lines.size()) {
            if (instructionCount > 100000000) {
                for (Map.Entry<Integer, Integer> v : instructionMapCount.entrySet()) {
                    System.out.println("Line Number " + v.getKey() + ": " + v.getValue());
                }
                break;
            }
            instructionCount++;
            Integer count = instructionMapCount.getOrDefault(lineNumber,0);
            count++;
            instructionMapCount.put(lineNumber,count);
            String line = lines.get(lineNumber);
            Integer nextInstr = 1;
            String parseLine[] = line.split(" ");
            String opCode = parseLine[0];
            if (opCode.equals("mul")) {
                Integer value1 = registers.get(parseLine[1]);
                Integer value2 = registers.get(parseLine[2]);
                registers.put(parseLine[3], value1 * value2);
            }
            else if (opCode.equals("tgl")) {
                Integer value = 0;
                if (registers.containsKey(parseLine[1])) {
                    value = registers.get(parseLine[1]);
                } else {
                    value = Integer.parseInt(parseLine[1]);
                }
                Integer updateInstruction = lineNumber + value;
                if ((updateInstruction >= 0) && (updateInstruction < lines.size())) {
                    System.out.println("Toggling Line Number: " + updateInstruction);
                    System.out.println("Original instruction: " + lines.get(updateInstruction));
                    String newInstruction[] = lines.get(updateInstruction).split(" ");
                    if (ONE_ARGUMENT_INSTRUCTIONS.contains(newInstruction[0])) {
                        if (newInstruction[0].equals("inc")) {
                            newInstruction[0] = "dec";
                        } else {
                            newInstruction[0] = "inc";
                        }
                    } else if (TWO_ARGUMENT_INSTRUCTIONS.contains(newInstruction[0])) {
                        if (newInstruction[0].equals("jnz")) {
                            newInstruction[0] = "cpy";
                        } else {
                            newInstruction[0] = "jnz";
                        }
                    }
                    String newInstr = Arrays.stream(newInstruction).collect(Collectors.joining(" "));
                    System.out.println("Updated instruction: " + newInstr);
                    lines.remove(lines.get(updateInstruction));
                    lines.add(updateInstruction,newInstr);
                }
            }
            else if (opCode.equals("cpy")) {
                Integer value = 0;
                if (registers.containsKey(parseLine[1])) {
                    value = registers.get(parseLine[1]);
                } else {
                    value = Integer.parseInt(parseLine[1]);
                }
                if (registers.containsKey(parseLine[2])) {
                    registers.put(parseLine[2], value);
                }
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
                    if (registers.containsKey(parseLine[2])) {
                        nextInstr = registers.get(parseLine[2]);
                    } else {
                        nextInstr = Integer.parseInt(parseLine[2]);
                    }
                }
            }
            lineNumber += nextInstr;
        }
        return registers.get("a");
    }

}
