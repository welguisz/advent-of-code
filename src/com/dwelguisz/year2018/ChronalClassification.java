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

    public static class WristDevice {
        Map<Long, Long> registers;
        List<List<Long>> program;
        Map<Long, String> opCodeDecode;
        public WristDevice() {
            registers = new HashMap<>();
            registers.put(0L,0L);
            registers.put(1L,0L);
            registers.put(2L,0L);
            registers.put(3L,0L);
            program = new ArrayList<>();
            opCodeDecode = new HashMap<>();
        };

        public void setProgram(List<List<Long>> program) {
            this.program = program;
        }

        public void setOpCodeDecode(Map<Long,String> opCodeDecode) {
            this.opCodeDecode = opCodeDecode;
        }
        List<String> instructions = List.of("addr","addi","mulr","muli","banr","bani","borr","bori",
                "setr","seti","gtir","gtri","gtrr","eqir","eqri","eqrr");

        public Map<String, Long> possibleInstructions(List<Long> registersBefore, List<Long> instruction) {
            Map<String, Long> results = new HashMap<>();
            for (String inst : instructions) {
                for (Long i = 0L; i < registersBefore.size(); i++) {
                    registers.put(i, registersBefore.get(i.intValue()));
                }
                if(inst.equals("addr")) {
                    addr(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("addi")) {
                    addi(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("mulr")) {
                    mulr(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("muli")) {
                    muli(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("banr")) {
                    banr(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("bani")) {
                    bani(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("borr")) {
                    borr(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("bori")) {
                    bori(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("setr")) {
                    setr(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("seti")) {
                    seti(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("gtir")) {
                    gtir(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("gtri")) {
                    gtri(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("gtrr")) {
                    gtrr(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("eqir")) {
                    eqir(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("eqri")) {
                    eqri(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                } else if (inst.equals("eqrr")) {
                    eqrr(instruction.get(1), instruction.get(2), instruction.get(3));
                    results.put(inst,registers.get(instruction.get(3)));
                }
            }
            return results;
        }

        public void processInstruction(String inst, List<Long> instruction) {
            if(inst.equals("addr")) {
                addr(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("addi")) {
                addi(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("mulr")) {
                mulr(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("muli")) {
                muli(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("banr")) {
                banr(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("bani")) {
                bani(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("borr")) {
                borr(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("bori")) {
                bori(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("setr")) {
                setr(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("seti")) {
                seti(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("gtir")) {
                gtir(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("gtri")) {
                gtri(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("gtrr")) {
                gtrr(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("eqir")) {
                eqir(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("eqri")) {
                eqri(instruction.get(1), instruction.get(2), instruction.get(3));
            } else if (inst.equals("eqrr")) {
                eqrr(instruction.get(1), instruction.get(2), instruction.get(3));
            }
        }

        public void run() {
            for (List<Long> inst : program) {
                processInstruction(opCodeDecode.get(inst.get(0)), inst);
            }
        }

        public void addr(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long value2 = registers.get(b);
            registers.put(c, value1+value2);
        }

        public void addi(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            registers.put(c, value1 + b);
        }

        public void mulr(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long value2 = registers.get(b);
            registers.put(c, value1*value2);
        }

        public void muli(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            registers.put(c, value1 * b);
        }

        public void banr(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long value2 = registers.get(b);
            registers.put(c, value1&value2);
        }

        public void bani(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            registers.put(c, value1 & b);
        }

        public void borr(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long value2 = registers.get(b);
            registers.put(c, value1|value2);
        }

        public void bori(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            registers.put(c, value1 | b);
        }

        public void setr(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            registers.put(c, value1);
        }

        public void seti(Long a, Long b, Long c) {
            registers.put(c,a);
        }

        public void gtir(Long a, Long b, Long c) {
            Long value2 = registers.get(b);
            Long putValue = (a > value2) ? 1L : 0L;
            registers.put(c,putValue);
        }

        public void gtri(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long putValue = (value1 > b) ? 1L : 0L;
            registers.put(c,putValue);
        }

        public void gtrr(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long value2 = registers.get(b);
            Long putValue = (value1 > value2) ? 1L : 0L;
            registers.put(c,putValue);
        }

        public void eqir(Long a, Long b, Long c) {
            Long value2 = registers.get(b);
            Long putValue = (a == value2) ? 1L : 0L;
            registers.put(c,putValue);
        }

        public void eqri(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long putValue = (value1 == b) ? 1L : 0L;
            registers.put(c,putValue);
        }

        public void eqrr(Long a, Long b, Long c) {
            Long value1 = registers.get(a);
            Long value2 = registers.get(b);
            Long putValue = (value1 == value2) ? 1L : 0L;
            registers.put(c,putValue);
        }

    }
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
            WristDevice wristDevice = new WristDevice();
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
        WristDevice wristDevice = new WristDevice();
        wristDevice.setProgram(programs);
        wristDevice.setOpCodeDecode(opCodeDecode);
        wristDevice.run();
        return wristDevice.registers.get(0L);
    }

}
