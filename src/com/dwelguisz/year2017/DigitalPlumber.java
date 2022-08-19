package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class DigitalPlumber extends AoCDay {
    Map<Integer, Set<Integer>> pipes;
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day12/input.txt");
        createPipes(lines);
        Integer part1 = solutionPart1(0).size();
        Integer part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));


    }

    public Set<Integer> solutionPart1(Integer startingPipe) {
        Set<Integer> visitedPipes = new HashSet<>();
        visitedPipes = findVisitedSpots(startingPipe, visitedPipes);
        return visitedPipes;
    }

    public Integer solutionPart2() {
        Integer groups = 0;
        Set<Integer> visitedPipes = new HashSet<>();
        while (visitedPipes.size() < pipes.size()) {
            Integer nextStartingGroupPipeNumber = findNextStartingGroupPipe(visitedPipes, pipes.keySet());
            Set<Integer> thisGroupPipes = solutionPart1(nextStartingGroupPipeNumber);
            visitedPipes.addAll(thisGroupPipes);
            groups++;
        }
        return groups++;
    }

    public Integer findNextStartingGroupPipe(Set<Integer> visited, Set<Integer> pipes) {
        Set<Integer> tmp = new HashSet<>(pipes);
        for (Integer visit : visited) {
            tmp.remove(visit);
        }
        return tmp.stream().collect(Collectors.toList()).get(0);
    }

    public Set<Integer> findVisitedSpots(Integer pipeNumber, Set<Integer> visitedPipes) {
        visitedPipes.add(pipeNumber);
        Set<Integer> connections = pipes.get(pipeNumber);
        for (Integer connection : connections) {
            if (visitedPipes.contains(connection)) {
                continue;
            }
            visitedPipes = findVisitedSpots(connection, visitedPipes);
        }
        return visitedPipes;
    }

    public void createPipes(List<String> inputs) {
        pipes = new HashMap<>();
        for (String input : inputs) {
            String[] pipesV = input.split(" <-> ");
            Integer inputPipe = Integer.parseInt(pipesV[0]);
            Set<Integer> outputPipe = Arrays.stream(pipesV[1].split(", ")).map(Integer::parseInt).collect(Collectors.toSet());
            pipes.put(inputPipe, outputPipe);
        }
    }
}
