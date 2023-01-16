package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class ComboBreaker extends AoCDay {
    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2020/day25/input.txt");
        List<Long> publicKeys = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(publicKeys);
        Long part1Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
    }

    List<Long> parseLines (List<String> lines) {
        return lines.stream().map(l -> Long.parseLong(l)).collect(Collectors.toList());
    }

    Long discreteLog(Long target, Long base, Long modulus) {
        Long value = 1L;
        Long power = 0L;
        while (!value.equals(target)) {
            value *= base;
            value %= modulus;
            power += 1L;
            if (power >= modulus) {
                return -1L;
            }
        }
        return power;
    }

    Long discretePower(Long base, Long loop, Long modulus) {
        Long value = 1L;
        for (int i = 0; i < loop; i++) {
            value *= base;
            value %= modulus;
        }
        return value;
    }

    Long solutionPart1(List<Long> publicKeys) {
        Long keyPublicKey = publicKeys.get(0);
        Long doorPublicKey = publicKeys.get(1);
        Long cardLoops = discreteLog(keyPublicKey, 7L, 20201227L);
        return discretePower(doorPublicKey, cardLoops, 20201227L);
    }
}
