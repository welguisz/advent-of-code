package com.dwelguisz.year2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class CustomCustoms {
    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day06/input.txt");
        Integer part1 = solutionPart1(lines);
        Long part2 = solutionPart2(lines);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part2: %d",part2));
    }

    public static Integer solutionPart1(List<String> lines) {
        Set<String> groupAnswers = new HashSet<>();
        Integer runningSum = 0;
        for (String line : lines) {
            if (line.length() == 0) {
                runningSum += groupAnswers.size();
                groupAnswers = new HashSet<>();
            } else {
                Set<String> lineAnswer = Arrays.stream(line.split("")).collect(Collectors.toSet());
                groupAnswers.addAll(lineAnswer);
            }
        }
        if (!groupAnswers.isEmpty()) {
            runningSum += groupAnswers.size();
        }
        return runningSum;
    }

    public static Long solutionPart2(List<String> lines) {
        Map<String, Integer> groupAnswers = new HashMap<>();
        Integer totalPeopleinGroup = 0;
        Long runningSum = 0L;
        for (String line : lines) {
            if (line.length() == 0) {
                final Integer tpg = totalPeopleinGroup;
                runningSum += groupAnswers.values().stream().filter(val -> val == tpg).count();
                groupAnswers = new HashMap<>();
                totalPeopleinGroup = 0;
            } else {
                Set<String> lineAnswer = Arrays.stream(line.split("")).collect(Collectors.toSet());
                for(String answer : lineAnswer) {
                    groupAnswers.merge(answer, 1, Integer::sum);
                }
                totalPeopleinGroup++;
            }
        }
        if (!groupAnswers.isEmpty()) {
            final Integer tpg = totalPeopleinGroup;
            runningSum += groupAnswers.values().stream().filter(val -> val == tpg).count();
        }
        return runningSum;

    }

}
