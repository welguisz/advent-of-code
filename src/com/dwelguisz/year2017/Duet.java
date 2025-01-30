package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.*;

public class Duet extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,18,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<String> lines) {
        Long lastSoundPlayed = 0L;
        Map<Character,Long> registers = new HashMap<>();
        Boolean run = true;
        Integer currentInstruction = 0;
        while (run) {
            String cmd[] = lines.get(currentInstruction).split(" ");
            Integer nextInstruction = 1;
            if (cmd[0].equals("snd")) {
                Long val = registers.getOrDefault(cmd[1].toCharArray()[0],0L);
                lastSoundPlayed = val;
            } else if (cmd[0].equals("set")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, Long.parseLong(cmd[2]));
                } else {
                    Long val = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val);
                }
            }
            else if (cmd[0].equals("add")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, Long.parseLong(cmd[2]) + val);
                } else {
                    Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val + val2);
                }
            } else if (cmd[0].equals("mul")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, Long.parseLong(cmd[2]) * val);
                } else {
                    Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val * val2);
                }
            } else if (cmd[0].equals("mod")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, val % Long.parseLong(cmd[2]));
                } else {
                    Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val % val2);
                }
            } else if (cmd[0].equals("rcv")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (!val.equals(0L)) {
                    run = false;
                }
            } else if (cmd[0].equals("jgz")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (!val.equals(0L)) {
                    if (checkValue(cmd[2])) {
                        nextInstruction = Integer.parseInt(cmd[2]);
                    } else {
                        Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                        nextInstruction = val2.intValue();
                    }
                }

            }
            currentInstruction += nextInstruction;
        }
        return lastSoundPlayed;
    }

    public Long solutionPart2(List<String> lines) {
        Map<Character,Long> registers1 = new HashMap<>();
        Map<Character,Long> registers2 = new HashMap<>();
        registers1.put('p',0L);
        registers2.put('p',1L);
        Integer currentProgram = 0;
        Boolean run = true;
        Integer currentInstructionArr[] = new Integer[2];
        currentInstructionArr[0] = 0;
        currentInstructionArr[1] = 0;
        Queue<Long> sentFromA = new LinkedList<>();
        Queue<Long> sentFromB = new LinkedList<>();
        Long sentFromBCount = 0L;
        while (run) {
            Integer currentInstruction = currentInstructionArr[currentProgram];
            Map<Character,Long> registers = (currentProgram == 0) ? registers1 : registers2;
            String cmd[] = lines.get(currentInstruction).split(" ");
            Integer nextInstruction = 1;
            if (cmd[0].equals("snd")) {
                Long val = 0L;
                if (checkValue(cmd[1])) {
                    val = Long.parseLong(cmd[1]);
                } else {
                    val = registers.getOrDefault(cmd[1].toCharArray()[0], 0L);
                }
                if (currentProgram == 0) {
                    sentFromA.add(val);
                } else {
                    sentFromB.add(val);
                    sentFromBCount++;
                }
            } else if (cmd[0].equals("set")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, Long.parseLong(cmd[2]));
                } else {
                    Long val = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val);
                }
            }
            else if (cmd[0].equals("add")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, Long.parseLong(cmd[2]) + val);
                } else {
                    Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val + val2);
                }
            } else if (cmd[0].equals("mul")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, Long.parseLong(cmd[2]) * val);
                } else {
                    Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val * val2);
                }
            } else if (cmd[0].equals("mod")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = registers.getOrDefault(targetRegister, 0L);
                if (checkValue(cmd[2])) {
                    registers.put(targetRegister, val % Long.parseLong(cmd[2]));
                } else {
                    Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                    registers.put(targetRegister, val % val2);
                }
            } else if (cmd[0].equals("rcv")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                if (sentFromA.isEmpty() && sentFromB.isEmpty()) {
                    run = false;
                } else if (currentProgram == 0) {
                    if (sentFromB.isEmpty()) {
                        currentProgram = 1;
                        continue;
                    } else {
                        registers.put(targetRegister, sentFromB.remove());
                    }
                } else if (currentProgram == 1) {
                    if (sentFromA.isEmpty()) {
                        currentProgram = 0;
                        continue;
                    } else {
                        registers.put(targetRegister, sentFromA.remove());
                    }
                }
            } else if (cmd[0].equals("jgz")) {
                Character targetRegister = cmd[1].toCharArray()[0];
                Long val = 0L;
                if (checkValue(cmd[1])) {
                    val = Long.parseLong(cmd[1]);
                } else {
                    val = registers.getOrDefault(targetRegister, 0L);
                }
                if (val > 0L) {
                    if (checkValue(cmd[2])) {
                        nextInstruction = Integer.parseInt(cmd[2]);
                    } else {
                        Long val2 = registers.getOrDefault(cmd[2].toCharArray()[0],0L);
                        nextInstruction = val2.intValue();
                    }
                }

            }
            currentInstruction += nextInstruction;
            if (currentProgram == 0) {
                currentInstructionArr[0] = currentInstruction;
                registers1 = registers;
            } else if (currentProgram == 1) {
                currentInstructionArr[1] = currentInstruction;
                registers2 = registers;
            }
        }
        return sentFromBCount;
    }

    public boolean checkValue(String value) {
        if (StringUtils.isNumeric(value)) {
            return true;
        }
        if (value.toCharArray()[0] == '-' && StringUtils.isNumeric(value.substring(1))) {
            return true;
        }
        return false;
    }

}
