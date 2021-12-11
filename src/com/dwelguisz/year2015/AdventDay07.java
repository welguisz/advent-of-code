package com.dwelguisz.year2015;

import java.util.List;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class AdventDay07 {
    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2015/day07/input.txt");
        Integer part1 = 0;
        Integer part2 = 0;
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

}
