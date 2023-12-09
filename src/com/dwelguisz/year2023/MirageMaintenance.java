package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MirageMaintenance extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 9, false, 0);
        List<List<Long>> ints = lines.stream().map(
                l -> Arrays.stream(l.split(" "))
                        .map(Long::parseLong)
                        .collect(Collectors.toList())
        ).collect(Collectors.toList());;
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(ints, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(ints, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Stack<List<Long>> createStack(List<Long> values) {
        Stack<List<Long>> stack = new Stack<>();
        stack.push(values);
        List<Long> currentRow = new ArrayList<>(values);
        while (!currentRow.stream().allMatch(i -> i == 0l)) {
            final List<Long> t = currentRow;
            List<Long> diffRow = IntStream.range(0, currentRow.size() - 1).boxed()
                    .map(i -> t.get(i + 1) - t.get(i))
                    .collect(Collectors.toList());
            stack.push(diffRow);
            currentRow = new ArrayList<>(diffRow);
        }
        return stack;
    }
    Long solutionPart1(List<List<Long>> ints, boolean part2) {
        Long total = 0L;
        for (List<Long> intList : ints) {
            Stack<List<Long>> stack = createStack(intList);
            Long sum = 0L;
            while (!stack.isEmpty()) {
                List<Long> cr = stack.pop();
                sum = part2 ? cr.get(0) - sum : sum + cr.get(cr.size() - 1);
            }
            total += sum;
        }
        return total;
    }
}