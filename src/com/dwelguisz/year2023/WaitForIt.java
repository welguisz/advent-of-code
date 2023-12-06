package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class WaitForIt extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 6, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private List<Long> parseLinePart1(String line) {
        return Arrays.stream(line.split(":\\s+")[1].split("\\s+")).map(Long::parseLong).collect(Collectors.toList());
    }
    public Long solutionPart1(List<String> lines) {
        List<Long> time = parseLinePart1(lines.get(0));
        List<Long> distance = parseLinePart1(lines.get(1));
        List<Pair<Long,Long>> records = IntStream.range(0, time.size()).boxed()
                .map(i -> Pair.of(time.get(i),distance.get(i)))
                .collect(Collectors.toList());
        return runBoatRaces(records);
    }

    private Long parseLinePart2(String line) {
        return Long.parseLong(line.split(":\\s+")[1].replaceAll(" ",""));
    }
    public Long solutionPart2(List<String> lines) {
        Long time = parseLinePart2(lines.get(0));
        Long distance = parseLinePart2(lines.get(1));
        List<Pair<Long,Long>> records = new ArrayList<>();
        records.add(Pair.of(time,distance));
        return runBoatRaces(records);
    }

    public Long runBoatRaces(List<Pair<Long,Long>> records) {
        return records.stream()
                .map(
                        p -> LongStream.range(0L,p.getLeft())
                                .map(time -> (p.getLeft() - time) * time)
                                .filter(v -> v > p.getRight())
                                .count()
                ).reduce(1L, (a,b) -> a*b);
    }

}
