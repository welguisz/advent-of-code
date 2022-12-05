package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class SupplyStacks extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day05/input.txt");
        String part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        String part2 = solutionPart2(lines);
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

    public List<Stack<String>> createStacks(List<String> lines) {
        List<Stack<String>> finalStack = new ArrayList<>();
        Stack<String> rows = new Stack<>();
        String keyMap = "";
        for (String line : lines) {
            if (line.contains("1")) {
                keyMap = line;
                break;
            }
            rows.push(line);
        }
        Map<Integer,Integer> map = new HashMap<>();
        List<Character> integerMap = List.of('1','2','3','4','5','6','7','8','9');
        for (int i = 0; i < keyMap.length(); i++) {
            if (integerMap.contains(keyMap.charAt(i))) {
                map.put(i,Integer.parseInt(keyMap.substring(i,i+1)));
            }
        }
        for (int i = 0; i < map.size(); i++) {
            finalStack.add(new Stack<>());
        }
        while (!rows.isEmpty()) {
            String current = rows.pop();
            for (Map.Entry<Integer, Integer> loc : map.entrySet()) {
                Integer stackNumber = loc.getValue();
                Integer charPosition = loc.getKey();
                if (charPosition > current.length()) {
                    continue;
                }
                String tmp = current.substring(charPosition,charPosition+1);
                if (!tmp.equals(" ")) {
                    finalStack.get(stackNumber-1).push(tmp);
                }
            }
        }
        return finalStack;
    }

    public String solutionPart1 (List<String>lines) {
        List<Stack<String>> curStack = createStacks(lines);
        for (String line : lines) {
            if (line.contains("move")) {
                String w[] = line.split(" ");
                Integer blocks = Integer.parseInt(w[1]);
                Integer from = Integer.parseInt(w[3]) - 1;
                Integer to = Integer.parseInt(w[5]) - 1;
                for (int i = 0; i < blocks; i++) {
                    curStack.get(to).push(curStack.get(from).pop());
                }
            }
        }
        String returnStr = "";
        for (Stack<String> c : curStack) {
            returnStr += c.pop();
        }
        return returnStr;
    }

    public String solutionPart2(List<String> lines) {
        List<Stack<String>> curStack = createStacks(lines);
        for (String line : lines) {
            if (line.contains("move")) {
                String w[] = line.split(" ");
                Integer blocks = Integer.parseInt(w[1]);
                Integer from = Integer.parseInt(w[3]) - 1;
                Integer to = Integer.parseInt(w[5]) - 1;
                Stack<String> tmp = new Stack<>();
                for (int i = 0; i < blocks; i++) {
                    tmp.push(curStack.get(from).pop());
                }
                for (int i = 0; i < blocks; i++) {
                    curStack.get(to).push(tmp.pop());
                }
            }
        }
        String returnStr = "";
        for (Stack<String> c : curStack) {
            returnStr += c.pop();
        }
        return returnStr;
    }
}
