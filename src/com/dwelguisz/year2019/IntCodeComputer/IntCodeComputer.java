package com.dwelguisz.year2019.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class IntCodeComputer {
    Integer programCounter;
    Map<Integer, Integer> intCode;
    //Stores the opcodes and the jump for position
    Map<Integer, Integer> opCodes;
    Integer inputValue;
    Integer outputValue;
    enum ParameterModes {
        positionMode,
        immediateMode
    };
    public IntCodeComputer() {
        programCounter = 0;
        opCodes = new HashMap<>();
        inputValue = 0;
        outputValue = 0;
        opCodes.put(1,4);
        opCodes.put(2,4);
        opCodes.put(3,2);
        opCodes.put(4,2);
        opCodes.put(5,3);
        opCodes.put(6,3);
        opCodes.put(7,4);
        opCodes.put(8,4);
    }

    public void setIntCode(Map<Integer, Integer> intCode) {
        this.intCode = intCode;
    }

    public void setIntCodeMemory(Integer address, Integer value) {
        intCode.put(address, value);
    }

    public void initializeIntCode(List<String> lines) {
        intCode = new HashMap<>();
        Integer address = 0;
        for(String value : lines.get(0).split(",")){
            intCode.put(address,Integer.parseInt(value));
            address++;
        }
    }

    public void setInputValue(Integer inputValue) {
        this.inputValue = inputValue;
    }

    public Integer getOutputValue() {
        return this.outputValue;
    }

    public Integer getMemoryLocation(Integer location) {
        return intCode.get(location);
    }

    private ParameterModes updateMode(Integer value) {
        if (value == 0) {
            return ParameterModes.positionMode;
        }
        return ParameterModes.immediateMode;
    }

    public void run() {
        Integer currentInstructionWithMode = intCode.getOrDefault(programCounter,-1);
        Integer currentInstruction = currentInstructionWithMode % 100;
        boolean incProgramCounter = true;
        while ((currentInstruction != 99) && (opCodes.containsKey(currentInstruction))) {
            incProgramCounter = true;
            int aMode = (currentInstructionWithMode / 100) % 10;
            int bMode = (currentInstructionWithMode / 1000) % 10;
            ParameterModes mode0 = updateMode(aMode);
            ParameterModes mode1 = updateMode(bMode);
            Integer opPointer1 = intCode.getOrDefault(programCounter + 1, -1);
            Integer opPointer2 = intCode.getOrDefault(programCounter + 2, -1);
            Integer storePointer = intCode.getOrDefault(programCounter + 3, -1);
            Integer value1 = (mode0 == ParameterModes.positionMode) ? intCode.getOrDefault(opPointer1, -1) : opPointer1;
            Integer value2 = (mode1 == ParameterModes.positionMode) ? intCode.getOrDefault(opPointer2, -1) : opPointer2;
            switch(currentInstruction) {
                case 1: {
                    intCode.put(storePointer, value1 + value2);
                    break;
                }
                case 2: {
                    intCode.put(storePointer, value1 * value2);
                    break;
                }
                case 3: {
                    intCode.put(opPointer1, inputValue);
                    break;
                }
                case 4: {
                    outputValue = (mode0 == ParameterModes.positionMode) ? intCode.getOrDefault(opPointer1, Integer.MIN_VALUE) : opPointer1;
                    System.out.println("Output value: " + outputValue);
                    break;
                }
                case 5: {
                    if (!value1.equals(0)) {
                        programCounter = value2;
                        incProgramCounter = false;
                    }
                    break;
                }
                case 6: {
                    if (value1.equals(0)) {
                        programCounter = value2;
                        incProgramCounter = false;
                    }
                    break;
                }
                case 7: {
                    int value = 0;
                    if (value1 < value2) {
                        value = 1;
                    }
                    intCode.put(storePointer, value);
                    break;
                }
                case 8: {
                    int value = 0;
                    if (value1.equals(value2)) {
                        value = 1;
                    }
                    intCode.put(storePointer, value);
                    break;
                }
            }
            programCounter += incProgramCounter? opCodes.get(currentInstruction) : 0;
            currentInstructionWithMode = intCode.getOrDefault(programCounter, -1);
            currentInstruction = currentInstructionWithMode % 100;
        }

    }
}
