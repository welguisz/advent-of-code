package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class OpeningTheTuringLock extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,23,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines, 0);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lines, 1);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(List<String> lines, Integer registerAStart) {
        Integer registerA = registerAStart;
        Integer registerB = 0;
        Integer currentLine = 0;
        Boolean inProgram = true;
        while (inProgram) {
            String line = lines.get(currentLine);
            String items[] = line.split(" ");
            String instr = items[0];
            String register = items[1].substring(0,1);
            Integer nextInstr = 1;
            if (instr.equals("hlf")) {
                if (register.equals("a")) {
                    registerA /= 2;
                } else if (register.equals("b")) {
                    registerB /= 2;
                }
            } else if (instr.equals("tpl")) {
                if (register.equals("a")) {
                    registerA *= 3;
                } else if (register.equals("b")) {
                    registerB *= 3;
                }
            } else if (instr.equals("inc")) {
                if (register.equals("a")) {
                    registerA += 1;
                } else if (register.equals("b")) {
                    registerB += 1;
                }
            } else if (instr.equals("jmp")) {
                nextInstr = Integer.parseInt(items[1]);
            } else if (instr.equals("jio")) {
                Integer val = (register.equals("a")) ? registerA : registerB;
                if (val  == 1) {
                    nextInstr = Integer.parseInt(items[2]);
                }
            } else if (instr.equals("jie")) {
                Integer val = (register.equals("a")) ? registerA : registerB;
                if (val %2 == 0) {
                    nextInstr = Integer.parseInt(items[2]);
                }
            }
            currentLine += nextInstr;
            if (currentLine < 0 || currentLine >= lines.size()) {
                inProgram = false;
            }
        }
        return registerB;
    }
}
