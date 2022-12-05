package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SupplyStacks extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day05/input.txt");
        String part1 = solutionPart1(lines, createStacks());
        System.out.println(String.format("Part 1 Answer: %s",part1));
        String part2 = solutionPart2(lines, createStacks());
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

//            [N] [G]                     [Q]
//            [H] [B]         [B] [R]     [H]
//            [S] [N]     [Q] [M] [T]     [Z]
//            [J] [T]     [R] [V] [H]     [R] [S]
//            [F] [Q]     [W] [T] [V] [J] [V] [M]
//            [W] [P] [V] [S] [F] [B] [Q] [J] [H]
//            [T] [R] [Q] [B] [D] [D] [B] [N] [N]
//            [D] [H] [L] [N] [N] [M] [D] [D] [B]
//             1   2   3   4   5   6   7   8   9

    public List<Stack<String>> createStacks() {
        List<String> preStack = new ArrayList<>();
        preStack.add("DTWFJSHN");
        preStack.add("HRPQTNBG");
        preStack.add("LQV");
        preStack.add("NBSWRQ");
        preStack.add("NDFTVMB");
        preStack.add("MDBVHTR");
        preStack.add("DBQJ");
        preStack.add("DNJVRZHQ");
        preStack.add("BNHMS");
        List<Stack<String>> finalStack = new ArrayList<>();
        for (String s : preStack) {
            Stack<String> stack = new Stack<>();
            List<String> tmp = Arrays.stream(s.split("")).collect(Collectors.toList());
            for (String t : tmp) {
                stack.push(t);
            }
            finalStack.add(stack);
        }
        return finalStack;
    }

    public String solutionPart1 (List<String>lines, List<Stack<String>> stacks) {
        List<Stack<String>> curStack = stacks;
        for (String line : lines) {
            String w[] = line.split(" ");
            Integer blocks = Integer.parseInt(w[1]);
            Integer from = Integer.parseInt(w[3])-1;
            Integer to = Integer.parseInt(w[5])-1;
            for(int i = 0; i < blocks; i++) {
                curStack.get(to).push(curStack.get(from).pop());
            }
        }
        String returnStr = "";
        for (Stack<String> c : curStack) {
            returnStr += c.pop();
        }
        return returnStr;
    }

    public String solutionPart2(List<String> lines,  List<Stack<String>> stacks) {
        List<Stack<String>> curStack = stacks;
        Integer lineNumber = 0;
        for (String line : lines) {
            String w[] = line.split(" ");
            Integer blocks = Integer.parseInt(w[1]);
            Integer from = Integer.parseInt(w[3])-1;
            Integer to = Integer.parseInt(w[5])-1;
            Stack<String> tmp = new Stack<>();
            for(int i = 0; i < blocks; i++) {
                tmp.push(curStack.get(from).pop());
            }
            for (int i = 0; i < blocks; i++) {
                curStack.get(to).push(tmp.pop());
            }
            lineNumber++;
        }
        String returnStr = "";
        for (Stack<String> c : curStack) {
            returnStr += c.pop();
        }
        return returnStr;
    }
}
