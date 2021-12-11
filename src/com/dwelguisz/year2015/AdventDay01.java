package com.dwelguisz.year2015;

import java.util.Arrays;
import java.util.List;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class AdventDay01 {
    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2015/day01/input.txt");
        List<String> row0 = Arrays.asList(instructions.get(0).split(""));
        Long part1 = solutionPart1(row0);
        Long part2 = solutionPart2(row0);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    static Long solutionPart2(List<String> input) {
        Long answer = 0L;
        for (int i = 0; i < input.size(); i++) {
            if ("(".equals(input.get(i))) {
                answer++;
            } else if (")".equals(input.get(i))) {
                answer--;
            }
            if (answer == -1) {
                return (1L + i);
            }
        }
        return -1L;
    }

    static Long solutionPart1(List<String> input) {
        Long answer = 0L;
        for(String in : input) {
            if ("(".equals(in)) {
                answer++;
            } else if (")".equals(in)) {
                answer--;
            }
        }
        return answer;
    }
}
