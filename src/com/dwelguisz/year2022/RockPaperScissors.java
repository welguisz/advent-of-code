package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RockPaperScissors extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day02/input.txt");
        Integer part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines) {
        Integer score = 0;
        Map<Pair<Character,Character>, Integer> part1ScoreMap = new HashMap<>();
        part1ScoreMap.put(Pair.of('A','X'),4);
        part1ScoreMap.put(Pair.of('A','Y'),8);
        part1ScoreMap.put(Pair.of('A','Z'),3);
        part1ScoreMap.put(Pair.of('B','X'),1);
        part1ScoreMap.put(Pair.of('B','Y'),5);
        part1ScoreMap.put(Pair.of('B','Z'),9);
        part1ScoreMap.put(Pair.of('C','X'),7);
        part1ScoreMap.put(Pair.of('C','Y'),2);
        part1ScoreMap.put(Pair.of('C','Z'),6);
        for (String line:lines) {
            Character p1 = line.charAt(0);
            Character p2 = line.charAt(2);
            score += part1ScoreMap.get(Pair.of(p1,p2));
        }
        return score;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer score = 0;
        Map<Pair<Character,Character>, Integer> part2ScoreMap = new HashMap<>();
        part2ScoreMap.put(Pair.of('A','X'),3);
        part2ScoreMap.put(Pair.of('A','Y'),4);
        part2ScoreMap.put(Pair.of('A','Z'),8);
        part2ScoreMap.put(Pair.of('B','X'),1);
        part2ScoreMap.put(Pair.of('B','Y'),5);
        part2ScoreMap.put(Pair.of('B','Z'),9);
        part2ScoreMap.put(Pair.of('C','X'),2);
        part2ScoreMap.put(Pair.of('C','Y'),6);
        part2ScoreMap.put(Pair.of('C','Z'),7);
        for (String line:lines) {
            Character p1 = line.charAt(0);
            Character p2 = line.charAt(2);
            score += part2ScoreMap.get(Pair.of(p1,p2));
        }
        return score;
    }
}
