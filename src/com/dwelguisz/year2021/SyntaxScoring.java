package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntaxScoring extends AoCDay {

    private List<String> openingChars;
    private List<String> closingChars;
    private  Map<String, String> charMap;
    private Map<String, Integer> scoreMap;

    public SyntaxScoring() {
        openingChars = Arrays.asList("(","[","{","<");
        closingChars = Arrays.asList(")","]","}",">");
        charMap = new HashMap<>();
        charMap.put("(",")");
        charMap.put("{","}");
        charMap.put("[","]");
        charMap.put("<",">");
        scoreMap = new HashMap<>();
        scoreMap.put(")", 3);
        scoreMap.put("}", 57);
        scoreMap.put("]", 1197);
        scoreMap.put(">", 25137);
        scoreMap.put("(", 1);
        scoreMap.put("[", 2);
        scoreMap.put("{", 3);
        scoreMap.put("<", 4);
    }
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day10/input.txt");
        Long part1 = parseLines(lines, false);
        Long part2 = parseLines(lines, true);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    private Long parseLines(List<String> lines, boolean part2) {
        Long total = 0L;
        List<Long> scores = new ArrayList<>();
        for (String line : lines) {
            Long temp = readLine(line, part2);
            if (part2) {
                if (temp != 0L) {
                    scores.add(temp);
                }
            } else {
                total += readLine(line, part2);
            }
        }
        if (!part2) {
            return total;
        }
        Collections.sort(scores);
        return scores.get(scores.size()/2);
    }

    private Long readLine(String line, boolean part2) {
        List<String> stack = new ArrayList<>();
        List<String> chars = Arrays.asList(line.split(""));
        for(String chr : chars) {
            if (openingChars.contains(chr)) {
                stack.add(chr);
            }
            else if (closingChars.contains(chr)) {
                String lastOpen = stack.remove(stack.size()-1);
                if (!charMap.get(lastOpen).equals(chr)) {
                    return part2 ? 0L : scoreMap.get(chr);
                }
            }
        }
        if (!part2) {
            return 0L;
        }
        Long total = 0L;
        while (!stack.isEmpty()) {
            total *= 5;
            String chr = stack.remove(stack.size() - 1);
            total += scoreMap.get(chr);
        }
        return total;
    }
}
