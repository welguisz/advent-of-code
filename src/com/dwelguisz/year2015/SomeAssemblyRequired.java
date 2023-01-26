package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class SomeAssemblyRequired extends AoCDay {

    static List<String> ALLOWED_INSTRUCTIONS = List.of ("AND", "OR", "LSHIFT", "RSHIFT", "NOT");
    static List<String> ONE_OP_INSTRUCTION = List.of("SET", "NOT");
    static Pattern pattern = Pattern.compile("\\d+");

    public static class LogicGate {
        String instruction;
        String op1;
        String op2;
        String result;
        public LogicGate(String instruction, String op1, String op2, String result) {
            this.instruction = instruction;
            this.op1 = op1;
            this.op2 = op2;
            this.result = result;
        }

        public Boolean ableToProcess(Map<String, Integer> values) {
            if (ONE_OP_INSTRUCTION.contains(instruction)) {
                return values.containsKey(op1) || isNumber(op1);
            } else {
                return (values.containsKey(op1) || isNumber(op1)) && (values.containsKey(op2) || isNumber(op2));
            }
        }

        public Map<String, Integer> process(Map<String, Integer> values) {
            if (values.containsKey(result)) {
                return values;
            }
            if (instruction.equals("SET")) {
                values.put(result, getValue(op1, values));
            } else if (instruction.equals("NOT")) {
                values.put(result, make16bit(~getValue(op1, values)));
            } else if (instruction.equals("AND")) {
                values.put(result, make16bit(getValue(op1, values) & getValue(op2, values)));
            } else if (instruction.equals("OR")) {
                values.put(result, make16bit(getValue(op1, values) | getValue(op2, values)));
            } else if (instruction.equals("LSHIFT")) {
                values.put(result, make16bit(getValue(op1, values) << getValue(op2, values)));
            } else if (instruction.equals("RSHIFT")) {
                values.put(result, make16bit(getValue(op1, values) >> getValue(op2, values)));
            }
            return values;
        }

        Integer make16bit(int x) {
            return x & 65535;
        }

        Integer getValue(String instruction, Map<String, Integer> values) {
            String yStr = instruction;
            if (isNumber(yStr)) {
                return parseInt(yStr);
            }
            return values.get(yStr);
        }

        boolean isNumber(String str) {
            return pattern.matcher(str).matches();
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,7,false,0);
        List<LogicGate> logicGates = parseLines(instructions);
        timeMarkers[1] = Instant.now().toEpochMilli();
        Map<String, Integer> values = new HashMap<>();
        part1Answer = part1(logicGates, values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        Map<String, Integer> newValues = new HashMap<>();
        newValues.put("b", (Integer) part1Answer);
        part2Answer = part1(logicGates, newValues);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<LogicGate> parseLines(List<String> lines) {
        List<LogicGate> logicGates = new ArrayList<>();
        for (String l : lines) {
            String puts[] = l.split(" -> ");
            String output = puts[1];
            String input[] = puts[0].split(" ");
            Integer instructionLocation = -1;
            for (int i = 0; i < input.length; i++) {
                if (ALLOWED_INSTRUCTIONS.contains(input[i])) {
                    instructionLocation = i;
                    break;
                }
            }
            if (instructionLocation == -1) {
                logicGates.add(new LogicGate("SET", input[0], "",output));
            } else if (instructionLocation == 0) { // NOT
                logicGates.add(new LogicGate("NOT", input[1], "", output));
            } else if (instructionLocation == 1) { // AND, OR, LSHIFT, RSHIFT
                logicGates.add(new LogicGate(input[1], input[0], input[2], output));
            }
        }
        return logicGates;
    }

    Integer part1(List<LogicGate> logicGates, Map<String, Integer> values) {
        while (!logicGates.isEmpty()) {
            List<LogicGate> unknownLogicGates = new ArrayList<>();
            for(LogicGate logicGate : logicGates) {
                if (logicGate.ableToProcess(values)) {
                    values = logicGate.process(values);
                } else {
                    unknownLogicGates.add(logicGate);
                }
            }
            logicGates = unknownLogicGates;
        }
        return values.get("a");
    }



}
