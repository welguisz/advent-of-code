package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class RedNosedReports extends AoCDay {
    Set<Integer> NEGATIVE_STEPS = Set.of(-1,-2,-3);
    Set<Integer> POSITIVE_STEPS = Set.of(1,2,3);

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 2, false, 0);
        List<List<Integer>> reports = convertToReports(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(reports);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(reports);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<List<Integer>> convertToReports(List<String> lines) {
        List<List<Integer>> reports = new ArrayList<>();
        for (String line : lines) {
            List<Integer> values = Arrays.stream(line.split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
            reports.add(values);
        }
        return reports;
    }

    public long solutionPart1(List<List<Integer>> reports) {
        return reports.stream().filter(this::safePart1).count();
    }

    public boolean safePart1(List<Integer> values) {
        List<Integer> diff = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            diff.add(values.get(i) - values.get(i+1));
        }
        if (diff.stream().allMatch(i -> i < 0) || diff.stream().allMatch(i -> i > 0)) {
            return diff.stream().allMatch(i -> Math.abs(i) > 0 && Math.abs(i) < 4);
        }
        return false;
    }

    public long solutionPart2(List<List<Integer>> reports) {
        List<List<Integer>> moreCheck = reports.stream().filter(r -> !safePart1(r)).toList();
        long knownGood = reports.size() - moreCheck.size();
        List<List<Integer>> needsMoreWork = moreCheck.stream().filter(r -> safePart2(r) != elegantSafePart2(r)).toList();
        long moreKnownGood = needsMoreWork.stream().filter(this::elegantSafePart2).count();
        return knownGood + moreKnownGood;
    }

    public boolean elegantSafePart2(List<Integer> values) {
        List<Integer> diff = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            diff.add(values.get(i) - values.get(i+1));
        }
        Map<Integer, Long> counter = diff.stream().collect(Collectors.groupingBy(key -> key, Collectors.counting()));
        long negativeCount = 0l;
        long zeroCount = 0l;
        long positiveCount = 0l;
        for (Map.Entry<Integer, Long> entry : counter.entrySet()) {
            if (entry.getKey() < 0) {
                negativeCount += entry.getValue();
            } else if (entry.getKey() > 0) {
                positiveCount += entry.getValue();
            } else {
                zeroCount += entry.getValue();
            }
        }
        long nonNegativeCount = positiveCount + zeroCount;
        long nonPositiveCount = negativeCount + zeroCount;
        //If there are more than 1 nonNegativeCount and more than 1 nonPositiveCount, then this can't be valid at all
        if (nonNegativeCount > 1 && nonPositiveCount > 1) {
            return false;
        }
        Set<Integer> allowedDiffs = (nonNegativeCount > 1) ? POSITIVE_STEPS : NEGATIVE_STEPS;
        boolean hitOutOfBounds = false;
        for (int i = 0; i < diff.size(); i++) {
            if (allowedDiffs.contains(diff.get(i))) {
                allowedDiffs = (nonNegativeCount > 1) ? POSITIVE_STEPS : NEGATIVE_STEPS;
            } else {
                if (hitOutOfBounds) {
                    return false;
                } else {
                    hitOutOfBounds = true;
                    if (i != 0) {
                        final Integer delta = diff.get(i) * -1;
                        Set<Integer> tempSet = (nonNegativeCount > 1) ? POSITIVE_STEPS : NEGATIVE_STEPS;
                        allowedDiffs = tempSet.stream().map(dif -> dif + delta).collect(Collectors.toSet());
                    } else {
                        Integer tmp = diff.get(0) + diff.get(1);
                        if (!allowedDiffs.contains(tmp)) {
                            return false;
                        } else {
                            i++;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean safePart2(List<Integer> values) {
        for (int i = 0; i < values.size(); i++) {
            List<Integer> newValues = new ArrayList<>(values);
            newValues.remove(i);
            if (safePart1(newValues)) {
                return true;
            }
        }
        return false;
    }

}
