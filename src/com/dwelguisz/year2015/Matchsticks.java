package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class Matchsticks extends AoCDay {

    static Pattern pattern = Pattern.compile("\\\\(\\\\|\\\"|x[0123456789abcdef]{2})");

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2015/day08/input.txt");
        Long part1 = solutionPart1(instructions);
        Long part2 = solutionPart2(instructions);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static Long solutionPart1(List<String> strings) {
        Long allChars = 0L;
        Long trueCount = 0L;
        for (String str: strings) {
            int allChar = str.length();
            int trueCountChar = allChar - 2 - matchedCharacters(str);
            allChars += allChar;
            trueCount += trueCountChar;
        }
        return allChars - trueCount;
    }

    public static Long solutionPart2(List<String> strings) {
        Long allChars = 0L;
        Long trueCount = 0L;
        for (String str: strings) {
            int allChar = str.length();
            int trueCountChar = allChar + 4 + matchedCharactersPart2(str);
            System.out.println("-----------------------");
            System.out.println(String.format("String: %s", str));
            System.out.println(String.format("First pass:  %d", allChar));
            System.out.println(String.format("Second pass: %d", trueCountChar));
            allChars += allChar;
            trueCount += trueCountChar;
        }
        return Math.abs(allChars - trueCount);
    }

    public static int matchedCharacters(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int diff = matcher.end() - matcher.start();
            count += diff - 1;
        }
        return count;
    }

    public static int matchedCharactersPart2(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String subString = str.substring(matcher.start(), matcher.end());
            if (subString.contains("\\x")) {
                count += 1;
            } else if (subString.equals("\\\"")) {
                count += 2;
            } else if (subString.equals("\\\\")) {
                count += 2;
            }
        }
        return count;
    }


}
