package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class PlutonianPebbles extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 11, false, 0);
        List<Long> stones = Arrays.stream(lines.get(0).split("\\s+")).map(Long::parseLong).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = generalizedSolution(stones, 25);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = generalizedSolution(stones, 75);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Long> stoneRules(Long stone) {
        String stoneStr = "" + stone;
        if (stone == 0L) {
            return List.of(1L);
        } else if (stoneStr.length() % 2  == 0) {
            int splitLoc = stoneStr.length() / 2;
            return List.of(Long.parseLong(stoneStr.substring(0, splitLoc)), Long.parseLong(stoneStr.substring(splitLoc)));
        } else {
            return List.of(stone * 2024);
        }
    }

    long generalizedSolution(List<Long> stones, int blinks) {
        List<Long> nextStones = new ArrayList<>(stones);
        Map<Long, Long> count = new HashMap<>();
        for (Long st : nextStones) {
            count.put(st, count.getOrDefault(st, 0L) + 1);
        }
        for (int i = 0; i < blinks; i++) {
            Map<Long, Long> nextCount = new HashMap<>();
            for (Map.Entry<Long, Long> e : count.entrySet()) {
                nextStones = stoneRules(e.getKey());
                for (Long st : nextStones) {
                    nextCount.put(st, nextCount.getOrDefault(st, 0L) + e.getValue());
                }
            }
            count = nextCount;
        }
        return count.values().stream().mapToLong(l -> l).sum();
    }

}
