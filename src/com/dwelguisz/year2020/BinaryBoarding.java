package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class BinaryBoarding extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,5,false,0);
        List<Integer> boardIds = BinaryCodeToInt(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = boardIds.stream().mapToInt(v -> v).max().orElse(-5);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = findMySeatId(boardIds);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Integer findMySeatId(List<Integer> boardIds) {
        Integer maxId = boardIds.stream().mapToInt(v -> v).max().orElse(-5);
        Integer minId = boardIds.stream().mapToInt(v -> v).min().orElse(-5);
        List<Integer> values = IntStream.range(minId, maxId)
                .filter(v -> !boardIds.contains(v))
                .boxed()
                .collect(Collectors.toList());
        return values.get(0);

    }

    private List<Integer> BinaryCodeToInt(List<String> lines) {
        return lines.stream().map(line -> binaryCodeToInt(line)).collect(Collectors.toList());
    }

    private Integer binaryCodeToInt(String line) {
        String tmp = line;
        tmp = tmp.replaceAll("F","0");
        tmp = tmp.replaceAll("B", "1");
        tmp = tmp.replaceAll("L","0");
        tmp = tmp.replaceAll("R","1");
        return parseInt(tmp,2);
    }

}
