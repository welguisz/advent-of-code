package com.dwelguisz.year2019.IntCodeComputer;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class IntCodeComputer implements Runnable{
    Integer programCounter;
    Map<Integer, Integer> intCode;
    //Stores the opcodes and the jump for position
    Map<Integer, Integer> opCodes;
    ArrayDeque<Integer> inputValues;
    ArrayDeque<Integer> outputValues;
    Integer id;
    Boolean done;

    enum ParameterModes {
        positionMode,
        immediateMode
    };
    public IntCodeComputer() {
        programCounter = 0;
        id = 0;
        done = false;
        opCodes = new HashMap<>();
        inputValues = new ArrayDeque<>();
        outputValues = new ArrayDeque<>();
        opCodes.put(1,4);
        opCodes.put(2,4);
        opCodes.put(3,2);
        opCodes.put(4,2);
        opCodes.put(5,3);
        opCodes.put(6,3);
        opCodes.put(7,4);
        opCodes.put(8,4);
    }

    public Boolean isDone() {
        return this.done;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.inputValues.add(inputValue);
    }

    public Integer getOutputValue() {
        return outputValues.poll();
    }

    public Optional<Integer> getOutputValuePolling() {
        if (!outputValues.isEmpty()) {
            return Optional.of(outputValues.poll());
        }
        return Optional.empty();
    }

    public Boolean outputAvailable() {
        return !outputValues.isEmpty();
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

    public static Integer SLEEP_TIME = 10;

    public void run() {
        Integer currentInstructionWithMode = intCode.getOrDefault(programCounter,-1);
        Integer currentInstruction = currentInstructionWithMode % 100;
        boolean incProgramCounter = true;
        done = false;
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
                    while (inputValues.isEmpty()) {
                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (InterruptedException e) {

                        }
                    }
                    intCode.put(opPointer1, inputValues.poll());
                    break;
                }
                case 4: {
                    outputValues.add((mode0 == ParameterModes.positionMode) ? intCode.getOrDefault(opPointer1, Integer.MIN_VALUE) : opPointer1);
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
        done = true;

    }
}
