package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.List;

public class ExplosiveInCyberspace extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day09/input.txt");
        Integer part1 = solutionPart1(lines.get(0));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Long part2 = solutionPart2(lines.get(0));
        System.out.println(String.format("Part 1 Answer: %d",part2));
    }

    public Integer solutionPart1(String line) {
        int length = 0;
        int currentPos = 0;
        int openParenthesesLength = 0;
        boolean inInstruction = false;
        while (currentPos < line.length()) {
            if (line.charAt(currentPos) == '(') {
                openParenthesesLength = currentPos;
                inInstruction = true;
                currentPos++;
            } else if (line.charAt(currentPos) == ')') {
                inInstruction = false;
                String tmp = line.substring(openParenthesesLength+1,currentPos);
                String[] vals = tmp.split("x");
                int repeatLength = Integer.parseInt(vals[0]);
                int repeatTime = Integer.parseInt(vals[1]);
                length += repeatLength * repeatTime;
                currentPos+=repeatLength+1;
            } else if (!inInstruction){
                length++;
                currentPos++;
            } else {
                currentPos++;
            }
        }
        return length;
    }

    public Long solutionPart2(String line) {
        Long length = 0L;
        int currentPos = 0;
        int openParenthesesLength = 0;
        boolean inInstruction = false;
        while (currentPos < line.length()) {
            if (line.charAt(currentPos) == '(') {
                openParenthesesLength = currentPos;
                inInstruction = true;
                currentPos++;
            } else if (line.charAt(currentPos) == ')') {
                inInstruction = false;
                String tmp = line.substring(openParenthesesLength+1,currentPos);
                String[] vals = tmp.split("x");
                int repeatLength = Integer.parseInt(vals[0]);
                int repeatTime = Integer.parseInt(vals[1]);
                String newStr = line.substring(currentPos+1,currentPos+1+repeatLength);
                length += solutionPart2(newStr) * repeatTime;
                currentPos+=repeatLength+1;
            } else if (!inInstruction){
                length++;
                currentPos++;
            } else {
                currentPos++;
            }
        }
        return length;
    }


}
