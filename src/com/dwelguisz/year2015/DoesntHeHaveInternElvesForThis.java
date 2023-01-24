package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DoesntHeHaveInternElvesForThis extends AoCDay {

    List<String> VOWELS = List.of("a", "e", "i", "o", "u");

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,5,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(instructions);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(instructions);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Long solutionPart1(List<String> strings) {
        return strings.stream().filter(str -> vowels(str))
                .filter(str->containsDoubleLetters(str))
                .filter(str->doesNotContainBadLetters(str)).count();
    }

    Long solutionPart2(List<String> strings) {
        return strings.stream()
                .filter(str -> containsDoubleLettersTwice(str))
                .filter(str -> letterSpace(str)).count();
    }

    public boolean vowels(String str) {
        return Arrays.stream(str.split("")).filter(chr -> VOWELS.contains(chr)).count() > 2;
    }

    public boolean containsDoubleLetters(String str) {
        char[] chars = str.toCharArray();
        return IntStream.range(0,str.length()-1).anyMatch(i -> chars[i] == chars[i+1]);
    }

    public boolean doesNotContainBadLetters(String str) {
        List<String> badSet = Arrays.asList("ab", "cd", "pq", "xy");
        List<String> twoLetterStrings = IntStream.range(0,str.length()-1).boxed().map(i -> str.substring(i,i+2)).collect(Collectors.toList());
        return !twoLetterStrings.stream().anyMatch(ss -> badSet.contains(ss));
    }

    public boolean containsDoubleLettersTwice(String str) {
        List<String> subStrings = IntStream.range(0,str.length()-1).boxed().map(i -> str.substring(i,i+2)).collect(Collectors.toList());
        for (int i = 0; i < subStrings.size(); i++) {
            for (int j = i + 2; j < subStrings.size(); j++) {
                if (subStrings.get(i).equals(subStrings.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean letterSpace(String str) {
        char[] chars = str.toCharArray();
        return IntStream.range(0,str.length()-2).anyMatch(i -> chars[i] == chars[i+2]);
    }
}
