package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AoC2022Day21 extends AoCDay {
    public void solve() {
        System.out.println("Day 21 ready to go");
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day21/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Map<String, String> values = parsedLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1Iterative = solutionPart1(values, false);
        Long part1Recursion = solutionPart1(values, true);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2Iterative = solutionPart2(values, false);
        Long part2Recursion = solutionPart2(values, true);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Iterative Answer: %d",part1Iterative));
        System.out.println(String.format("Part 2 Recursion Answer: %d",part1Recursion));
        System.out.println(String.format("Part 2 Iterative Answer: %d",part2Iterative));
        System.out.println(String.format("Part 2 Recursion Answer: %d",part2Recursion));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
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

    public Long monkeyOperationRecursion(Map<String, String> monkeys, String name, Long human) {
        String monkey = monkeys.get(name);
        if (name.equals("humn") && human >= 0) {
            return human;
        }
        if (isNumeric(monkey)) {
            return Long.parseLong(monkey);
        }
        String operand1Str = monkey.substring(0,4);
        String operand2Str = monkey.substring(7);
        Character op = monkey.substring(5,6).toCharArray()[0];
        Long operand1 = monkeyOperationRecursion(monkeys,operand1Str,human);
        Long operand2 = monkeyOperationRecursion(monkeys,operand2Str,human);
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
        return 0L;
    }

    public Long monkeyOperation(Map<String, String> values, String keyToFind, Long humanValue) {
        Map<String, Long> decoded = new HashMap<>();
        if (humanValue >= 0) {
            decoded.put("humn",humanValue);
            values.remove("humn");
        }
        List<String> removeKeys = new ArrayList<>();
        for (Map.Entry<String, String>v : values.entrySet()) {
            if (isNumeric(v.getValue())) {
                decoded.put(v.getKey(), Long.parseLong(v.getValue()));
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
                    Long func1 = decoded.get(func1Str);
                    Long func2 = decoded.get(func2Str);
                    Long result = (op.equals("+")) ? func1 + func2 : (op.equals("-")) ? func1 - func2 :
                            (op.equals("*")) ? func1 * func2 : func1 / func2;
                    decoded.put(v.getKey(),result);
                    removeKeys.add(v.getKey());
                }
            }
        }
        return decoded.get(keyToFind);

    }
    public Long solutionPart1(Map<String, String> values, boolean recursion) {
        return useIterativeOrRecursion(new HashMap<>(values), "root", -1L, recursion);
    }

    public Long useIterativeOrRecursion(Map<String,String> values, String keyToFind, Long humanValue, Boolean recursive) {
        if (recursive) {
            return monkeyOperationRecursion(new HashMap<>(values), keyToFind, humanValue);
        }
        return monkeyOperation(new HashMap<>(values), keyToFind, humanValue);
    }

    public Long solutionPart2(Map<String, String> values, Boolean recursive) {
        String rootExpr = values.get("root");
        String root1Str = rootExpr.substring(0,4);
        String root2Str = rootExpr.substring(7);
        if (!useIterativeOrRecursion(values, root2Str, 0L, recursive).equals(useIterativeOrRecursion(values, root2Str, 1L, recursive))) {
            String tmp = root1Str;
            root1Str = root2Str;
            root2Str = tmp;
        }
        Long target = useIterativeOrRecursion(values, root2Str, 0L, recursive);

        Long low = 0L;
        Long high = Long.MAX_VALUE;
        while (low < high) {
            Long mid = (low + high) / 2;
            Long tmp = useIterativeOrRecursion(values, root1Str, mid, recursive);
            Long score = target - tmp;
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
