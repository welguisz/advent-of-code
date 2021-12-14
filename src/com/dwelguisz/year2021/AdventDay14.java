package com.dwelguisz.year2021;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.util.stream.Collectors.counting;

public class AdventDay14 {
    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2021/day14/input.txt");
        List<String> equations = lines.stream().filter(str -> (str.contains(" -> "))).collect(Collectors.toList());
        Map<String, List<String>> newStrings = createMap(equations);
        Long part1 = elegantSolution(newStrings, lines.get(0), 10);
        Long part2 = elegantSolution(newStrings, lines.get(0), 40);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static Map<String, List<String>> createMap(List<String> equations) {
        Map<String, List<String>> map = new HashMap<>();
        for(String equation : equations) {
            String[] puts = equation.split(" -> ");
            String input = puts[0];
            String output = puts[1];
            List<String> outputs = new ArrayList<>();
            outputs.add(puts[0].substring(0,1) + output);
            outputs.add(output + puts[0].substring(1,2));
            map.put(input, outputs);
        }
        return map;
    }

    public static Long bruteForceMethod(Map<String, List<String>> equations, String line, int steps) {
        String newStr = line;
        for (int i = 0; i < steps; i++) {
            newStr = bruteForceRunOnce(equations, newStr);
        }
        Map<String, Long> counts = Arrays.stream(newStr.split("")).collect(Collectors.groupingBy(x -> x.toString(), counting()));
        Long maxValue = counts.entrySet().stream().map(entry -> entry.getValue()).max(Long::compareTo).get();
        Long minValue = counts.entrySet().stream().map(entry -> entry.getValue()).min(Long::compareTo).get();
        return maxValue - minValue;
    }

    public static String bruteForceRunOnce(Map<String, List<String>> equations, String line) {
        StringBuffer sb = new StringBuffer(line.substring(0,1));
        List<String> breakString = new ArrayList<>();
        for (int i = 0; i < line.length() - 1; i++) {
            breakString.add(line.substring(i, i+2));
        }
        for (String code : breakString) {
            sb.append(equations.get(code).get(0).substring(1,2));
            sb.append(code.substring(1,2));
        }
        return sb.toString();
    }

    public static Long elegantSolution(Map<String, List<String>> equations, String line, int steps) {
        Set<String> breakString = new HashSet<>();
        //We will be double counting letters in the middle, so let's just keep these around to add in at the very end.
        List<String> tempStrings = new ArrayList<>();
        tempStrings.add(line.substring(0,1));
        tempStrings.add(line.substring(line.length()-1));
        // line "NNCB" --> breakString: ["NN", "NC", "CB"]
        for (int i = 0; i < line.length() - 1; i++) {
            breakString.add(line.substring(i, i+2));
        }
        Map<String, Long> counts = new HashMap<>();
        for (String str : breakString) {
            counts.put(str, (long) StringUtils.countMatches(line, str));
        }
        for (int i = 0; i < steps; i++) {
            counts = elegantSolutionRunOnce(counts, equations);
        }
        final Map<String, Long> finalCounts = new HashMap<>();
        final Map<String, Long> finCounts = new HashMap(counts);
        for (String fin : counts.keySet()) {
            String[] keyChars = fin.split("");
            for (int i = 0; i < keyChars.length; i++) {
                finalCounts.computeIfPresent(keyChars[i], (k,v) -> v + finCounts.get(fin));
                finalCounts.computeIfAbsent(keyChars[i], k -> finCounts.get(fin));
            }
        }
        for (String tempStr : tempStrings) {
            finalCounts.computeIfPresent(tempStr, (k, v) -> v++);
        }

        Long maxValue = finalCounts.entrySet().stream().map(entry -> (entry.getValue()/2)).max(Long::compareTo).get();
        Long minValue = finalCounts.entrySet().stream().map(entry -> (entry.getValue()/2)).min(Long::compareTo).get();
        return maxValue - minValue;
    }


    public static Map<String, Long> elegantSolutionRunOnce(final Map<String, Long> counts, Map<String, List<String>> equations) {
        Map<String, Long> newCounts = new HashMap<>();
        for(String countStr : counts.keySet()) {
            List<String> countOnce = equations.get(countStr);
            for (String one : countOnce) {
                newCounts.computeIfPresent(one, (k, v) -> v + counts.get(countStr));
                newCounts.computeIfAbsent(one, k -> counts.get(countStr));
            }
        }
        return newCounts;
    }
}
