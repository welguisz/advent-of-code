package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItHangsInTheBalance extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day24/input.txt");
        List<Long> values = lines.stream().map(Long::parseLong).collect(Collectors.toList());
        Long part1 = solutionPart1(values, 3);
        Long part2 = solutionPart1(values, 4);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long solutionPart1(List<Long> values, Integer compartments) {
        Long totalWeight = values.stream().reduce(0L, (a,b)->a+b);
        Long eachSectionWeight = totalWeight/compartments;
        List<List<Long>> importantCombinations = new ArrayList<>();
        for (int sizeOfElements = compartments; sizeOfElements < values.size() - compartments; sizeOfElements++) {
            importantCombinations.addAll(combinations(values, sizeOfElements, eachSectionWeight));
        }
        Integer currentSize = values.size();
        Long currentQE = 1L * values.get(0) * values.get(1);
        for (List<Long> combination : importantCombinations) {
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

    public List<List<Long>> combinations(List<Long> inputSet, int k, Long target) {
        List<List<Long>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0, target);
        return results;
    }

    public void combinationsInternal(List<Long> inputSet, int k, List<List<Long>> results, ArrayList<Long> accumulator, int index, Long target) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;
        if (accumulator.size() == k) {
            Long sum = accumulator.stream().reduce(0L, (a,b) -> a+b);
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
