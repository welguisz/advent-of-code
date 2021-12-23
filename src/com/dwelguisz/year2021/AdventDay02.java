package com.dwelguisz.year2021;

import java.util.List;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay02 {

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2021/day2/input.txt");
        int part1 = calculateLocation(instructions);
        int part2 = calculateLocationWithAim(instructions);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));

    }

    static private Integer calculateLocation(List<String> instructions) {
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


    static private Integer calculateLocationWithAim(List<String> instructions) {
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
