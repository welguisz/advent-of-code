package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncodingError extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day09/input.txt");
        List<Long> values = lines.stream().map(str -> Long.parseLong(str)).collect(Collectors.toList());
        Pair<Integer, Long> part1 = solutionPart1(values, 25);
        Long part2 = solutionPart2(values, part1);
        System.out.println(String.format("Solution Part1: %d",part1.getRight()));
        System.out.println(String.format("Solution Part2: %d",part2));
    }

    private Pair<Integer, Long> solutionPart1(List<Long> values, Integer preambleSize) {
        Integer currentPos = preambleSize;
        Boolean validNumber = true;
        while (validNumber) {
            List<Long> preambleList = new ArrayList<>();
            for(int i = currentPos - preambleSize; i < currentPos; i++) {
                preambleList.add(values.get(i));
            }
            validNumber = validNumber(preambleList, values.get(currentPos));
            if (validNumber) {
                currentPos++;
            }
        }
        return Pair.of(currentPos, values.get(currentPos));
    }

    private Long solutionPart2(List<Long> values, Pair<Integer, Long> target) {
        Integer minimumPos = 0;
        Integer maximumPos = 0;
        Long currentDiff = target.getRight();
        while ((maximumPos < target.getLeft()) && (currentDiff != 0L)) {
            if (currentDiff > 0) {
                currentDiff -= values.get(maximumPos);
                maximumPos++;
            } else if (currentDiff < 0) {
                currentDiff += values.get(minimumPos);
                minimumPos++;
            }
        }
        List<Long> subSet = new ArrayList<>();
        for (int i = minimumPos; i < maximumPos; i++) {
            subSet.add(values.get(i));
        }
        Long minValue = subSet.stream().min(Long::compareTo).get();
        Long maxValue = subSet.stream().max(Long::compareTo).get();
        return minValue + maxValue;
    }

    private Boolean validNumber(List<Long> preamble, Long value) {
        for (int i = 0; i < preamble.size(); i++) {
            for (int j = i+1; j < preamble.size(); j++) {
                if (preamble.get(i) + preamble.get(j) == value) {
                    return true;
                }
            }
        }
        return false;
    }
}
