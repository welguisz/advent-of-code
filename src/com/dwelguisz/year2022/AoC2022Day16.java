package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.BreadthFirstSearch;
import com.dwelguisz.base.SearchNode;
import com.dwelguisz.year2022.helper.TunnelNode;
import com.dwelguisz.year2022.helper.TunnelPathNode;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class AoC2022Day16 extends BreadthFirstSearch {


    Map<String, Valve> graph;
    Map<String, Integer> flows;
    Map<String, Integer> indicies;
    Map<Pair<String, String>, Integer> distances;

    Map<Pair<String,String>,Integer> pathCosts;
    Map<String, Valve> valveMap;


    //Thoughts ... Find the valves that have a flow rate on them. Those are the ones that you want to visit
    //Find the time from one valve to another.
    // So to go from valve AA to valve BB, it will take 4 steps, so keep that number
    //   {AA,BB} -> 4

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
        Integer part1 = solutionPart1(graph.values().stream().collect(Collectors.toList()));
        Long part1Time = Instant.now().toEpochMilli();
        //Integer part2 = solutionPart2(parsedClass);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        //System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    void parseLines(List<String> lines) {
        graph = new HashMap<>();
        valveMap = new HashMap<>();
        for (String l : lines) {
            String s[] = l.split(" ");
            String name = s[1];
            Integer flowRate = Integer.parseInt(s[4].substring(5, s[4].length()-1));
            Integer tunnelIndex = l.contains("valves") ? 7 : 6;
            String tunnels = l.substring(l.indexOf("valve") + tunnelIndex);
            Valve tmp = new Valve(name,flowRate,tunnels);
            graph.put(name, tmp);
            valveMap.put(name, tmp);
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

    public List<List<String>> combinations(List<String> inputSet, int k) {
        List<List<String>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0);
        return results;
    }
    public void combinationsInternal(List<String> inputSet, int k, List<List<String>> results, ArrayList<String> accumulator, int index) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;
        if (accumulator.size() == k) {
            results.add(new ArrayList<>(accumulator));
        } else if (needToAccumulate <= canAccumulate) {
            combinationsInternal(inputSet, k, results, accumulator, index + 1);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet,k,results,accumulator,index+1);
            accumulator.remove(accumulator.size()-1);
        }
    }

    Integer calculateTime(List<String> path) {
        Integer time = 0;
        Integer index = 0;
        while (index + 1 < path.size()) {
            time += pathCosts.get(Pair.of(path.get(index),path.get(index+1))) + 1;
            index++;
        }
        return time;
    }

    Integer calculatePressure(List<String> path, Integer totalTime) {
        Integer pressure = 0;
        Integer time = 0;
        Integer index = 0;
        while (index + 1 < path.size()) {
            time += pathCosts.get(Pair.of(path.get(index),path.get(index+1))) + 1;
            index++;
            if (time < 30) {
                pressure += valveMap.get(path.get(index)).flowRate * (totalTime - time);
            }
        }
        return pressure;

    }

    public Integer compareFlow(String a, String b) {
        Integer aFlow = valveMap.get(a).flowRate;
        Integer bFlow = valveMap.get(b).flowRate;
        return bFlow - aFlow;
    }

    Map<Integer, List<Pair<Integer, Valve>>> calculatePressureMap(List<String> valvesWithFlow, Integer maxTime) {
        Map<Integer, List<Pair<Integer, Valve>>> pressureMap = new HashMap<>();
        for (String valve : valvesWithFlow) {
            Valve currentValve = valveMap.get(valve);
            Integer flowRate = valveMap.get(valve).flowRate;
            for (int time = 0; time < maxTime; time++) {
                Integer pressure = flowRate * (maxTime - time);
                List<Pair<Integer,Valve>> temp = pressureMap.getOrDefault(pressure, new ArrayList<>());
                temp.add(Pair.of(time, currentValve));
                pressureMap.put(pressure, temp);
            }
        }
        return pressureMap;
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

    Integer solutionPart1(List<Valve> valves) {
        Map<Integer, Integer> answers = visit(graph.get("AA"), 30, 0, 0, new HashMap<>());
        return answers.values().stream().mapToInt(i -> i).max().getAsInt();
    }

    Integer solutionPart2(List<Valve> valves) {
        List<String> valvesWithFlow = valves.stream().filter(v -> v.flowRate > 0).map(v -> v.name).collect(Collectors.toList());
        Boolean addedToList = true;
        Integer personOne = 2;
        Integer maxPressure = Integer.MIN_VALUE;
        Integer totalTime = 26;
        return 0;
    }



}
