package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.*;
import java.util.stream.Collectors;

public class HighEntropyPassphrases extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day04/input.txt");
        Integer part1 = solutionPart1(lines);
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines) {
        Integer count = 0;
        for(String line : lines) {
            List<String> allWords = Arrays.stream(line.split(" ")).collect(Collectors.toList());
            Set<String> uniqueWords = Arrays.stream(line.split(" ")).collect(Collectors.toSet());
            if (allWords.size() == uniqueWords.size()) {
                count++;
            }
        }
        return count;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer count = 0;
        for(String line : lines) {
            List<String> allWords = Arrays.stream(line.split(" ")).collect(Collectors.toList());
            Set<String> uniqueWords = Arrays.stream(line.split(" ")).collect(Collectors.toSet());
            if (allWords.size() == uniqueWords.size()) {
                if (!anagrams(allWords)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean anagrams(List<String> words) {
        for (int i = 0; i < words.size(); i++) {
            for (int j = i + 1; j < words.size(); j++) {
                if (allLettersSame(words.get(i), words.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allLettersSame(String word1, String word2) {
        List<String> letters1 = Arrays.stream(word1.split("")).collect(Collectors.toList());
        List<String> letters2 = Arrays.stream(word2.split("")).collect(Collectors.toList());
        Map<String, Integer> letterFrequency1 = new HashMap<>();
        Map<String, Integer> letterFrequency2 = new HashMap<>();
        for (String letter : letters1) {
            Integer count = letterFrequency1.getOrDefault(letter, 0);
            count++;
            letterFrequency1.put(letter,count);
        }
        for (String letter : letters2) {
            Integer count = letterFrequency2.getOrDefault(letter, 0);
            count++;
            letterFrequency2.put(letter,count);
        }
        for(Map.Entry<String, Integer> letterFreq1 : letterFrequency1.entrySet()) {
            Integer otherWordCount = letterFrequency2.getOrDefault(letterFreq1.getKey(),-1);
            if (otherWordCount != letterFreq1.getValue()) {
                return false;
            }
        }
        for(Map.Entry<String, Integer> letterFreq2 : letterFrequency2.entrySet()) {
            Integer otherWordCount = letterFrequency1.getOrDefault(letterFreq2.getKey(),-1);
            if (otherWordCount != letterFreq2.getValue()) {
                return false;
            }
        }
        return true;
    }
}
