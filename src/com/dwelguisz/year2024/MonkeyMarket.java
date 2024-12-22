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
        List<Integer> startingSecrets = lines.stream().map(Integer::parseInt).toList();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(startingSecrets);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(startingSecrets);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    int calculateOneSecret(int startingSecret) {
        int x = startingSecret;
        for (int i = 0; i < 2000; i++) {
            x = oneEvolution(x);
        }
        return x;
    }

    long solutionPart1(List<Integer> startingSecrets) {
        return startingSecrets.stream().mapToLong(this::calculateOneSecret).reduce(0L, Long::sum);
    }

    int oneEvolution(int secret) {
        int x = secret;
        x = ((x << 6) ^ x) & 0xFFFFFF;
        x = ((x >> 5) ^ x) & 0xFFFFFF;
        return ((x << 11) ^ x) & 0xFFFFFF;
    }

    long solutionPart2(List<Integer> startingSecrets) {
        Map<List<Integer>, Integer> total = new HashMap<>();
        for (Integer secret : startingSecrets) {
            List<Coord2D> pattern = new ArrayList<>();
            int last = secret % 10;
            int x = secret;
            for (int i = 0; i < 2000; i++) {
                x = oneEvolution(x);
                int temp = x % 10;
                pattern.add(new Coord2D(temp - last, temp));
                last = temp;
            }
            Set<List<Integer>> seen = new HashSet<>();
            for (int i = 0; i < pattern.size()-4; i++) {
                List<Integer> pat = pattern.subList(i, i+4).stream().map(m -> m.x).toList();
                if (!seen.contains(pat)) {
                    Integer value = pattern.get(i+3).y;
                    seen.add(pat);
                    total.merge(pat, value, Integer::sum);
                }
            }
        }
        return total.values().stream().mapToLong(l -> l).max().getAsLong();
    }
}
