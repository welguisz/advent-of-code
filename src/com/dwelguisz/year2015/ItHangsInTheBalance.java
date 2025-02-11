package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import lombok.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItHangsInTheBalance extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,24,false,0);
        Set<Long> values = lines.stream().map(Long::parseLong).collect(Collectors.toSet());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = quickSolutionPart1(new ArrayList<>(values), 3);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = quickSolutionPart1(new ArrayList<>(values), 4);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }


    // This makes several assumptions to run faster.
    // Find all sets of numbers that equal totalWeigth/comparment that have the least amount of elements
    // Find least QE from that
    public Long quickSolutionPart1(List<Long> values, Integer compartments) {
        Long totalWeight = values.stream().reduce(0L, Long::sum);
        Long eachSectionWeight = totalWeight/compartments;
        List<List<Long>> importantCombinations = new ArrayList<>();
        int sizeOfElements = compartments;
        while (importantCombinations.isEmpty()) {
            importantCombinations.addAll(combinations(values, sizeOfElements, eachSectionWeight));
            sizeOfElements++;
        }
        return findBestQE(importantCombinations, values);
    }

    Long findBestQE(List<List<Long>> combinations, List<Long> values) {
        int currentSize = values.size();
        long currentQE = values.get(0) * values.get(1);
        for (List<Long> combination : combinations) {
            if (combination.size() < currentSize) {
                currentSize = combination.size();
                currentQE = combination.stream().reduce(1L,(a,b) -> a*b);
            }
            else if (combination.size() == currentSize) {
                Long possibleQE = combination.stream().reduce(1L,(a,b)-> a*b);
                currentQE = Math.min(possibleQE, currentQE);
            }
        }
        return currentQE;
    }

    public Long exhaustiveSolutionPart1(List<Long> values, Integer compartments) {
        Long totalWeight = values.stream().reduce(0L, Long::sum);
        Long eachSectionWeight = totalWeight/compartments;
        List<List<Long>> importantCombinations = new ArrayList<>();
        Instant time1 = Instant.now();
        for (int sizeOfElements = compartments; sizeOfElements < values.size() - compartments; sizeOfElements++) {
            importantCombinations.addAll(combinations(values, sizeOfElements, eachSectionWeight));
        }
        Instant time2 = Instant.now();
        System.out.println("Time to run through all elements: " + (time2.toEpochMilli() - time1.toEpochMilli()));
        return findBestQE(importantCombinations, values);
    }

    public List<List<Long>> combinations(List<Long> inputSet, int k, Long target) {
        List<List<Long>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0, target);
        return results;
    }

    public void combinationsInternal(List<Long> inputSet, int k, List<List<Long>> results, ArrayList<Long> accumulator, int index, Long target) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;
        Long sum = accumulator.stream().reduce(0L, Long::sum);
        if (accumulator.size() == k) {
            if (sum.equals(target)) {
                results.add(new ArrayList<>(accumulator));
            }
        } else if (needToAccumulate <= canAccumulate) {
            if (sum > target) {
                return;
            }
            combinationsInternal(inputSet, k, results, accumulator, index + 1, target);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet,k,results,accumulator,index+1, target);
            accumulator.remove(accumulator.size()-1);
        }
    }
}
