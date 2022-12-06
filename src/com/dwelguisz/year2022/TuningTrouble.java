package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class TuningTrouble extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day06/input.txt");
        Integer part1 = solutionPart1(lines.get(0));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(lines.get(0));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(String line) {
        List<Integer> values = new ArrayList<>();
        values.addAll(List.of(0,1,2,3));
        List<List<Integer>> combos = combinations(values, 2);
        for (int i = 0; i < line.length()-4; i++) {
            if (checkAllCombos(line.substring(i,i+4), combos)) {
                return i + 4;
            }
        }
        return -1;
    }

    public Integer solutionPart2(String line) {
        List<Integer> values = new ArrayList<>();
        values.addAll(List.of(0,1,2,3,4,5,6,7,8,9));
        values.addAll(List.of(10,11,12,13));
        List<List<Integer>> combos = combinations(values, 2);
        for(int i = 0; i < line.length() -14; i++) {
            if (checkAllCombos(line.substring(i,i+14), combos)) {
                return i + 14;
            }
        }
        return -1;
    }

    public boolean checkAllCombos(String line, List<List<Integer>> combos) {
        for (List<Integer> c : combos) {
            if (line.charAt(c.get(0)) == line.charAt(c.get(1))) {
                return false;
            }
        }
        return true;
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
