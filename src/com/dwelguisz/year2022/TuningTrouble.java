package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class TuningTrouble extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day06/input.txt");
        Integer part1 = findPacket(lines.get(0),4);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = findPacket(lines.get(0),14);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer findPacket(String line, Integer size) {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(i);
        }
        List<List<Integer>> combos = combinations(values, 2);
        Integer currentPosition = 0;
        while (true) {
            Pair<Boolean, Integer> result = checkAllCombos(line.substring(currentPosition, currentPosition+size), combos);
            if (result.getLeft()) {
                return currentPosition + size;
            }
            currentPosition += result.getRight();
        }
    }


    public Pair<Boolean,Integer> checkAllCombos(String line, List<List<Integer>> combos) {
        for (List<Integer> c : combos) {
            if (line.charAt(c.get(0)) == line.charAt(c.get(1))) {
                return Pair.of(false, Integer.min(c.get(0), c.get(1))+1);
            }
        }
        return Pair.of(true, -1);
    }
    public List<List<Integer>> combinations(List<Integer> inputSet, int k) {
        List<List<Integer>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0);
        return results;
    }
    public void combinationsInternal(List<Integer> inputSet, int k, List<List<Integer>> results, ArrayList<Integer> accumulator, int index) {
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

}
