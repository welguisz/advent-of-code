package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class TreacheryOfWhales extends AoCDay {

    private Integer[] crabsPerLoc;

    public TreacheryOfWhales() {
        super();
        crabsPerLoc = new Integer[10];
    }

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day7/input.txt");
        createArray(instructions.get(0));
        Long part1 = findMinSteps(false);
        Long part2 = findMinSteps(true);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public void createArray(String crabLocs) {
        List<Integer> crabPositions =Arrays.stream(crabLocs.split(",")).map(str -> parseInt(str)).collect(Collectors.toList());
        int max = Collections.max(crabPositions);
        crabsPerLoc = new Integer[max+1];
        Arrays.fill(crabsPerLoc, 0);
        for(Integer crabPos : crabPositions) {
            crabsPerLoc[crabPos]++;
        }
    }

    public Long findMinSteps(boolean part2) {
        Long minSteps = fuelToPos(0, part2);
        for (int pos = 0; pos < crabsPerLoc.length; pos++) {
            Long fuelSpent = fuelToPos(pos, part2);
            minSteps = Math.min(minSteps, fuelSpent);
        }
        return minSteps;
    }

    public Long fuelToPos(int newPos, boolean part2) {
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
