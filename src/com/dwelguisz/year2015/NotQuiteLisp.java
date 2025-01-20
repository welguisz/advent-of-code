package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NotQuiteLisp extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,1,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(instructions.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(instructions.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    int solutionPart2(String input) {
        int level = 0;
        int position = 0;
        List<Integer> inputChrs = input.chars().boxed().toList();
        while (level != -1) {
            Integer chr = inputChrs.get(position);
            level += (chr == '(') ? 1 : -1;
            position++;
        }
        return position;
    }

    long solutionPart1(String input) {
        long openParentheses = input.chars().filter(s -> s == '(').count();
        return (2*openParentheses) - input.length();
    }
}
