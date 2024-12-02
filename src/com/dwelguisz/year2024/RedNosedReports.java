package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RedNosedReports extends AoCDay {
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
        long moreKnownGood = moreCheck.stream().filter(this::safePart2).count();
        return knownGood + moreKnownGood;
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
