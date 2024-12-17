package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChronospatialComputer extends AoCDay {

    Long registerA;
    Long registerB;
    Long registerC;
    List<Integer> instructions = new ArrayList<>();
    int instructionPointer = 0;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 17, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines.get(4).split(":\\s+")[1]);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void parseLines(List<String> lines) {
        registerA = Long.parseLong(lines.get(0).split(":\\s+")[1]);
        registerB = Long.parseLong(lines.get(1).split(":\\s+")[1]);
        registerC = Long.parseLong(lines.get(2).split(":\\s+")[1]);
        instructions = Arrays.stream(lines.get(4).split(":\\s+")[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    long getOperand(int operand) {
        if (operand <= 3) {
            return operand;
        } else if (operand == 4) {
            return registerA;
        } else if (operand == 5) {
            return registerB;
        } else if (operand == 6) {
            return registerC;
        } else if (operand == 7) {
            throw new RuntimeException("Not allowed");
        }
        return -1;
    }

    List<Long> runProgram() {
        boolean run = true;
        List<Long> values = new ArrayList<>();
        while (instructionPointer < instructions.size()) {
            int opcode = instructions.get(instructionPointer);
            if (opcode == 0) {
                registerA >>= getOperand(instructions.get(instructionPointer+1));
            } else if (opcode == 1) {
                registerB ^= instructions.get(instructionPointer+1);
            } else if (opcode == 2) {
                registerB = (long) getOperand(instructions.get(instructionPointer+1)) & 0x7;
            } else if (opcode == 3) {
                if (registerA != 0) {
                    instructionPointer = instructions.get(instructionPointer+1)-2;
                }
            } else if (opcode == 4) {
                registerB ^= registerC;
            } else if (opcode == 5) {
                values.add(getOperand(instructions.get(instructionPointer+1)) & 0x7);
            } else if (opcode == 6) {
                registerB = registerA >> getOperand(instructions.get(instructionPointer+1));
            } else if (opcode == 7) {
                registerC = registerA >> getOperand(instructions.get(instructionPointer+1));
            }
            instructionPointer+=2;
        }
        return values;
    }

    String solutionPart1() {
        List<Long> values = runProgram();
        return values.stream().map(v -> ""+v).collect(Collectors.joining(","));
    }

    List<Long> findPossibleNumbers(List<Long> possibleValues, int shiftAmount, String expected, int substringStart) {
        List<Long> nextValues = new ArrayList<>();
        for (Long possibleValue : possibleValues) {
            for (long h = 0; h < 8; h++) {
                for (long i = 0; i < 8; i++) {
                    for (long j = 0; j < 8; j++) {
                        for (long k = 0; k < 8; k++) {
                            registerA = possibleValue;
                            registerA += (k << (shiftAmount + 9));
                            registerA += (j << (shiftAmount + 6));
                            registerA += (i << (shiftAmount + 3));
                            registerA += (h << (shiftAmount + 0));
                            long temp = registerA;
                            registerB = 0L;
                            registerC = 0L;
                            instructionPointer = 0;
                            List<Long> values = runProgram();
                            String t = values.stream().map(s -> "" + s).collect(Collectors.joining(","));
                            if (t.length() > 27 && expected.substring(substringStart).equals(t.substring(substringStart))) {
                                nextValues.add(temp);
                            }
                        }
                    }
                }
            }
        }
        return nextValues;
    }

    long solutionPart2(String expected) {
        List<Long> possible_values = new ArrayList<>();
        possible_values.add(0L);
        for (int i = 0; i < 4; i++) {
            possible_values = findPossibleNumbers(possible_values, (3-i)*12, expected, 32-(8*(i+1)));
        }
        System.out.println("possible_values = " + possible_values);
        return possible_values.stream().mapToLong(l -> l).min().getAsLong();
    }

}
