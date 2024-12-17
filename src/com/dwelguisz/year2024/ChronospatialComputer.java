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

    long solutionPart2(String expected) {
        System.out.println("instructionSize = " + instructions.size());
        long tempValue = Long.MAX_VALUE;
        List<Long> most_significant_half = new ArrayList<>();
        for (long h = 0; h < 8; h++) {
            for (long i = 0; i < 8; i++) {
                for (long j = 0; j < 8; j++) {
                    for (long k = 0; k < 8; k++) {
                        for (long l = 0; l < 8; l++) {
                            for (long m = 0; m < 8; m++) {
                                for (long n = 0; n < 8; n++) {
                                    for (long o = 0; o < 8; o++) {
                                        List<Long> ints = List.of(h,i, j, k, l, m, n, o);
                                        registerA += (long) (o << 45);
                                        registerA += (long) (n << 42);
                                        registerA += (long) (m << 39);
                                        registerA += (long) (l << 36);
                                        registerA += (long) (k << 33);
                                        registerA += (long) (j << 30);
                                        registerA += (long) (i << 27);
                                        registerA += (long) (h << 24);
                                        long temp = registerA;
                                        registerB = 0L;
                                        registerC = 0L;
                                        instructionPointer = 0;
                                        List<Long> values = runProgram();
                                        String t = values.stream().map(s -> "" + s).collect(Collectors.joining(","));
                                        if (t.length() > 27 && expected.substring(16).equals(t.substring(16))) {
                                            most_significant_half = ints;
                                            System.out.println(ints.stream().map(el -> "" + el).collect(Collectors.joining(",")) + "; temp: " + temp + "; output: " + t);
                                            break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        long start = 0l;
        long leftShift = 45;
        List<Long> abc = new ArrayList<>(most_significant_half);
        Collections.reverse(abc);
        for (Long value : abc) {
            start += value << leftShift;
            leftShift -= 3;
        }
        //long start = (1L<<45) + (3L<<39) + (5L<<36) + (5L<<33) + (1L << 30) + (7 << 27) + (4 << 24);
        System.out.println("start value (part2): " + start);
        for (long h = 0; h < 8; h++) {
            for (long i = 0; i < 8; i++) {
                for (long j = 0; j < 8; j++) {
                    for (long k = 0; k < 8; k++) {
                        for (long l = 0; l < 8; l++) {
                            for (long m = 0; m < 8; m++) {
                                for (long n = 0; n < 8; n++) {
                                    for (long o = 0; o < 8; o++) {
                                        List<Long> ints = List.of(h,i, j, k, l, m, n, o);
                                        registerA = start;
                                        registerA += (long) (o << 21);
                                        registerA += (long) (n << 18);
                                        registerA += (long) (m << 15);
                                        registerA += (long) (l << 12);
                                        registerA += (long) (k << 9);
                                        registerA += (long) (j << 6);
                                        registerA += (long) (i << 3);
                                        registerA += h;
                                        long temp = registerA;
                                        registerB = 0L;
                                        registerC = 0L;
                                        instructionPointer = 0;
                                        List<Long> values = runProgram();
                                        String t = values.stream().map(s -> "" + s).collect(Collectors.joining(","));
                                        if (t.length() > 27 && expected.substring(4).equals(t.substring(4))) {
                                            System.out.println(ints.stream().map(el -> "" + el).collect(Collectors.joining(",")) + "; temp: " + temp + "; output: " + t);
                                        }
                                        if (expected.equals(t)) {
                                            if (temp < tempValue) {
                                                System.out.println("temp Value: " + temp);
                                                tempValue = temp;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return tempValue;
    }

}
