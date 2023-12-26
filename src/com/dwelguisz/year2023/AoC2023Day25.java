package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AoC2023Day25 extends AoCDay {

    Map<String, Set<String>> graph;
    String start;
    String end;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 25, true, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void parseLines(List<String> lines) {
        graph = new HashMap<>();
        for (String line : lines) {
            String colonSplit[] = line.split(": ");
            List<String> endpoints = Arrays.stream(colonSplit[1].split(" ")).map(s -> s.strip()).collect(Collectors.toList());
            String start = colonSplit[0];
            this.start = (this.start == null) ? start : this.start;
            this.end = (this.end == null) ? endpoints.get(endpoints.size()-1) : this.end;
            List<String> all = new ArrayList<>();
            all.add(start);
            all.addAll(endpoints);
            for (String a : all) {
                for (String b: all) {
                    if (a.equals(b)) {
                        continue;
                    }
                    graph.computeIfAbsent(a,k -> new HashSet<>()).add(b);
                    graph.computeIfAbsent(b,k -> new HashSet<>()).add(a);
                }
            }
        }
    }

    Integer solutionPart1() {
        List<String> orderedParts = graph.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().size()))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        Collections.reverse(orderedParts);
        return 0;
    }
}
