package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class SonarSweep extends AoCDay {

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day1/input.txt");
        List<Integer> values = convertStringsToInts(instructions);
        int roughCalculation = calculateIncreases(values);
        List<Integer> windowValues = calculateWindow(values);
        int betterCalculation = calculateIncreases(windowValues);
        System.out.println(String.format("Part 1 Answer: %d", roughCalculation));
        System.out.println(String.format("Part 2 Answer: %d", betterCalculation));
    }

    private Integer calculateIncreases(List<Integer> values) {
        int increased = 0;
        for(int i = 0; i < values.size(); i++) {
            if (i == 0) {
                continue;
            }
            if (values.get(i) > values.get(i-1)) {
                increased++;
            }
        }
        return increased;
    }

    private List<Integer> calculateWindow(List<Integer> depths) {
        List<Integer> sums = new ArrayList<>();
        for(int i = 0; i < depths.size(); i++) {
            if (i < 2) {
                continue;
            }
            sums.add(depths.get(i) + depths.get(i-1) + depths.get(i-2));
        }
        return sums;
    }

}
