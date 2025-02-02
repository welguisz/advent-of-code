package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoryManagementSystem extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,2,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
            if (Objects.equals(entry.getValue(), number)) {
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
