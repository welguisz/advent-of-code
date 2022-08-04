package com.dwelguisz.year2019.IntCodeComputer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntCodeComputer {
    Integer programCounter;
    Map<Integer, Integer> intCode;
    List<Integer> validOpCodes;
    public IntCodeComputer(Map<Integer, Integer> intCode) {
        programCounter = 0;
        this.intCode = intCode;
        validOpCodes = new ArrayList<>();
        validOpCodes.add(1);
        validOpCodes.add(2);
        validOpCodes.add(3);
        validOpCodes.add(4);
    }

    public Integer getMemoryLocation(Integer location) {
        return intCode.get(location);
    }

    public void run() {
        Integer currentInstruction = intCode.getOrDefault(programCounter,-1);

        while ((currentInstruction != 99) && (validOpCodes.contains(currentInstruction))) {
            Integer opPointer1 = intCode.getOrDefault(programCounter + 1, -1);
            Integer opPointer2 = intCode.getOrDefault(programCounter + 2, -1);
            Integer storePointer = intCode.getOrDefault(programCounter + 3, -1);
            Integer value1 = intCode.getOrDefault(opPointer1, -1);
            Integer value2 = intCode.getOrDefault(opPointer2, -1);
            if (currentInstruction == 1) {
                intCode.put(storePointer, value1 + value2);
            } else if (currentInstruction == 2) {
                intCode.put(storePointer, value1 * value2);
            }
            programCounter += 4;
            currentInstruction = intCode.getOrDefault(programCounter, -1);
        }

    }
}
