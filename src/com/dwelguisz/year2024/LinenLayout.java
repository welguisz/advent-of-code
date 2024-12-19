package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinenLayout extends AoCDay {

    List<String> towelPatterns;

    Map<String, Long> possibleDesiredDesigns = new HashMap<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 19, false, 0);
        List<String> desiredDesigns = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(desiredDesigns);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(desiredDesigns);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    long createPossibleDesignPatterns(String desiredPattern) {
        if (possibleDesiredDesigns.containsKey(desiredPattern)) {
            return possibleDesiredDesigns.get(desiredPattern);
        }
        long possiblePatternCount = 0;
        if (desiredPattern.isEmpty())
            possiblePatternCount = 1;
        for (String towelPattern : towelPatterns) {
            if (desiredPattern.startsWith(towelPattern)) {
                possiblePatternCount += createPossibleDesignPatterns(desiredPattern.substring(towelPattern.length()));
            }
        }
        possibleDesiredDesigns.put(desiredPattern, possiblePatternCount);
        return possiblePatternCount;
    }

    List<String> parseLines(List<String> lines) {
        boolean beforeEmptyLine = true;
        List<String> desiredPatterns = new ArrayList<>();
        towelPatterns = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                beforeEmptyLine = false;
            } else if (beforeEmptyLine) {
                towelPatterns.addAll(Arrays.stream(line.split(",\\s+")).toList());
            } else {
                createPossibleDesignPatterns(line);
                desiredPatterns.add(line);
            }
        }
        return desiredPatterns;
    }

    long solutionPart1(List<String> desiredPatterns) {
        return desiredPatterns.stream().filter(t -> possibleDesiredDesigns.get(t) > 0).count();
    }

    long solutionPart2(List<String> desiredPatterns) {
        return desiredPatterns.stream().map(t -> possibleDesiredDesigns.get(t)).reduce(0L, Long::sum);
    }
}
