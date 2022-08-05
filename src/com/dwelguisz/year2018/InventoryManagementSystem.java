package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManagementSystem extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2018/day02/input.txt");
        Long part1 = solutionPart1(lines);
        String part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 1 Answer: %s",part2));
    }

    public Long solutionPart1(List<String> lines) {
        Long contains3SameLetters = 0L;
        Long contains2SameLetters = 0L;
        for(String line : lines) {
            Map<String, Integer> frequencyMap = createFrequencyMap(line);
            if (hasRequestedLetters(frequencyMap, 3)) {
                contains3SameLetters++;
            }
            if (hasRequestedLetters(frequencyMap,2)) {
                contains2SameLetters++;
            }
        }
        return contains2SameLetters * contains3SameLetters;
    }

    public String solutionPart2(List<String> lines) {
        for (int i = 0 ; i < lines.size(); i++) {
            String firstLine = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                String secondLine = lines.get(j);
                int distance = 0;
                StringBuilder commonLetters = new StringBuilder();
                for (int k = 0; k < firstLine.length(); k++) {
                    if (firstLine.charAt(k) != secondLine.charAt(k)) {
                        if (++distance > 1) {
                            break;
                        }
                    } else {
                        commonLetters.append(firstLine.charAt(k));
                    }
                }
                if (distance == 1)
                    return commonLetters.toString();
            }
        }
        return "";
    }


    public boolean hasRequestedLetters(Map<String, Integer> frequencyMap, Integer number) {
        for(Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() == number) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Integer> createFrequencyMap(String line) {
        String[] letters = line.split("");
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String letter : letters) {
            Integer count = frequencyMap.getOrDefault(letter, 0);
            count++;
            frequencyMap.put(letter,count);
        }
        return frequencyMap;
    }

}
