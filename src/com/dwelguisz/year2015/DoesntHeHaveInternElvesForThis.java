package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoesntHeHaveInternElvesForThis extends AoCDay {

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2015/day05/input.txt");
        Integer part1 = solutionPart1(instructions);
        Integer part2 = solutionPart2(instructions);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static int solutionPart1(List<String> strings) {
        List<String> nice = strings.stream().filter(str -> vowels(str))
                .filter(str->containsDoubleLetters(str))
                .filter(str->doesNotContainBadLetters(str))
                .collect(Collectors.toList());
        return nice.size();
    }

    public static int solutionPart2(List<String> strings) {
        List<String> nice = strings.stream()
                .filter(str -> containsDoubleLettersTwice(str))
                .filter(str -> letterSpace(str))
                .collect(Collectors.toList());
        return nice.size();
    }

    public static boolean vowels(String str) {
        List<String> vowels = Arrays.asList("a", "e", "i", "o", "u");
        List<String> vowelsInStr = Arrays.stream(str.split("")).filter(chr -> vowels.contains(chr)).collect(Collectors.toList());
        return (vowelsInStr.size() > 2);
    }

    public static boolean containsDoubleLetters(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            String sub1 = str.substring(i, i+1);
            String sub2 = str.substring(i+1, i+2);
            if (sub1.equals(sub2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesNotContainBadLetters(String str) {
        List<String> badSet = Arrays.asList("ab", "cd", "pq", "xy");
        for (int i = 0; i < str.length() - 1; i++) {
            String sub = str.substring(i, i+2);
            if (badSet.contains(sub)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsDoubleLettersTwice(String str) {
        List<String> subStrings = new ArrayList<>();
        for (int i = 0; i < str.length() - 1; i++) {
            String sub = str.substring(i, i+2);
            subStrings.add(sub);
        }
        for (int i = 0; i < subStrings.size(); i++) {
            String str1 = subStrings.get(i);
            for (int j = i + 2; j < subStrings.size(); j++) {
                String str2 = subStrings.get(j);
                if (str1.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean letterSpace(String str) {
        for (int i = 0; i < str.length() - 2; i++) {
            String sub1 = str.substring(i, i+1);
            String sub2 = str.substring(i+1, i+2);
            String sub3 = str.substring(i+2, i+3);
            if (sub1.equals(sub3)) {
                return true;
            }
        }
        return false;
    }
}
