package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.graph.graph.Graph;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Snowverland extends AoCDay {

    Set<String> vertices;
    Set<Set<String>> edges;
    String start;
    String end;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 25, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void parseLines(List<String> lines) {
        vertices = new HashSet<>();
        edges = new HashSet<>();
        for (String line : lines) {
            String colonSplit[] = line.split(": ");
            List<String> endpoints = Arrays.stream(colonSplit[1].split(" ")).map(s -> s.strip()).collect(Collectors.toList());
            String start = colonSplit[0];
            this.start = (this.start == null) ? start : this.start;
            this.end = (this.end == null) ? endpoints.get(endpoints.size()-1) : this.end;
            vertices.add(start);
            vertices.addAll(endpoints);
            for (String b: endpoints) {
                edges.add(Set.of(start,b));
            }
        }
    }

    Integer solutionPart1() {
        Graph<String> graph = new Graph<>();
        vertices.stream().forEach(v -> graph.addVertex(v));
        edges.stream().forEach(e -> {
            List<String> tmp = e.stream().collect(Collectors.toList());
            graph.addEdges(tmp.get(0), tmp.get(1), 0);
        });
        List<Graph<String>> groups = graph.minCut(3);
        return groups.get(0).size() * groups.get(1).size();
    }
}
