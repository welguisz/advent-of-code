package com.dwelguisz.year2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay01 {

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2021/day1/input.txt");
        List<Integer> values = instructions.stream().map(str -> parseInt(str)).collect(Collectors.toList());
        int roughCalculation = calculateIncreases(values);
        List<Integer> windowValues = calculateWindow(values);
        int betterCalculation = calculateIncreases(windowValues);
        System.out.println(String.format("Part 1 Answer: %d", roughCalculation));
        System.out.println(String.format("Part 2 Answer: %d", betterCalculation));
    }

    static private Integer calculateIncreases(List<Integer> values) {
        int increased = 0;
        boolean skipFirst = true;
        for(int i = 0; i < values.size(); i++) {
            if (skipFirst) {
                skipFirst = false;
                continue;
            }
            if (values.get(i) > values.get(i-1)) {
                increased++;
            }
        }
        return increased;
    }

    static private List<Integer> calculateWindow(List<Integer> depths) {
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
