package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.util.List;

import static java.lang.Integer.parseInt;

public class PasswordPhilosophy extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day02/input.txt");
        validPassword("1-3 a: abcde",true);
        validPassword("1-3 b: cdefg", true);
        validPassword("2-9 c: cccccccccc", true);
        Long part1 = lines.stream().filter(str -> validPassword(str, false)).count();
        Long part2 = lines.stream().filter(str -> validPassword(str, true)).count();
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part2: %d",part2));
    }

    private boolean validPassword(String str, boolean part2) {
        String[] splitLine = str.split(": ");
        String password = splitLine[1];
        char requiredChar = splitLine[0].split(" ")[1].charAt(0);
        Integer minRequiredChar = parseInt(splitLine[0].split(" ")[0].split("-")[0]);
        Integer maxRequiredChar = parseInt(splitLine[0].split(" ")[0].split("-")[1]);
        if (part2) {
            return validPasswordPart2(password, requiredChar, minRequiredChar, maxRequiredChar);
        } else {
            return validPasswordPart1(password, requiredChar, minRequiredChar, maxRequiredChar);
        }
    }

    private boolean validPasswordPart1(String password, char requiredChar, Integer minRequireChar, Integer maxRequiredChar) {
        Long requiredCharCount = password.chars().filter(ch -> ch == requiredChar).count();
        return ((minRequireChar <= requiredCharCount) && (requiredCharCount <= maxRequiredChar));
    }

    private boolean validPasswordPart2(String password, char requiredChar, Integer minRequireChar, Integer maxRequiredChar) {
        char charAtMinLoc = password.charAt(minRequireChar-1);
        char charAtMaxLoc = password.charAt(maxRequiredChar-1);
        Integer value = 0;
        if (requiredChar == charAtMinLoc) {
            value++;
        }
        if (requiredChar == charAtMaxLoc) {
            value++;
        }
        return value == 1;
    }
}
