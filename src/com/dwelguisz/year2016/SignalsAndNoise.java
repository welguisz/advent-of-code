package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalsAndNoise extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day06/input.txt");
        List<HashMap<Character, Integer>> frequencyMap = createFrequencyMap(lines);
        String part1 = solutionPart1(frequencyMap);
        String part2 = solutionPart2(frequencyMap);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

    public List<HashMap<Character,Integer>> createFrequencyMap(List<String> lines) {
        List<HashMap<Character, Integer>> frequencyMap = new ArrayList<>();
        int length = lines.get(0).length();
        for (int i = 0; i < length; i++) {
            HashMap<Character, Integer> map = new HashMap<>();
            frequencyMap.add(map);
        }
        for (String line : lines) {
            char charArray[] = line.toCharArray();
            int lcv = 0;
            for (char value : charArray) {
                HashMap<Character, Integer> map = frequencyMap.get(lcv);
                Integer mapValue = map.getOrDefault(value, 0);
                mapValue++;
                map.put(value, mapValue);
                lcv++;
            }
        }
        return frequencyMap;
    }

    public String solutionPart1(List<HashMap<Character,Integer>> frequencyMap) {
        StringBuffer sb = new StringBuffer();
        for (HashMap<Character, Integer> map : frequencyMap) {
            Character maxChar = ' ';
            Integer maxValue = -1;
            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                if (entry.getValue() > maxValue) {
                    maxChar = entry.getKey();
                    maxValue = entry.getValue();
                }
            }
            sb.append(maxChar);
        }

        return sb.toString();
    }


    public String solutionPart2(List<HashMap<Character,Integer>> frequencyMap) {
        StringBuffer sb = new StringBuffer();
        for (HashMap<Character, Integer> map : frequencyMap) {
            Character minChar = ' ';
            Integer minValue = 1000;
            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                if (entry.getValue() < minValue) {
                    minChar = entry.getKey();
                    minValue = entry.getValue();
                }
            }
            sb.append(minChar);
        }

        return sb.toString();
    }


}
