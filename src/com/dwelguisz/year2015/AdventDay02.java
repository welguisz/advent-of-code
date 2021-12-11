package com.dwelguisz.year2015;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay02 {
    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2015/day02/input.txt");
        Long part1 = solutionPart1(instructions);
        Long part2 = solutionPart2(instructions);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    static Long solutionPart1(List<String> instructions) {
        Long total = 0L;
        for(String in : instructions) {
            List<Integer> ints = Arrays.stream(in.split("x")).map(str -> parseInt(str)).collect(Collectors.toList());
            Integer l = ints.get(0);
            Integer w = ints.get(1);
            Integer h = ints.get(2);
            List<Integer> areas = Arrays.asList(l*w, l*h, w*h);
            Integer extra = areas.stream().min(Integer::compareTo).get();
            Integer totalArea = ((areas.get(0) + areas.get(1) + areas.get(2)) * 2)  + extra;
            total += totalArea;
        }
        return total;
    }

    static Long solutionPart2(List<String> instructions) {
        Long total = 0L;
        for(String in : instructions) {
            List<Integer> ints = Arrays.stream(in.split("x")).map(str -> parseInt(str)).collect(Collectors.toList());
            Integer l = ints.get(0);
            Integer w = ints.get(1);
            Integer h = ints.get(2);
            List<Integer> perimeters = Arrays.asList(2 * (l + w), 2 * (l + h), 2 * (h + w));
            Integer perimeter = perimeters.stream().min(Integer::compareTo).get();
            Integer volume = l * w * h;
            Integer totalRibbon = perimeter + volume;
            total += totalRibbon;
        }
        return total;
    }
}
