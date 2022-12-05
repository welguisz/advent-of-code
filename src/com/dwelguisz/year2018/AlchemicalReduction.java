package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AlchemicalReduction extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day05/input.txt");
        Integer part1 = solutionPart1(lines.get(0));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(lines.get(0));
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    public Integer solutionPart1(String line) {
        String polymer = line;
        boolean runOneMoreTime = true;
        while (runOneMoreTime) {
            Stack<Integer> deleteChars = new Stack<>();
            Boolean skip = false;
            for (int i = 0; i < polymer.length() - 1; i++) {
                if (skip) {
                    skip = false;
                    continue;
                }
                Character cur = polymer.charAt(i);
                Character nex = polymer.charAt(i + 1);
                if (Math.abs(cur - nex) == 32) {
                    deleteChars.push(i);
                    deleteChars.push(i+1);
                    skip = true;
                }
            }
            runOneMoreTime = !deleteChars.isEmpty();
            while(!deleteChars.isEmpty()) {
                Integer tmp = deleteChars.pop();
                polymer = polymer.substring(0,tmp) + polymer.substring(tmp+1);
            }
        }
        return polymer.length();
    }

    public Integer solutionPart2(String polymer) {
        Integer min = Integer.MAX_VALUE;
        for (char i = 65; i < 91; i++) {
            char lowercase = (char) (i + 32);
            Integer val = solutionPart1(polymer.replaceAll(String.valueOf(i),"").replaceAll(String.valueOf(lowercase),""));
            min = Integer.min(val, min);
        }
        return min;
    }

}
