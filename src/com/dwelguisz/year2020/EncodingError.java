package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncodingError extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,9,false,0);
        List<Long> values = lines.stream().map(Long::parseLong).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        Pair<Integer, Long> part1 = solutionPart1(values, 25);
        part1Answer = part1.getRight();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values, part1);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
