package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
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
        part1Answer = processOASISdata(values, (list) -> list.size()-1, (a, b) -> a+b);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = processOASISdata(values, (list) -> 0, (a, b) -> b-a);
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

    Long processOASISdata(List<List<Long>> values,
                          Function<List, ? extends Integer> func1,
                          BiFunction<Long, Long, Long> func2) {
        return values.stream().mapToLong(
                r -> createStack(r).stream()
                        .mapToLong(
                                row -> row.get(func1.apply(row)))
                        .reduce(0L, (a,b) -> func2.apply(a,b)))
                .sum();
    }
}