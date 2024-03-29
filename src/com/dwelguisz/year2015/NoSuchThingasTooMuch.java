package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NoSuchThingasTooMuch extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,17,false,0);
        List<List<Integer>> possibleCombinations = createPossibleCombinations(lines, 150);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(possibleCombinations);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(possibleCombinations);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<List<Integer>> createPossibleCombinations(List<String> lines, Integer target) {
        List<Integer> containerSizes = new ArrayList<>();
        for (String line : lines){
            containerSizes.add(Integer.parseInt(line));
        }
        List<List<Integer>> possibleItems = new ArrayList<>();
        for(int i = 0; i < containerSizes.size(); i++) {
            possibleItems.addAll(combinations(containerSizes,i, target));
        }
        return possibleItems;
    }

    public Integer solutionPart1(List<List<Integer>> possibleCombinations) {
        return possibleCombinations.size();
    }

    public Long solutionPart2(List<List<Integer>> possibleCombinations) {
        Integer minimum = possibleCombinations.stream().mapToInt(nums -> nums.size()).min().getAsInt();
        return possibleCombinations.stream().filter(pi -> pi.size() == minimum).count();
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
            Integer sum = accumulator.stream().mapToInt(i -> i).sum();
            if (sum.equals(target)) {
                results.add(new ArrayList<>(accumulator));
                return;
            }
            if (sum > target) {
                return;
            }
        } else if (needToAccumulate <= canAccumulate) {
            combinationsInternal(inputSet, k, results, accumulator, index + 1, target);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet,k,results,accumulator,index+1, target);
            accumulator.remove(accumulator.size()-1);
        }
    }
}
