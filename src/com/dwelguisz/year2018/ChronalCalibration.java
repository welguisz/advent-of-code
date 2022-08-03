package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class ChronalCalibration extends AoCDay {
    public void solve(){
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2018/day01/input.txt");
        Long part1 = solutionPart1(lines);
        Long part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long solutionPart1(List<String> lines) {
        Long current = 0L;
        for (String line : lines) {
            if (line.substring(0,1).equals("-")) {
                current -= Integer.parseInt(line.substring(1));
            } else {
                current += Integer.parseInt(line.substring(1));
            }
        }
        return current;
    }


    public Long solutionPart2(List<String> lines) {
        Long current = 0L;
        List<Long> previousFrequencies = new ArrayList<>();
        previousFrequencies.add(current);
        while(true) {
            for (String line : lines) {
                if (line.substring(0, 1).equals("-")) {
                    current -= Integer.parseInt(line.substring(1));
                } else {
                    current += Integer.parseInt(line.substring(1));
                }
                if (previousFrequencies.contains(current)) {
                    return current;
                }
                previousFrequencies.add(current);
            }
        }
    }

}
