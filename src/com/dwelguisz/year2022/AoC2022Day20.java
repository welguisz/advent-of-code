package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AoC2022Day20 extends AoCDay {
    public void solve() {
        System.out.println("Day 20 ready to go");
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day20/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<String> parsedClass = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(parsedClass);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(parsedClass);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    List<String> parseLines(List<String> lines) {
        List<String> values = new ArrayList<>();
        for (String l : lines) {
            values.add(l);
        }
        return values;
    }

    Integer solutionPart1(List<String> values) {
        return 0;
    }

    Integer solutionPart2(List<String> values) {
        return 0;
    }
}
