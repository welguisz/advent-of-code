package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MirageMaintenance extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 9, false, 0);
        List<List<Long>> values = lines.stream().map(
                l -> Arrays.stream(l.split(" "))
                        .map(Long::parseLong)
                        .collect(Collectors.toList())
        ).collect(Collectors.toList());;
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<List<Long>> createStack(List<Long> values) {
        List<List<Long>> stack = new ArrayList<>();
        stack.add(values);
        List<Long> currentRow = new ArrayList<>(values);
        while (!currentRow.stream().allMatch(i -> i == 0l)) {
            final List<Long> t = currentRow;
            List<Long> diffRow = IntStream.range(0, currentRow.size() - 1).boxed()
                    .map(i -> t.get(i + 1) - t.get(i))
                    .collect(Collectors.toList());
            stack.add(diffRow);
            currentRow = new ArrayList<>(diffRow);
        }
        Collections.reverse(stack);
        return stack;
    }

    Long solutionPart1(List<List<Long>> values) {
        return values.stream().mapToLong(
                r -> createStack(r).stream()
                        .mapToLong(row -> row.get(row.size()-1)).sum())
                .sum();
    }

    Long solutionPart2(List<List<Long>> ints) {
        return ints.stream().mapToLong(
                r -> createStack(r).stream()
                        .mapToLong(row -> row.get(0)).reduce(0L, (a,b) -> b-a))
                .sum();
    }

}