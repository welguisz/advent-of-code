package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChronalClassification extends AoCDay {

    List<List<Long>> programs;

    public static class InstructionProcessing {
        List<Long> beforeRegisters;
        List<Long> instruction;
        List<Long> afterRegsiters;

        public InstructionProcessing(List<Long> beforeRegisters, List<Long> instruction, List<Long> afterRegsiters) {
            this.beforeRegisters = beforeRegisters;
            this.instruction = instruction;
            this.afterRegsiters = afterRegsiters;
        }

        public Set<String> possibleOpcodes() {
            WristDevice wristDevice = new WristDevice(false);
            Map<String, Long> results = wristDevice.possibleInstructions(beforeRegisters, instruction);
            Long finalAnswer = afterRegsiters.get(instruction.get(3).intValue());
            Set<String> opCodes = new HashSet<>();
            for (Map.Entry<String, Long> r : results.entrySet()) {
                if (r.getValue().equals(finalAnswer)) {
                    opCodes.add(r.getKey());
                }
            }
            return opCodes;
        }
        public boolean behaveLikeThreeOrMoreOpcodes() {
            Set<String> opCodes = possibleOpcodes();
            if (opCodes.size() > 2) {
                return true;
            }
            return false;
        }

    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day16/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<InstructionProcessing> instructionProcessing = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(instructionProcessing);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(instructionProcessing);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public List<InstructionProcessing> parseLines(List<String> lines) {
        List<Long> after = new ArrayList<>();
        List<Long> before = new ArrayList<>();
        List<Long> instruction = new ArrayList<>();
        List<InstructionProcessing> instructionProcessing = new ArrayList<>();
        programs = new ArrayList<>();
        Integer blankLineCount = 0;
        Boolean secondPart = false;
        for (String l : lines) {
            if (!secondPart) {
                if (l.contains("After")) {
                    String tmp = l.substring(l.indexOf("[")+1, l.indexOf("]"));
                    after = Arrays.stream(tmp.split(", ")).map(Long::parseLong).collect(Collectors.toList());
                    instructionProcessing.add(new InstructionProcessing(before, instruction, after));
                    before = new ArrayList<>();
                    instruction = new ArrayList<>();
                } else if (l.contains("Before")) {
                    blankLineCount = 0;
                    after = new ArrayList<>();
                    String tmp = l.substring(l.indexOf("[")+1, l.indexOf("]"));
                    before = Arrays.stream(tmp.split(", ")).map(Long::parseLong).collect(Collectors.toList());
                } else if (after.isEmpty()) {
                    instruction = Arrays.stream(l.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                } else if (l.length() == 0) {
                    blankLineCount++;
                }
                if (blankLineCount > 2) {
                    secondPart = true;
                }
            } else {
                instruction = Arrays.stream(l.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                programs.add(instruction);
            }
        }
        return instructionProcessing;
    }

    public Integer solutionPart1(List<InstructionProcessing> instructionProcessing) {
        Integer count = 0;
        for (InstructionProcessing ip : instructionProcessing) {
            if (ip.behaveLikeThreeOrMoreOpcodes()) {
                count++;
            }
        }
        return count;
    }

    public Long solutionPart2(List<InstructionProcessing> instructionProcessings) {
        Map<String, Set<Long>> heatMap = new HashMap<>();
        for (InstructionProcessing ip : instructionProcessings) {
            Set<String> opCodes = ip.possibleOpcodes();
            for (String opCode : opCodes) {
                Set<Long> ints = heatMap.getOrDefault(opCode, new HashSet<>());
                ints.add(ip.instruction.get(0));
                heatMap.put(opCode, ints);
            }
        }
        Map<Long, String> opCodeDecode = new HashMap<>();
        while (!heatMap.isEmpty()) {
            Map<String, Set<Long>> newHeatMap = new HashMap<>();
            for(Map.Entry<String,Set<Long>> v : heatMap.entrySet()) {
                Set<Long> newValues = new HashSet<>();
                for (Long v1 : v.getValue()) {
                    if (!opCodeDecode.containsKey(v1)) {
                        newValues.add(v1);
                    }
                }
                if (!newValues.isEmpty()) {
                    newHeatMap.put(v.getKey(), newValues);
                }
            }
            heatMap = newHeatMap;
            for (Map.Entry<String,Set<Long>> v : heatMap.entrySet()) {
                if (v.getValue().size()==1) {
                    opCodeDecode.put(v.getValue().stream().collect(Collectors.toList()).get(0),v.getKey());
                }
            }
        }
        WristDevice wristDevice = new WristDevice(false);
        wristDevice.setProgram(programs);
        wristDevice.setOpCodeDecode(opCodeDecode);
        wristDevice.run();
        return wristDevice.registers.get(0L);
    }

}
