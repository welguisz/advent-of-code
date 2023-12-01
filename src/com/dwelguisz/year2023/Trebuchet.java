package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Trebuchet extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 1, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Long solutionPart1(List<String> lines) {
       return lines.stream().map(line ->
            Arrays.stream(line.split(""))
            .filter(str -> (str.toCharArray()[0] > 48) && (str.toCharArray()[0] < 58))
            .map(Integer::parseInt)
            .collect(Collectors.toList()))
            .mapToLong(arr -> arr.get(0) * 10 + arr.get(arr.size()-1))
            .sum();
    }

    Long solutionPart2(List<String> lines) {
        return lines.stream().map(line ->
                        Arrays.stream(line.toLowerCase()
                                        .replaceAll("one","one1one")
                                        .replaceAll("two","two2two")
                                        .replaceAll("three","three3three")
                                        .replaceAll("four","four4four")
                                        .replaceAll("five","five5five")
                                        .replaceAll("six","six6six")
                                        .replaceAll("seven","seven7seven")
                                        .replaceAll("eight","eight8eight")
                                        .replaceAll("nine","nine9nine")
                                        .replaceAll("zero","zero0zero")
                                .split(""))
                                .filter(str -> (str.toCharArray()[0] > 47) && (str.toCharArray()[0] < 58))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList()))
                .mapToLong(arr -> arr.get(0) * 10 + arr.get(arr.size()-1))
                .sum();
    }
}
