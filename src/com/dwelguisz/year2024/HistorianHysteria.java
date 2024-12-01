package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistorianHysteria extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 1, false, 0);
        List<Integer> left = createPairs(lines, 0);
        List<Integer> right = createPairs(lines, 1);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(left, right);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(left, right);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Integer> createPairs(List<String> lines, int index) {
        return lines.stream().map(l -> split(l, index)).collect(Collectors.toList());
    }

    Integer split(String line, int index) {
        String[] sp = line.split("\\s+");
        return Integer.parseInt(sp[index]);
    }

    public long solutionPart1(List<Integer> left, List<Integer> right) {
        List<Integer> leftSorted = left.stream().sorted().toList();
        List<Integer> rightSorted = right.stream().sorted().toList();
        long sum = 0L;
        for (int i = 0; i < leftSorted.size(); i++) {
            sum += Math.abs(leftSorted.get(i) - rightSorted.get(i));
        }
        return sum;
    }

    public long solutionPart2(List<Integer> left, List<Integer> right) {
        Map<Integer, Long> counter = right.stream().collect(Collectors.groupingBy(key -> key, Collectors.counting()));
        return left.stream().map(l -> l * counter.getOrDefault(l, 0L)).mapToLong(l -> l).sum();
    }
}
