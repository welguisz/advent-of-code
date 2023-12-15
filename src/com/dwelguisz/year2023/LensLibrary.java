package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LensLibrary extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 15, false, 0);
        List<String> hashes = Arrays.stream(lines.get(0).split(",")).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(hashes);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(hashes);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Integer doHash(String hash) {
        Integer value = 0;
        for (char ch : hash.toCharArray()) {
            value = ((value + ch) * 17) % 256;
        }
        return value;
    }
    Long solutionPart1(List<String> hashes) {
        return (long) hashes.stream().mapToInt(h -> doHash(h)).sum();
    }

    Long solutionPart2(List<String> hashes) {
        Map<Integer, List<String>> lenses = new HashMap<>();
        Map<Integer, Map<String,Integer>> lensLengths = new HashMap<>();
        for (String hash : hashes) {
            String label = hash.split("=")[0].split("-")[0];
            Integer labelHash = doHash(label);
            if (hash.contains("-")) {
                if (lenses.computeIfAbsent(labelHash,l -> new ArrayList<>()).contains(label)) {
                    lenses.get(labelHash).remove(label);
                    lensLengths.get(labelHash).remove(label);
                }
            }
            if (hash.contains("=")) {
                Integer length = Integer.parseInt(hash.split("=")[1]);
                if (!lenses.computeIfAbsent(labelHash,l -> new ArrayList<>()).contains(label)) {
                    lenses.computeIfAbsent(labelHash,l->new ArrayList<>()).add(label);
                }
                lensLengths.computeIfAbsent(labelHash,l -> new HashMap<>()).put(label,length);
            }
        }
        return lenses.entrySet().stream().map(lens ->
                        IntStream.range(0, lens.getValue().size()).boxed()
                                .mapToLong(i -> (lens.getKey() + 1) * (i+1) * lensLengths.get(lens.getKey()).get(lens.getValue().get(i)))
                                .sum()
                ).reduce(0L, (a,b)->a+b);
    }
}
