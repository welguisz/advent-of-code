package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;

import java.util.Arrays;

import java.util.List;

import java.util.stream.Collectors;


public class Lobby extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 3, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }


    public long solutionPart1(List<String> lines) {
        long answer = 0l;
        for (String line : lines) {
            answer += getJolts(line, 2);
        }
        return answer;
    }

    public long solutionPart2(List<String> lines) {
        long answer = 0l;
        for (String line : lines) {
            answer += getJolts(line, 12);
        }
        return answer;
    }

    long getJolts(String line, int size) {
        List<Integer> values = Arrays.stream(line.split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        int position = 0;
        long value = 0;
        for (int i = size-1; i >= 0; i--) {
            value *= 10;
            int maxValue = values.subList(position, values.size() - i).stream().mapToInt(Integer::intValue).max().getAsInt();
            value += maxValue;
            int lastposition = position;
            position = values.subList(position, values.size() - i).indexOf(maxValue);
            position += lastposition + 1;
        }
        return value;
    }
}
