package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlawedFrequencyTransmission extends AoCDay {

    public static List<Integer> BASE_PATTERN = List.of(0,1,0,-1);

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,16,false,0);
        List<Integer> values = parseLines(lines.get(0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<Integer> parseLines (String line) {
        return Arrays.stream(line.split("")).map(Integer::parseInt).collect(Collectors.toList());
    }

    public String solutionPart1(List<Integer> values) {
        for (int j = 0; j < 100; j++) {
            List<Integer> newValues = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                newValues.add(runOnePattern(values, createPattern(i)));
            }
            values = newValues;
        }
        return values.subList(0,8).stream().map(i -> i.toString()).collect(Collectors.joining());
    }

    public Integer runOnePattern(final List<Integer> values, final List<Integer> pattern) {
        Integer patternSize = pattern.size();
        return (Math.abs(IntStream.range(0,values.size()).boxed().mapToInt(j -> values.get(j) * pattern.get((j+1)%patternSize)).sum())%10);
    }

    public String solutionPart2(List<Integer> values) {
        Integer offset = 0;
        for (int i = 0; i < 7; i++) {
            offset *= 10;
            offset += values.get(i);
        }
        Integer totalElements = values.size() * 10000;
        List<Integer> trueFFT = new ArrayList<>();
        for (int i = totalElements-1; i >= offset; i--) {
            trueFFT.add(values.get(i % values.size()));
        }
        for (int j = 0; j < 100; j++) {
            List<Integer> newValues = new ArrayList<>();
            Integer runningSum = 0;
            for (int i = 0; i < trueFFT.size(); i++) {
                runningSum = (runningSum + trueFFT.get(i)) % 10;
                newValues.add(runningSum);
            }
            trueFFT = newValues;
        }
        List<String> finalValues = trueFFT.stream().map(i -> i.toString()).collect(Collectors.toList());
        Collections.reverse(finalValues);
        return finalValues.subList(0,8).stream().collect(Collectors.joining());
    }

    public List<Integer> createPattern(Integer elementNumber) {
        List<Integer> pattern = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j <= elementNumber; j++) {
                pattern.add(BASE_PATTERN.get(i));
            }
        }
        return pattern;
    }

}
