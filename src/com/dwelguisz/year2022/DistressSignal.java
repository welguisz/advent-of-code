package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.json.JSONArray;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class DistressSignal extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,13,false,0);
        List<String> pairs = Arrays.stream(String.join("t", lines).split("tt")).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(pairs);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(pairs);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(List<String> pairs) {
        Integer sum = 0;
        Integer index = 1;
        for (String pair : pairs) {
            String pairValues[] = pair.split("t");
            if (compareStr(pairValues[0], pairValues[1]) == -1) {
                sum += index;
            }
            index++;
        }
        return sum;
    }

    List<Object> convertJSONtoAL(JSONArray input) {
        List<Object> output = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            output.add(input.get(i));
        }
        return output;
    }

    Integer compare(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) {
            Integer ai = (Integer) a;
            Integer bi = (Integer) b;
            if (ai < bi) {
                return -1;
            } else if (ai == bi) {
                return 0;
            } else {
                return 1;
            }
        } else if (a instanceof JSONArray && b instanceof JSONArray) {
            List aa = convertJSONtoAL((JSONArray) a);
            List ba = convertJSONtoAL((JSONArray) b);
            Integer minLength = Integer.min(aa.size(), ba.size());
            for (int i = 0; i < minLength; i++) {
                Integer result = compare(aa.get(i), ba.get(i));
                if (result == 1) {
                    return 1;
                } else if (result == -1) {
                    return -1;
                }
            }
            if (aa.size() < ba.size()) {
                return -1;
            } else if (aa.size() > ba.size()) {
                return 1;
            } else {
                return 0;
            }
        } else if (a instanceof JSONArray && b instanceof Integer) {
            JSONArray ba = new JSONArray("["+b+"]");
            return compare(a, ba);
        } else {
            JSONArray aa = new JSONArray("["+a+"]");
            return compare(aa, b);
        }
    }

    Integer compareStr(String a, String b) {
        return compare(new JSONArray(a), new JSONArray(b));
    }
    public Integer solutionPart2(List<String> pairs) {
        PriorityQueue<String> results = new PriorityQueue<>(200, (a,b) -> compareStr(a,b));
        for (String p : pairs) {
            String parts[] = p.split("t");
            results.add(parts[0]);
            results.add(parts[1]);
        }
        List<String> newValues = List.of("[[2]]","[[6]]");
        results.addAll(newValues);
        List<Integer> indices = new ArrayList<>();
        Integer index = 1;
        while (!results.isEmpty()) {
            String v = results.poll();
            if (newValues.contains(v)) {
                indices.add(index);
            }
            index++;
        }
        return indices.get(0) * indices.get(1);
    }
}
