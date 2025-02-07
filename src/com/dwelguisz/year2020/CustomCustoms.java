package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomCustoms extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,6,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Integer solutionPart1(List<String> lines) {
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

    private Long solutionPart2(List<String> lines) {
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
