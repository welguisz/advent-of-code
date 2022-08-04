package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RocketEquation extends AoCDay {
    public void solve(){
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2019/day01/input.txt");
        List<Integer> values = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        Long part1 = solutionPart1(values);
        Long part2 = solutionPart2(values);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long solutionPart1(List<Integer> values) {
        Long sum = 0L;
        for (Integer value : values) {
            sum += (value / 3) - 2;
        }
        return sum;
    }

    public Long solutionPart2(List<Integer> values) {
        Long sum = 0L;
        for (Integer value : values) {
            Integer addedFuel = value;
            List<Integer> addedFuelMass = new ArrayList<>();
            while (addedFuel > 0) {
                addedFuel = (addedFuel / 3) - 2;
                if (addedFuel > 0) {
                    addedFuelMass.add(addedFuel);
                }
            }
            sum += addedFuelMass.stream().reduce(0, Integer::sum);
        }
        return sum;
    }
}
