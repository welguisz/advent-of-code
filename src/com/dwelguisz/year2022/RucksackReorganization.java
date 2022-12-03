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
            List<String> firstHalf = convertStringToList(line.substring(0,length));
            List<String> secondHalf = convertStringToList(line.substring(length));
            String appearsInBoth = firstHalf.stream().filter(l -> secondHalf.contains(l)).collect(Collectors.toList()).get(0);
            sum += priorityScore(appearsInBoth);
        }
        return sum;
    }

    public Integer priorityScore(String str) {
        Integer val = Integer.valueOf(str.toCharArray()[0]);
        return (val < 96) ? val - 38 : val - 96;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer sum = 0;
        for (int i = 0; i < lines.size(); i+=3) {
            List<String> line0 = convertStringToList(lines.get(i));
            List<String> line1 = convertStringToList(lines.get(i+1));
            List<String> line2 = convertStringToList(lines.get(i+2));
            String inAllThree = line0.stream().filter(l -> line1.contains(l)).filter(l -> line2.contains(l)).collect(Collectors.toList()).get(0);
            sum += priorityScore(inAllThree);
        }
        return sum;
    }

}
