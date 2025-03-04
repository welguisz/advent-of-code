package com.dwelguisz.year2019.IntCodeComputer;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class IntCodeComputer extends Thread implements Runnable {
    public Long instructionPointer;
    public Map<Long, Long> intCode;
    //Stores the opcodes and the jump for position
    public Map<Long, Long> opCodes;
    Map<Long, Integer> storeParamMap;
    public ArrayDeque<Long> inputValues;
    public ArrayDeque<Long> outputValues;
    Long id;
    public Boolean done;
    private volatile boolean running = true;
    private boolean exit;
    Long debugValue;

    Long relativeBaseAddress;

    public enum ParameterModes {
        positionMode,
        immediateMode,
        relativeMode
    };

    Boolean stopOnFirstTime;

    public IntCodeComputer()  {
        instructionPointer = 0L;
        id = 0L;
        exit = false;
        relativeBaseAddress = 0L;
        done = false;
        opCodes = new HashMap<>();
        inputValues = new ArrayDeque<>();
        outputValues = new ArrayDeque<>();
        debugValue = 0L;
        stopOnFirstTime = false;
        opCodes = Map.of(1L,4L,2L,4L,3L,2L,4L,2L,5L,3L,6L,3L,7L,4L,8L,4L,9L,2L);
        storeParamMap = Map.of(1L, 3, 2L, 3, 3L, 1, 4L, -1, 5L, 2, 6L, 2, 7L, 3, 8L, 3);
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

    public static Integer SLEEP_TIME = 1;

    public List<ParameterModes> findModes(Long opCode) {
        List<ParameterModes> modes = new ArrayList<>();
        Long divisor = 100L;
        for (int i = 0; i < 3; i++) {
            Long mode = (opCode / divisor) % 10L;
            modes.add(updateMode(mode));
            divisor *= 10L;
        }
        return modes;
    }

    Long getNewAddress(ParameterModes mode, Long value) {
        return getNewAddress(mode, value, false);
    }

    Long getNewAddress(ParameterModes mode, Long value, Boolean write) {
        if (mode == ParameterModes.positionMode) {
            if (write) {
                return value;
            }
            return intCode.getOrDefault(value, 0L);
        } else if (mode == ParameterModes.relativeMode) {
            if (write) {
                return relativeBaseAddress + value;
            }
            return relativeBaseAddress + intCode.getOrDefault(value, 0L);
        } else {
            throw new RuntimeException("Unexpected Address mode: " + mode);
        }
    }


    Long getOperand(ParameterModes mode, Long value) {
        if (mode == ParameterModes.immediateMode) {
            return intCode.getOrDefault(value, 0L);
        } else {
            Long newAddress = getNewAddress(mode, value);
            if (newAddress < 0) {
                throw new ArrayIndexOutOfBoundsException("Address is negative: " +  newAddress);
            }
            return intCode.getOrDefault(newAddress, 0L);
        }
    }

    public void doCalculation(Long instr, List<ParameterModes> modes) {
        if (instr.equals(1L)) {
            Long value = TwoInputOperand(modes,(a,b) -> a+b);
            Long address = getNewAddress(modes.get(2), intCode.getOrDefault(instructionPointer+3,0L), true);
            intCode.put(address, value);
            instructionPointer += opCodes.get(instr);
        } else if (instr.equals(2L)) {
            Long value = TwoInputOperand(modes,(a,b) -> a*b);
            Long address = getNewAddress(modes.get(2), intCode.getOrDefault(instructionPointer+3,0L), true);
            intCode.put(address, value);
            instructionPointer += opCodes.get(instr);
        } else if (instr.equals(3L)) {
            Pair<Boolean, Long> res = getInputValue();
            if (res.getLeft()) {
                Long address = getNewAddress(modes.get(0), intCode.getOrDefault(instructionPointer+1,0L), true);
                intCode.put(address, res.getRight());
                instructionPointer += opCodes.get(instr);
            } else {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {

                }
            }
        } else if (instr.equals(4L)) {
            Long outputValue = getOperand(modes.get(0), instructionPointer + 1);
            addOutputValue(outputValue);
            debugValue = outputValue;
            instructionPointer += opCodes.get(instr);
        } else if (instr.equals(5L)) {
            instructionPointer = TwoInputOperand(modes, (a,b) -> !a.equals(0L) ? b : instructionPointer + opCodes.get(instr));
        } else if (instr.equals(6L)) {
            instructionPointer = TwoInputOperand(modes, (a,b) -> a.equals(0L) ? b : instructionPointer + opCodes.get(instr));
        } else if (instr.equals(7L)) {
            Long result = TwoInputOperand(modes, (a,b) -> a < b ? 1L : 0L);
            Long address = getNewAddress(modes.get(2), intCode.getOrDefault(instructionPointer+3,0L), true);
            instructionPointer += opCodes.get(instr);
            intCode.put(address, result);
        } else if (instr.equals(8L)) {
            Long result = TwoInputOperand(modes, (a,b) -> a.equals(b) ? 1L : 0L);
            Long address = getNewAddress(modes.get(2), intCode.getOrDefault(instructionPointer+3,0L), true);
            instructionPointer += opCodes.get(instr);
            intCode.put(address, result);
        } else if (instr.equals(9L)) {
            relativeBaseAddress += getOperand(modes.get(0),instructionPointer+1);
            instructionPointer += opCodes.get(instr);
        }
    }

    Long TwoInputOperand(List<ParameterModes> modes, BinaryOperator<Long> func) {
        Long operand1 = getOperand(modes.get(0), instructionPointer+1);
        Long operand2 = getOperand(modes.get(1), instructionPointer+2);
        return func.apply(operand1, operand2);
    }

    public void run() {
        Long currentInstructionWithMode = intCode.getOrDefault(instructionPointer,0L);
        Long currentInstruction = currentInstructionWithMode % 100;
        done = false;
        while ((currentInstruction != 99) && (opCodes.containsKey(currentInstruction)) && !Thread.interrupted() && running) {
            List<ParameterModes> modes = findModes(currentInstructionWithMode);
            doCalculation(currentInstruction, modes);
            currentInstructionWithMode = intCode.getOrDefault(instructionPointer, 0L);
            currentInstruction = currentInstructionWithMode % 100;
        }
        done = true;
        exit = true;
    }

    public void shutdown() {
        running = false;
    }

}
