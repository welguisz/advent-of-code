package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public class AoC2022Day21 extends AoCDay {
    public void solve() {
        System.out.println("Day 21 ready to go");
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day21/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Map<String, String> values = parsedLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(new HashMap<>(values), "root");
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(new HashMap<>(values));
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
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

    public Long monkeyOperation(Map<String, String> monkeys, String name, Long human) {
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
        Long operand1 = monkeyOperation(monkeys,operand1Str,human);
        Long operand2 = monkeyOperation(monkeys,operand2Str,human);
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

    public Long solutionPart1(Map<String, String> values, String keyToFind) {
        Map<String, Long> decoded = new HashMap<>();
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

    public Long solutionPart2(Map<String, String> values) {
        String rootExpr = values.get("root");
        String root1Str = rootExpr.substring(0,4);
        String root2Str = rootExpr.substring(7);
        if (!monkeyOperation(values, root2Str, 0L).equals(monkeyOperation(values, root2Str, 1L))) {
            String tmp = root1Str;
            root1Str = root2Str;
            root2Str = tmp;
        }
        Long target = monkeyOperation(values, root2Str, 0L);

        Long low = 0L;
        Long high = Long.MAX_VALUE;
        while (low < high) {
            Long mid = (low + high) / 2;
            Long score = target - monkeyOperation(values, root1Str, mid);
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
