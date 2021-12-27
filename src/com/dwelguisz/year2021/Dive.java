package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.util.List;

import static java.lang.Integer.parseInt;

public class Dive extends AoCDay {

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day2/input.txt");
        int part1 = calculateLocation(instructions);
        int part2 = calculateLocationWithAim(instructions);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));

    }

    private Integer calculateLocation(List<String> instructions) {
        int horizontal = 0;
        int depth = 0;
        for(int i = 0; i < instructions.size(); i++) {
            String[] parseInstruction = instructions.get(i).split(" ");
            int step = parseInt(parseInstruction[1]);
            if ("forward".equals(parseInstruction[0])) {
                horizontal += step;
            } else if ("down".equals(parseInstruction[0])) {
                depth += step;
            } else if ("up".equals(parseInstruction[0])) {
                depth -= step;
            }
        }
        return horizontal*depth;
    }


    private Integer calculateLocationWithAim(List<String> instructions) {
        int horizontal = 0;
        int depth = 0;
        int aim = 0;
        for(int i = 0; i < instructions.size(); i++) {
            String[] parseInstruction = instructions.get(i).split(" ");
            int step = parseInt(parseInstruction[1]);
            if ("forward".equals(parseInstruction[0])) {
                horizontal += step;
                depth = depth + (step*aim);
            } else if ("down".equals(parseInstruction[0])) {
                aim += step;
            } else if ("up".equals(parseInstruction[0])) {
                aim -= step;
            }
        }
        return horizontal*depth;
    }

}
