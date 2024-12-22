package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonkeyMarket extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 22, false, 0);
        List<Long> startingSecrets = lines.stream().map(Long::parseLong).toList();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(startingSecrets);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(startingSecrets);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    long calculateOneSecret(long startingSecret) {
        long x = startingSecret;
        for (int i = 0; i < 2000; i++) {
            x = oneEvolution(x);
        }
        return x;
    }

    long solutionPart1(List<Long> startingSecrets) {
        return startingSecrets.stream().map(this::calculateOneSecret).reduce(0L, Long::sum);
    }

    long oneEvolution(long secret) {
        long x = secret;
        x = ((x * 64) ^ x) % 16777216;
        x = ((x / 32) ^ x) % 16777216;
        return ((x * 2048) ^ x) % 16777216;
    }

    long solutionPart2(List<Long> startingSecrets) {
        Map<List<Integer>, Integer> total = new HashMap<>();
        for (Long secret : startingSecrets) {
            List<Coord2D> pattern = new ArrayList<>();
            Long last = secret % 10;
            long x = secret;
            for (int i = 0; i < 2000; i++) {
                x = oneEvolution(x);
                long temp = x % 10;
                pattern.add(new Coord2D((int) (temp - last), (int) temp));
                last = temp;
            }
            Set<List<Integer>> seen = new HashSet<>();
            for (int i = 0; i < pattern.size()-4; i++) {
                List<Integer> pat = pattern.subList(i, i+4).stream().map(m -> m.x).toList();
                Integer value = pattern.get(i+3).y;
                if (!seen.contains(pat)) {
                    seen.add(pat);
                    total.computeIfAbsent(pat, k -> 0);
                    total.compute(pat, (k, v) -> v + value);
                }
            }
        }
        return total.values().stream().mapToLong(l -> l).max().getAsLong();
    }
}
