package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MonkeyMath extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,21,false,0);
        Map<String, String> values = parsedLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values, false);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Map<String, String> parsedLines(List<String> lines) {
        Map<String,String> values = new HashMap<>();
        for (String l : lines) {
            String split[] = l.split(": ");
            values.put(split[0], split[1]);
        }
        return values;
    }

    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public Double monkeyOperationRecursion(Map<String, String> monkeys, String name, Double human) {
        String monkey = monkeys.get(name);
        if (name.equals("humn") && human >= 0) {
            return human;
        }
        if (isNumeric(monkey)) {
            return 1.0* Long.parseLong(monkey);
        }
        String operand1Str = monkey.substring(0,4);
        String operand2Str = monkey.substring(7);
        Character op = monkey.substring(5,6).toCharArray()[0];
        Double operand1 = monkeyOperationRecursion(monkeys,operand1Str,human);
        Double operand2 = monkeyOperationRecursion(monkeys,operand2Str,human);
        switch (op) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
        }
        return 0.0;
    }

    public Double monkeyOperation(Map<String, String> values, String keyToFind, Double humanValue) {
        Map<String, Double> decoded = new HashMap<>();
        if (humanValue >= 0) {
            decoded.put("humn",humanValue);
            values.remove("humn");
        }
        List<String> removeKeys = new ArrayList<>();
        for (Map.Entry<String, String>v : values.entrySet()) {
            if (isNumeric(v.getValue())) {
                decoded.put(v.getKey(), 1.0 * Long.parseLong(v.getValue()));
                removeKeys.add(v.getKey());
            }
        }
        while (!decoded.containsKey(keyToFind)) {
            removeKeys.stream().forEach(s -> values.remove(s));
            removeKeys = new ArrayList<>();
            for(Map.Entry<String, String> v : values.entrySet()) {
                String operation = v.getValue();
                String func1Str = operation.substring(0,4);
                String func2Str = operation.substring(7);
                String op = operation.substring(5,6);
                if (decoded.containsKey(func1Str) && decoded.containsKey(func2Str)) {
                    Double func1 = decoded.get(func1Str);
                    Double func2 = decoded.get(func2Str);
                    Double result = (op.equals("+")) ? func1 + func2 : (op.equals("-")) ? func1 - func2 :
                            (op.equals("*")) ? func1 * func2 : func1 / func2;
                    decoded.put(v.getKey(),result);
                    removeKeys.add(v.getKey());
                }
            }
        }
        return decoded.get(keyToFind);

    }
    public Double solutionPart1(Map<String, String> values, boolean recursion) {
        return useIterativeOrRecursion(new HashMap<>(values), "root", -1.0, recursion);
    }

    public Double useIterativeOrRecursion(Map<String,String> values, String keyToFind, Double humanValue, Boolean recursive) {
        if (recursive) {
            return monkeyOperationRecursion(new HashMap<>(values), keyToFind, humanValue);
        }
        return monkeyOperation(new HashMap<>(values), keyToFind, humanValue);
    }

    public Long solutionPart2(Map<String, String> values, Boolean recursive) {
        String rootExpr = values.get("root");
        String root1Str = rootExpr.substring(0,4);
        String root2Str = rootExpr.substring(7);
        if (!useIterativeOrRecursion(values, root2Str, 0.0, recursive).equals(useIterativeOrRecursion(values, root2Str, 1.0, recursive))) {
            String tmp = root1Str;
            root1Str = root2Str;
            root2Str = tmp;
        }
        Double target = useIterativeOrRecursion(values, root2Str, 0.0, recursive);

        Long low = 0L;
        Long high = Long.MAX_VALUE;
        while (low < high) {
            Long mid = (low + high) / 2;
            Double tmp = useIterativeOrRecursion(values, root1Str, 1.0 * mid, recursive);
            Double score = target - tmp;
            if (score < 0) {
                low = mid;
            } else if (score == 0) {
                return mid;
            } else {
                high = mid;
            }
        }
        return 0L;
    }
}
