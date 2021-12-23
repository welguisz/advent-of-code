package com.dwelguisz.year2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class AdventDay07 {

    private static Integer[] crabsPerLoc;

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2021/day7/input.txt");
        createArray(instructions.get(0));
        Long part1 = findMinSteps(false);
        Long part2 = findMinSteps(true);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static void createArray(String crabLocs) {
        List<Integer> crabPositions =Arrays.stream(crabLocs.split(",")).map(str -> parseInt(str)).collect(Collectors.toList());
        int max = Collections.max(crabPositions);
        crabsPerLoc = new Integer[max+1];
        Arrays.fill(crabsPerLoc, 0);
        for(Integer crabPos : crabPositions) {
            crabsPerLoc[crabPos]++;
        }
    }

    public static Long findMinSteps(boolean part2) {
        Long minSteps = fuelToPos(0, part2);
        for (int pos = 0; pos < crabsPerLoc.length; pos++) {
            Long fuelSpent = fuelToPos(pos, part2);
            minSteps = Math.min(minSteps, fuelSpent);
        }
        return minSteps;
    }

    public static Long fuelToPos(int newPos, boolean part2) {
        Long sum = 0L;
        int loc = 0;
        for (Integer crabPos : crabsPerLoc) {
            int steps = Math.abs(newPos - loc);
            int energyStep = part2 ? (steps * (steps+1))/2 : steps;
            sum += energyStep * crabPos;
            loc++;
        }
        return sum;
    }
}
