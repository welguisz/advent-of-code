package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class AoCDay02 extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 2, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<String> lines) {
        return 0L;
    }

    public Long solutionPart2(List<String> lines) {
        return 0L;
    }
}
