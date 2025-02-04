package com.dwelguisz.year2018;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class WristDevice {
    Map<Long, Long> registers;
    List<List<Long>> program;
    List<String> programStr;
    Map<Long, String> opCodeDecode;
    Long programCounterToReg;
    Long programCounter;
    Boolean useProgramCounter;
    List<Long> heatMap;
    Integer stopCount;
    Integer cycleCount;
    public WristDevice(Boolean useProgramCounter) {
        registers = new HashMap<>();
        registers.put(0L,0L);
        registers.put(1L,0L);
        registers.put(2L,0L);
        registers.put(3L,0L);
        registers.put(4L,0L);
        registers.put(5L,0L);
        program = new ArrayList<>();
        programStr = new ArrayList<>();
        opCodeDecode = new HashMap<>();
        programCounterToReg = 0L;
        programCounter = 0L;
        stopCount = -1;
        heatMap = new ArrayList<>();
        cycleCount = 0;
        this.useProgramCounter = useProgramCounter;
    };

    public void setStopCount(Integer stopCount) {
        this.stopCount = stopCount;
    }
    public void setProgramStr(List<String> programStr) {
        this.programStr = programStr;
        for (String s : programStr) {
            heatMap.add(0L);
        }
    }

    public void setProgramCounterToReg(Long programCounterToReg) {
        this.programCounterToReg = programCounterToReg;
    }

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
            processInstruction(inst, instruction);
            results.put(inst,registers.get(instruction.get(3)));
        }
        return results;
    }

    public void processInstruction(String inst, List<Long> instruction) {
        if (useProgramCounter) {
            registers.put(programCounterToReg,programCounter);
        }
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
        if (useProgramCounter) {
            programCounter = registers.get(programCounterToReg);
            programCounter++;
            registers.put(programCounterToReg,programCounter);
        }
    }

    public void run() {
        if (useProgramCounter) {
            cycleCount = 0;
            while ((programCounter >= 0) && (programCounter < programStr.size())) {
                String instr = programStr.get(programCounter.intValue());
                String[] split = instr.split(" ");
                if ((stopCount >= 0) && (stopCount < cycleCount)) {
                    break;
                }
                if (cycleCount + 100 > stopCount) {
                    List<Long> regs = LongStream.range(0,6).boxed().map(r -> registers.get(r)).collect(Collectors.toList());
                }
                if (stopCount > 0) {
                    Long tmp = heatMap.remove(programCounter.intValue());
                    tmp++;
                    heatMap.add(programCounter.intValue(),tmp);
                }
                processInstruction(split[0], List.of(0L,Long.parseLong(split[1]),Long.parseLong(split[2]),Long.parseLong(split[3])));
                cycleCount++;
            }
        } else {
            for (List<Long> inst : program) {
                processInstruction(opCodeDecode.get(inst.get(0)), inst);
            }
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
        Long putValue = (a.equals(value2)) ? 1L : 0L;
        registers.put(c,putValue);
    }

    public void eqri(Long a, Long b, Long c) {
        Long value1 = registers.get(a);
        Long putValue = (value1.equals(b)) ? 1L : 0L;
        registers.put(c,putValue);
    }

    public void eqrr(Long a, Long b, Long c) {
        Long value1 = registers.get(a);
        Long value2 = registers.get(b);
        Long putValue = (value1.equals(value2)) ? 1L : 0L;
        registers.put(c,putValue);
    }

}
