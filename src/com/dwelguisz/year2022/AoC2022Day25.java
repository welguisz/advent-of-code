package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AoC2022Day25 extends AoCDay {
    public void solve() {
        System.out.println("Day 25 ready to go.");
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day25/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<String> parsedClass = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        String part1 = solutionPart1(parsedClass);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(parsedClass);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %s",part1));
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

    String solutionPart1(List<String> values) {
        return convertToSnafu(values.stream().mapToLong(v -> convertToLong(v)).sum());
    }

    String convertToSnafu(Long value) {
        while (value > 0) {
            Long remainder = value % 5;
            switch (remainder.intValue()) {
                case 0 : return convertToSnafu(value / 5) +"0";
                case 1 : return convertToSnafu(value / 5) + "1";
                case 2 : return convertToSnafu(value / 5) + "2";
                case 3 : return convertToSnafu((value+2) / 5) + "=";
                case 4 : return convertToSnafu((value+1) / 5) + "-";
            }
        }
        return "";
    }

    Long convertToLong(String value) {
        char[] vals = value.toCharArray();
        Long val = 0L;
        for(Character v : vals) {
            val *= 5L;
            val += convertToLong(v);
        }
        return val;
    }

    Long convertToLong(char c) {
        if (c == '-') {
            return -1L;
        } else if (c == '=') {
            return -2L;
        } else {
            return Long.parseLong("" + c);
        }
    }
    Integer solutionPart2(List<String> values) {
        return 0;
    }
}
