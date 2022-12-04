package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
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
            String tmp[] = line.split(",");
            String firstRange[] = tmp[0].split("-");
            String secondRange[] = tmp[1].split("-");
            Integer fLow = Integer.parseInt(firstRange[0]);
            Integer fHigh = Integer.parseInt(firstRange[1]);
            Integer sLow = Integer.parseInt(secondRange[0]);
            Integer sHigh = Integer.parseInt(secondRange[1]);
            List<Integer> first = IntStream.range(fLow,fHigh+1).boxed().collect(Collectors.toList());
            List<Integer> second = IntStream.range(sLow, sHigh+1).boxed().collect(Collectors.toList());
            pairs.add(Pair.of(first, second));
        }
        return pairs;
    }

    public Integer solutionPart1(List<Pair<List<Integer>,List<Integer>>> pairs) {
        int count = 0;
        for (Pair<List<Integer>,List<Integer>> pair : pairs) {
            List<Integer> first = pair.getLeft();
            List<Integer> second = pair.getRight();
            boolean firstContained = first.stream().filter(v -> second.contains(v)).collect(Collectors.toList()).size() == first.size();
            boolean secondContained = second.stream().filter(v -> first.contains(v)).collect(Collectors.toList()).size() == second.size();
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
            List<Integer> overlap = first.stream().filter(v -> second.contains(v)).collect(Collectors.toList());
            count += (overlap.isEmpty()) ? 0 : 1;
        }
        return count;
    }

}
