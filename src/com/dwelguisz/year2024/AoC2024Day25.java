package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AoC2024Day25 extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 24, false, 0);
        List<String> values = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<String> parseLines(List<String> lines) {
        List<String> values = new ArrayList<>();
        for (String line : lines) {
            values.add(line);
        }
        return values;
    }

    long solutionPart1(List<String> values) {
        return 0L;
    }

    String solutionPart2() {
        return "Press link to complete.";
    }
}
