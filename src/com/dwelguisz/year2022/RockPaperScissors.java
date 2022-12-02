package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.List;
import java.util.Map;

public class RockPaperScissors extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day02/input.txt");
        Integer part1 = simulate(lines, false);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = simulate(lines, true);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer simulate(List<String> lines, Boolean part2) {
        Map<String, Integer> scoreMap = Map.of(
                "A X", part2? 3 : 4,
                "A Y", part2? 4 : 8,
                "A Z", part2? 8 : 3,
                "B X", 1,
                "B Y", 5,
                "B Z", 9,
                "C X", part2? 2 : 7,
                "C Y", part2? 6 : 2 ,
                "C Z", part2? 7 : 6);
        return lines.stream().mapToInt(s -> scoreMap.get(s)).sum();
    }
}
