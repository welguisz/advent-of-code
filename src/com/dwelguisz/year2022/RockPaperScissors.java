package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RockPaperScissors extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day02/input.txt");
        HashMap<Pair<Character, Character>, Integer> scoreMap = (HashMap<Pair<Character, Character>, Integer>) createScoreMap();
        Integer part1 = simulate(lines, scoreMap);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        scoreMap = (HashMap<Pair<Character, Character>, Integer>) updateScoreMap(scoreMap);
        Integer part2 = simulate(lines, scoreMap);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer simulate(List<String> lines, HashMap<Pair<Character, Character>, Integer> scoreMap) {
        Integer score = 0;
        for (String line:lines) {
            Character p1 = line.charAt(0);
            Character p2 = line.charAt(2);
            score += scoreMap.get(Pair.of(p1,p2));
        }
        return score;
    }

    public Map<Pair<Character, Character>, Integer> createScoreMap() {
        Map<Pair<Character,Character>, Integer> scoreMap = new HashMap<>();
        scoreMap.put(Pair.of('A','X'),4);
        scoreMap.put(Pair.of('A','Y'),8);
        scoreMap.put(Pair.of('A','Z'),3);
        scoreMap.put(Pair.of('B','X'),1);
        scoreMap.put(Pair.of('B','Y'),5);
        scoreMap.put(Pair.of('B','Z'),9);
        scoreMap.put(Pair.of('C','X'),7);
        scoreMap.put(Pair.of('C','Y'),2);
        scoreMap.put(Pair.of('C','Z'),6);
        return scoreMap;
    }

    public Map<Pair<Character, Character>, Integer> updateScoreMap(Map<Pair<Character, Character>,Integer> scoreMap) {
        HashMap<Pair<Character,Character>,Integer> newScoreMap = new HashMap<>(scoreMap);
        newScoreMap.put(Pair.of('A','X'),3);
        newScoreMap.put(Pair.of('A','Y'),4);
        newScoreMap.put(Pair.of('A','Z'),8);
        newScoreMap.put(Pair.of('C','X'),2);
        newScoreMap.put(Pair.of('C','Y'),6);
        newScoreMap.put(Pair.of('C','Z'),7);
        return newScoreMap;
    }
}
