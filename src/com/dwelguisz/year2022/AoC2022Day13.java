package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class AoC2022Day13 extends AoCDay {
    public void solve() {
        boolean test = false;
        String filename = test ? "testcase.txt" : "input.txt";
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day13/" + filename);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(lines);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public Integer solutionPart1(List<String> lines) {
        return 0;
    }

    public Integer solutionPart2(List<String> lines) {
        return 0;
    }
}
