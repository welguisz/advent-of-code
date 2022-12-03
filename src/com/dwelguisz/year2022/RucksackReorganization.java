package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RucksackReorganization extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day03/input.txt");
        Integer part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines) {
        Integer sum = 0;
        for (String line : lines) {
            Integer length = line.length()/2;
            List<String> firstHalf = Arrays.stream(line.substring(0, length).split("")).collect(Collectors.toList());
            List<String> secondHalf = Arrays.stream(line.substring(length).split("")).collect(Collectors.toList());
            String appearsInBoth = firstHalf.stream().filter(l -> secondHalf.contains(l)).collect(Collectors.toList()).get(0);
            Integer val = Integer.valueOf(appearsInBoth.toCharArray()[0]);
            if (val < 96) {
                val -= 38;
            } else {
                val -= 96;
            }
            sum += val;
        }
        return sum;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer sum = 0;
        for (int i = 0; i < lines.size(); i+=3) {
            List<String> line0 = Arrays.stream(lines.get(i).split("")).collect(Collectors.toList());
            List<String> line1 = Arrays.stream(lines.get(i + 1).split("")).collect(Collectors.toList());
            List<String> line2 = Arrays.stream(lines.get(i + 2).split("")).collect(Collectors.toList());
            String inAllThree = line0.stream().filter(l -> line1.contains(l)).filter(l -> line2.contains(l)).collect(Collectors.toList()).get(0);
            Integer val = Integer.valueOf(inAllThree.toCharArray()[0]);
            if (val < 96) {
                val -= 38;
            } else {
                val -= 96;
            }
            sum += val;
        }
        return sum;
    }

}
