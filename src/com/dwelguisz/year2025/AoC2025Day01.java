package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AoC2025Day01 extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 1, true, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Integer> parseLines(List<String> lines, int index) {
        return lines.stream().map(l -> split(l, index)).collect(Collectors.toList());
    }

    Integer split(String line, int index) {
        String[] sp = line.split("\\s+");
        return Integer.parseInt(sp[index]);
    }

    public long solutionPart1() {
        return 0L;
    }

    public long solutionPart2() {
        return 0L;
    }

}
