package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class BinaryBoarding extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day05/input.txt");
        List<Integer> boardIds = BinaryCodeToInt(lines);
        Integer part1 = boardIds.stream().mapToInt(v -> v).max().orElse(-5);
        Integer part2 = findMySeatId(boardIds);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part1: %d",part2));
    }

    private Integer findMySeatId(List<Integer> boardIds) {
        Integer maxId = boardIds.stream().mapToInt(v -> v).max().orElse(-5);
        Integer minId = boardIds.stream().mapToInt(v -> v).min().orElse(-5);
        List<Integer> values = IntStream.range(minId, maxId)
                .filter(v -> !boardIds.contains(v))
                .boxed()
                .collect(Collectors.toList());
        return values.get(0);

    }

    private List<Integer> BinaryCodeToInt(List<String> lines) {
        return lines.stream().map(line -> binaryCodeToInt(line)).collect(Collectors.toList());
    }

    private Integer binaryCodeToInt(String line) {
        String tmp = line;
        tmp = tmp.replaceAll("F","0");
        tmp = tmp.replaceAll("B", "1");
        tmp = tmp.replaceAll("L","0");
        tmp = tmp.replaceAll("R","1");
        return parseInt(tmp,2);
    }

}
