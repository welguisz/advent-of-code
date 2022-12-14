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

    enum ParameterModes {
        positionMode,
        immediateMode
    };

    Boolean stopOnFirstTime;

    public IntCodeComputer()  {
        programCounter = 0L;
        id = 0L;
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
        if (value == 0) {
            return ParameterModes.positionMode;
        }
        return ParameterModes.immediateMode;
    }

    public static Integer SLEEP_TIME = 10;
    public void run() {
        Long currentInstructionWithMode = intCode.getOrDefault(programCounter,-1L);
        Long currentInstruction = currentInstructionWithMode % 100;
        boolean incProgramCounter = true;
        done = false;
        while ((currentInstruction != 99) && (opCodes.containsKey(currentInstruction))) {
            incProgramCounter = true;
            Long aMode = (currentInstructionWithMode / 100L) % 10L;
            Long bMode = (currentInstructionWithMode / 1000L) % 10L;
            ParameterModes mode0 = updateMode(aMode);
            ParameterModes mode1 = updateMode(bMode);
            Long opPointer1 = intCode.getOrDefault(programCounter + 1, -1L);
            Long opPointer2 = intCode.getOrDefault(programCounter + 2, -1L);
            Long storePointer = intCode.getOrDefault(programCounter + 3, -1L);
            Long value1 = (mode0 == ParameterModes.positionMode) ? intCode.getOrDefault(opPointer1, -1L) : opPointer1;
            Long value2 = (mode1 == ParameterModes.positionMode) ? intCode.getOrDefault(opPointer2, -1L) : opPointer2;
            if (currentInstruction.equals(1L)) {
                intCode.put(storePointer, value1 + value2);
            } else if (currentInstruction.equals(2L)) {
                intCode.put(storePointer, value1 * value2);
            } else if (currentInstruction.equals(3L)) {
                Pair<Boolean, Long> value = getInputValue();
                if (value.getLeft()) {
                    intCode.put(opPointer1, value.getRight());
                } else {
                    programCounter -= 2;
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {

                    }
                }
            } else if (currentInstruction.equals(4L)) {
                Long outputValue = (mode0 == ParameterModes.positionMode) ? intCode.getOrDefault(opPointer1, Long.MIN_VALUE) : opPointer1;
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
            }
            programCounter += incProgramCounter? opCodes.get(currentInstruction) : 0;
            currentInstructionWithMode = intCode.getOrDefault(programCounter, -1L);
            currentInstruction = currentInstructionWithMode % 100;
        }
        done = true;
    }
}
