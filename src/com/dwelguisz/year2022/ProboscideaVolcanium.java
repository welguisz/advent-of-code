package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProboscideaVolcanium extends AoCDay{

    Map<String, Valve> graph;
    Map<String, Integer> flows;
    Map<String, Integer> indicies;
    Map<Pair<String, String>, Integer> distances;

    public static class Valve  {
        public String name;
        public Integer flowRate;
        public List<String> tunnelsDestinations;
        public Valve(String name, Integer flowRate, String tunnels) {
            this.name = name;
            this.flowRate = flowRate;
            this.tunnelsDestinations = Arrays.stream(tunnels.split(", ")).collect(Collectors.toList());
        }
    }

    public void solve() {
        System.out.println("Day 16 ready to go.");
        List<String> lines = readResoruceFile(2022,16,false,0);
        Long parseTime = Instant.now().toEpochMilli();
        parseLines(lines);
        setUpImportantThings();
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1();
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    void parseLines(List<String> lines) {
        graph = new HashMap<>();
        for (String l : lines) {
            String s[] = l.split(" ");
            String name = s[1];
            Integer flowRate = Integer.parseInt(s[4].substring(5, s[4].length()-1));
            Integer tunnelIndex = l.contains("valves") ? 7 : 6;
            String tunnels = l.substring(l.indexOf("valve") + tunnelIndex);
            Valve tmp = new Valve(name,flowRate,tunnels);
            graph.put(name, tmp);
        }
    }

    public void setUpImportantThings() {
        flows = graph.entrySet().stream()
                .filter(entry -> entry.getValue().flowRate > 0)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().flowRate));
        int bitnumber = 0;
        indicies = new HashMap<>();
        for (Map.Entry<String, Integer> val : flows.entrySet()) {
            indicies.put(val.getKey(), 1 << bitnumber);
            bitnumber++;
        }
        List<String> valveNames = graph.values().stream()
                .map(v -> v.name)
                .collect(Collectors.toList());
        distances = new HashMap<>();
        for (String valve1 : valveNames) {
            List<String> adjacentValves = graph.get(valve1).tunnelsDestinations;
            for (String valve2 : valveNames) {
                if (valve1.equals(valve2)) {
                    distances.put(Pair.of(valve1, valve2), 0);
                } else if (adjacentValves.contains(valve2)) {
                    distances.put(Pair.of(valve1, valve2), 1);
                } else {
                    distances.put(Pair.of(valve1, valve2), 1_000_000);
                }
            }
        }

        for (String k: valveNames) {
            for (String i : valveNames) {
                for (String j : valveNames) {
                    distances.put(Pair.of(i,j), Integer.min(distances.get(Pair.of(i,j)), distances.get(Pair.of(i,k))+distances.get(Pair.of(k,j))));
                }
            }
        }
    }

    Map<Integer, Integer> visit(Valve valve, Integer minutes, Integer bitMask, Integer pressure, Map<Integer, Integer> answer) {
        answer.put(bitMask, Integer.max(answer.getOrDefault(bitMask, 0), pressure));
        for (Map.Entry<String, Integer> valve2 : flows.entrySet()) {
            String valveName = valve2.getKey();
            Valve valve2Tmp = graph.get(valveName);
            Integer remainingMinutes = minutes - distances.get(Pair.of(valve.name, valve2Tmp.name)) - 1;
            if (((indicies.get(valveName) & bitMask) != 0) || (remainingMinutes <= 0)) {
                continue;
            }
            answer.putAll(
                    visit(
                            graph.get(valve2.getKey()),
                            remainingMinutes,
                            bitMask | indicies.get(valve2.getKey()),
                            pressure + flows.get(valveName)*remainingMinutes,
                            answer
                    )
            );
        }
        return answer;
    }

    Integer solutionPart1() {
        Map<Integer, Integer> answers = visit(graph.get("AA"), 30, 0, 0, new HashMap<>());
        return answers.values().stream().mapToInt(i -> i).max().getAsInt();
    }

    Integer solutionPart2() {
        Map<Integer, Integer> visited = visit(graph.get("AA"), 26, 0, 0, new HashMap<>());
        Integer maxValue = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Integer> visit1 : visited.entrySet()) {
            for (Map.Entry<Integer, Integer> visit2 : visited.entrySet()) {
                if ((visit1.getKey() & visit2.getKey()) == 0) {
                    maxValue = Integer.max(maxValue, visit1.getValue() + visit2.getValue());
                }
            }
        }
        return maxValue;
    }
}
