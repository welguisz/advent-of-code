package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CorruptionChecksum extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day02/input.txt");
        Long part1 = solutionPart1(lines);
        Long part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long solutionPart1(List<String> lines) {
        Long sum = 0L;
        for(String line : lines) {
            List<Integer> rowValues = Arrays.stream(line.split("\\s{1,}"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            Integer minValue = Collections.min(rowValues);
            Integer maxValue = Collections.max(rowValues);
            sum += (maxValue - minValue);
        }
        return sum;
    }

    public Long solutionPart2(List<String> lines) {
        Long sum = 0L;
        for(String line : lines) {
            List<Integer> rowValues = Arrays.stream(line.split("\\s{1,}"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            boolean found = false;
            for (int i = 0; i < rowValues.size(); i++) {
                if (found) {
                    break;
                }
                for (int j = i+1; j < rowValues.size(); j++) {
                    if (rowValues.get(i) % rowValues.get(j) == 0) {
                        sum += rowValues.get(i) / rowValues.get(j);
                        found = true;
                        break;
                    } else if (rowValues.get(j) % rowValues.get(i) == 0) {
                        sum += rowValues.get(j) / rowValues.get(i);
                        found = true;
                        break;
                    }
                }
            }
        }
        return sum;
    }


}
