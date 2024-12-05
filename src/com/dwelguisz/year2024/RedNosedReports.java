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
            diff.add(values.get(i+1) - values.get(i));
        }
        if (diff.stream().allMatch(i -> i < 0) || diff.stream().allMatch(i -> i > 0)) {
            return diff.stream().allMatch(i -> Math.abs(i) > 0 && Math.abs(i) < 4);
        }
        return false;
    }

    public long solutionPart2(List<List<Integer>> reports) {
        List<List<Integer>> moreCheck = reports.stream().filter(r -> !safePart1(r)).toList();
        long knownGood = reports.size() - moreCheck.size();
        long moreKnownGood = moreCheck.stream().filter(this::elegantSafePart2).count();
        return knownGood + moreKnownGood;
    }

    public boolean elegantSafePart2(List<Integer> values) {
        List<Integer> diff = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            diff.add(values.get(i+1) - values.get(i));
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
        Stack<Integer> stackDiff = new Stack<>();
        Collections.reverse(diff);
        diff.forEach(stackDiff::push);
        boolean usePositiveSteps = nonNegativeCount > 1;
        Set<Integer> allowedDiffs = usePositiveSteps ? POSITIVE_STEPS : NEGATIVE_STEPS;
        boolean hitOutOfBounds = false;
        boolean first = true;
        while (!stackDiff.isEmpty()) {
            Integer currentNum = stackDiff.pop();
            if (allowedDiffs.contains(currentNum)) {
                allowedDiffs = usePositiveSteps ? POSITIVE_STEPS : NEGATIVE_STEPS;
            } else {
                if (hitOutOfBounds) {
                    return false;
                }
                hitOutOfBounds = true;
                if (!first) {
                    allowedDiffs = allowedDiffs.stream().map(t -> t - currentNum).collect(Collectors.toSet());
                } else {
                    //The first number popped is special.  With this special number, we have to figure out if we
                    //need to ignore the first value or the second value.
                    //Git rid of the first value since we don't know if it is good. We are going to create a new
                    //number.
                    stackDiff.pop();
                    //if values[2] - values[1] is in the allowed set, push that since we know it is valid
                    //else we push values[2] - values[0]. If this is invalid, then we know the report is bad.

                    int diff1 = values.get(2) - values.get(1);
                    int diff2 = values.get(2) - values.get(0);
                    if (allowedDiffs.contains(diff1)) {
                        stackDiff.push(diff1);
                    } else {
                        stackDiff.push(diff2);
                    }
                }
            }
            first = false;
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
