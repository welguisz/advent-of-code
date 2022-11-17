package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NoSuchThingasTooMuch extends AoCDay {
    List<Integer> containerSizes;
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day17/input.txt");
        createContainers(lines);
        Integer part1 = solutionPart1(150);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(150);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public void createContainers(List<String> lines) {
        containerSizes = new ArrayList<>();
        for (String line : lines){
            containerSizes.add(Integer.parseInt(line));
        }
    }

    public Integer solutionPart2(Integer target) {
        List<List<Integer>> possibleItems = new ArrayList<>();
        for(int i = 0; i < containerSizes.size(); i++) {
            possibleItems.addAll(combinations(containerSizes,i, target));
        }
        Integer minimumNumbersNeeded = Integer.MAX_VALUE;
        for (List<Integer> possibleItem : possibleItems) {
            if (possibleItem.size() < minimumNumbersNeeded) {
                minimumNumbersNeeded = possibleItem.size();
            }
        }
        final Integer minimum = minimumNumbersNeeded;

        return possibleItems.stream().filter(pi -> pi.size() == minimum).collect(Collectors.toList()).size();

    }

    public Integer solutionPart1(Integer target) {
        List<List<Integer>> possibleItems = new ArrayList<>();
        for(int i = 0; i < containerSizes.size(); i++) {
            possibleItems.addAll(combinations(containerSizes,i, target));
        }
        return possibleItems.size();
    }

    public List<List<Integer>> combinations(List<Integer> inputSet, int k, int target) {
        List<List<Integer>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0, target);
        return results;
    }

    public void combinationsInternal(List<Integer> inputSet, int k, List<List<Integer>> results, ArrayList<Integer> accumulator, int index, int target) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;
        if (accumulator.size() == k) {
            Integer sum = accumulator.stream().reduce(0, (a,b) -> a+b);
            if (sum.equals(target)) {
                results.add(new ArrayList<>(accumulator));
            }
        } else if (needToAccumulate <= canAccumulate) {
            combinationsInternal(inputSet, k, results, accumulator, index + 1, target);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet,k,results,accumulator,index+1, target);
            accumulator.remove(accumulator.size()-1);
        }
    }
}
