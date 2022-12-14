package com.dwelguisz.year2019.IntCodeComputer;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class IntCodeComputer implements Runnable {
    Long programCounter;
    Map<Long, Long> intCode;
    //Stores the opcodes and the jump for position
    Map<Long, Long> opCodes;
    ArrayDeque<Long> inputValues;
    ArrayDeque<Long> outputValues;
    Long id;
    Boolean done;
    Long debugValue;

    Long relativeBaseAddress;

    enum ParameterModes {
        positionMode,
        immediateMode,
        relativeMode
    };

    Boolean stopOnFirstTime;

    public IntCodeComputer()  {
        programCounter = 0L;
        id = 0L;
        relativeBaseAddress = 0L;
        done = false;
        opCodes = new HashMap<>();
        inputValues = new ArrayDeque<>();
        outputValues = new ArrayDeque<>();
        debugValue = 0L;
        stopOnFirstTime = false;
        opCodes.put(1L,4L);
        opCodes.put(2L,4L);
        opCodes.put(3L,2L);
        opCodes.put(4L,2L);
        opCodes.put(5L,3L);
        opCodes.put(6L,3L);
        opCodes.put(7L,4L);
        opCodes.put(8L,4L);
        opCodes.put(9L,2L);
    }

    public void setInputQueue(ArrayDeque<Long> iqueue) {
        inputValues = iqueue;
    }

    public void setOutputQueue(ArrayDeque<Long> oqueue) {
        outputValues = oqueue;
    }
    public Long getDebugValue() {
        return this.debugValue;
    }

    public Boolean isDone() {
        return this.done;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIntCodeMemory(Long address, Long value) {
        intCode.put(address, value);
    }

    public void initializeIntCode(List<String> lines) {
        intCode = new HashMap<>();
        Long address = 0L;
        for(String value : lines.get(0).split(",")){
            intCode.put(address,Long.parseLong(value));
            address++;
        }
    }

    public void setInputValue(Long inputValue) {
        this.inputValues.add(inputValue);
    }

    public Pair<Boolean, Long> getInputValue() {
        if (this.inputValues.isEmpty()) {
            return Pair.of(false, -1L);
        }
        return Pair.of(true, this.inputValues.poll());

    }

    public void addOutputValue(Long outputValue) {
        this.outputValues.add(outputValue);
    }

    public Pair<Boolean, Long> getOutputValue() {
        if (this.outputValues.isEmpty()) {
            return Pair.of(false, -1L);
        }
        return Pair.of(true, this.outputValues.poll());
    }

    public void stopOnFirstOutput(Boolean stop) {
        stopOnFirstTime = stop;
    }


    public Long getMemoryLocation(Long location) {
        return intCode.get(location);
    }

    private ParameterModes updateMode(Long value) {
        if (value.equals(0L)) {
            return ParameterModes.positionMode;
        } else if (value.equals(1L)) {
            return ParameterModes.immediateMode;
        } else {
            return ParameterModes.relativeMode;
        }
    }

    public Long getMemoryAddress(Long offset, ParameterModes mode) {
        if (mode == ParameterModes.immediateMode) {
            return offset;
        }
        return (mode == ParameterModes.relativeMode) ? relativeBaseAddress + offset : offset;
    }
    public Long getMemoryValue(Long address, ParameterModes mode) {
        if (mode == ParameterModes.immediateMode) {
            return address;
        }
        return intCode.getOrDefault(address, 0L);
    }
    public static Integer SLEEP_TIME = 10;
    public void run() {
        Long currentInstructionWithMode = intCode.getOrDefault(programCounter,0L);
        Long currentInstruction = currentInstructionWithMode % 100;
        boolean incProgramCounter = true;
        done = false;
        while ((currentInstruction != 99) && (opCodes.containsKey(currentInstruction))) {
            incProgramCounter = true;
            Long aMode = (currentInstructionWithMode / 100L) % 10L;
            Long bMode = (currentInstructionWithMode / 1000L) % 10L;
            Long cMode = (currentInstructionWithMode / 10000L) % 10L;
            ParameterModes mode0 = updateMode(aMode);
            ParameterModes mode1 = updateMode(bMode);
            ParameterModes mode2 = updateMode(cMode);
            Long opPointer1 = intCode.getOrDefault(programCounter + 1, 0L);
            Long opPointer2 = intCode.getOrDefault(programCounter + 2, 0L);
            Long storePointer = intCode.getOrDefault(programCounter + 3, 0L);
            opPointer1 = getMemoryAddress(opPointer1, mode0);
            opPointer2 = getMemoryAddress(opPointer2, mode1);
            storePointer = getMemoryAddress(storePointer, mode2);
            Long value1 = getMemoryValue(opPointer1, mode0);
            Long value2 = getMemoryValue(opPointer2, mode1);
            if (currentInstruction.equals(1L)) {
                intCode.put(storePointer, value1 + value2);
            } else if (currentInstruction.equals(2L)) {
                intCode.put(storePointer, value1 * value2);
            } else if (currentInstruction.equals(3L)) {
                Pair<Boolean, Long> value = getInputValue();
                if (value.getLeft()) {
                    Long address = getMemoryAddress(opPointer1, mode0);
                    intCode.put(address, value.getRight());
                } else {
                    programCounter -= opCodes.get(3L);
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {

                    }
                }
            } else if (currentInstruction.equals(4L)) {
                Long outputValue = getMemoryValue(opPointer1, mode0);
                addOutputValue(outputValue);
                debugValue = outputValue;
            } else if (currentInstruction.equals(5L)) {
                if (!value1.equals(0L)) {
                    programCounter = value2;
                    incProgramCounter = false;
                }
            } else if (currentInstruction.equals(6L)) {
                if (value1.equals(0L)) {
                    programCounter = value2;
                    incProgramCounter = false;
                }
            } else if (currentInstruction.equals(7L)) {
                Long value = 0L;
                if (value1 < value2) {
                    value = 1L;
                }
                intCode.put(storePointer, value);
            } else if (currentInstruction.equals(8L)) {
                Long value = 0L;
                if (value1.equals(value2)) {
                    value = 1L;
                }
                intCode.put(storePointer, value);
            } else if (currentInstruction.equals(9L)) {
                relativeBaseAddress += opPointer1;
            }
            programCounter += incProgramCounter? opCodes.get(currentInstruction) : 0;
            currentInstructionWithMode = intCode.getOrDefault(programCounter, 0L);
            currentInstruction = currentInstructionWithMode % 100;
        }
        done = true;
    }
}
