package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CampCleanup extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day04/input.txt");
        List<Pair<List<Integer>,List<Integer>>> pairs = createPairs(lines);
        Integer part1 = solutionPart1(pairs);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(pairs);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public List<Pair<List<Integer>,List<Integer>>> createPairs(List<String> lines) {
        List<Pair<List<Integer>, List<Integer>>> pairs = new ArrayList<>();
        for (String line : lines) {
            List<Integer> values  = Arrays.stream(line.replace(",", "-").split("-")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> first = IntStream.range(values.get(0),values.get(1)+1).boxed().collect(Collectors.toList());
            List<Integer> second = IntStream.range(values.get(2), values.get(3)+1).boxed().collect(Collectors.toList());
            pairs.add(Pair.of(first, second));
        }
        return pairs;
    }

    public Integer solutionPart1(List<Pair<List<Integer>,List<Integer>>> pairs) {
        int count = 0;
        for (Pair<List<Integer>,List<Integer>> pair : pairs) {
            List<Integer> first = pair.getLeft();
            List<Integer> second = pair.getRight();
            boolean firstContained = first.containsAll(second);
            boolean secondContained = second.containsAll(first);
            if (firstContained || secondContained) {
                count++;
            }
        }
        return count;
    }

    public Integer solutionPart2(List<Pair<List<Integer>,List<Integer>>> pairs) {
        Integer count = 0;
        for (Pair<List<Integer>,List<Integer>> pair : pairs) {
            List<Integer> first = pair.getLeft();
            List<Integer> second = pair.getRight();
            boolean overlap = first.stream().anyMatch(f -> second.contains(f));
            count += overlap ? 1 : 0;
        }
        return count;
    }

}
