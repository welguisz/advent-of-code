package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdapterArray extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day10/input.txt");
        List<Long> sortedAdapters = lines.stream().map(str -> Long.parseLong(str)).sorted(Long::compareTo).collect(Collectors.toList());
        Integer part1 = solutionPart1(sortedAdapters);
        Long part2 = solutionPart2(sortedAdapters);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    private Integer solutionPart1(List<Long> values) {
        Map<Long, List<Long>> jumps = new HashMap<>();
        Long currentJolt = 0L;
        jumps.put(1L, new ArrayList<>());
        jumps.put(2L, new ArrayList<>());
        jumps.put(3L, new ArrayList<>());
        for (Long value: values) {
            Long diff = (value - currentJolt);
            jumps.computeIfPresent(diff, (a,b) -> addValue(b,a));
            currentJolt = value;
        }
        jumps.computeIfPresent(3L, (a,b) -> addValue(b,a));
        return jumps.get(1L).size() * jumps.get(3L).size();
    }

    private List<Long> addValue(List<Long> list, Long value) {
        List<Long> newList = new ArrayList<>(list);
        newList.add(value);
        return newList;
    }

    private Long solutionPart2(List<Long> values) {
        Map<Long, Long> counts = new HashMap<>();
        counts.put(0L,1L);
        for(int i = 0; i < values.size(); i++) {
            counts.put(values.get(i),
                    counts.getOrDefault(values.get(i)-1, 0L) +
                            counts.getOrDefault(values.get(i)-2, 0L) +
                            counts.getOrDefault(values.get(i)-3, 0L)
                    );
        }
        return counts.get(values.get(values.size()-1));
    }
}
