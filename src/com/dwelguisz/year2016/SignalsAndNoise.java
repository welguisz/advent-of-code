package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalsAndNoise extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,6,false,0);
        List<HashMap<Character, Integer>> frequencyMap = createFrequencyMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(frequencyMap);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(frequencyMap);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
