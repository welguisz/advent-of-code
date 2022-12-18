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
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day16/testcase.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<Valve> parsedClass = parseLines(lines);
        setUpImportantThings(parsedClass);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(parsedClass);
        Long part1Time = Instant.now().toEpochMilli();
        //Integer part2 = solutionPart2(parsedClass);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        //System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    List<Valve> parseLines(List<String> lines) {
        List<Valve> values = new ArrayList<>();
        valveMap = new HashMap<>();
        for (String l : lines) {
            String s[] = l.split(" ");
            String name = s[1];
            Integer flowRate = Integer.parseInt(s[4].substring(5, s[4].length()-1));
            Integer tunnelIndex = l.contains("valves") ? 7 : 6;
            String tunnels = l.substring(l.indexOf("valve") + tunnelIndex);
            Valve tmp = new Valve(name,flowRate,tunnels);
            values.add(tmp);
            valveMap.put(name, tmp);
        }
        return values;
    }
    Map<Pair<String,String>,Integer> pathCosts;
    Map<String, Valve> valveMap;

    public void setUpImportantThings(List<Valve> valves) {
        pathCosts = new HashMap<>();
        List<String> valvesWithFlow = valves.stream().filter(v -> v.flowRate > 0).map(v -> v.name).collect(Collectors.toList());
        if (!valvesWithFlow.contains("AA")) {
            valvesWithFlow.add("AA");
        }
        List<List<String>> interestingPaths = combinations(valvesWithFlow, 2);
        for (List<String> path : interestingPaths) {
            TunnelNode initialNode = new TunnelNode(valveMap.get(path.get(0)),valveMap.get(path.get(1)),new ArrayList<>(),valveMap);
            Integer cost = findShortestPathAllEqual(List.of(valveMap.get(path.get(0))), List.of(initialNode)) + 1;
            pathCosts.put(Pair.of(path.get(0), path.get(1)), cost);
            pathCosts.put(Pair.of(path.get(1), path.get(0)), cost);
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

    Integer solutionPart1(List<Valve> valves) {
        List<String> valvesWithFlow = valves.stream().filter(v -> v.flowRate > 0).map(v -> v.name).sorted((a,b) -> compareFlow(a,b)).collect(Collectors.toList());
        Boolean addedToList = true;
        Integer k = 2;
        Integer maxPressure = Integer.MIN_VALUE;
        Integer totalTime = 30;
        TunnelPathNode initialNode = new TunnelPathNode(valveMap.get("AA"),pathCosts, new ArrayList<>(),valveMap,30,0,valvesWithFlow);
        Integer cost = findBestPathInTimeLimit(List.of(valveMap.get("AA")), List.of(initialNode), TunnelPathNode::compare);
        return cost;
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
