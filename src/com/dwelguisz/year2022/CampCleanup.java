package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CampCleanup extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022, 4, false, 0);
        List<Pair<List<Integer>,List<Integer>>> pairs = createPairs(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(pairs);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(pairs);
        timeMarkers[3] = Instant.now().toEpochMilli();
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

    public Long solutionPart1(List<Pair<List<Integer>,List<Integer>>> pairs) {
        return pairs.stream()
                .filter(p -> p.getRight().containsAll(p.getLeft()) || p.getLeft().containsAll(p.getRight()))
                .count();
    }

    public Long solutionPart2(List<Pair<List<Integer>,List<Integer>>> pairs) {
        return pairs.stream()
                .filter(p -> p.getRight().stream()
                        .anyMatch(f -> p.getLeft().contains(f)))
                .count();
    }

}
