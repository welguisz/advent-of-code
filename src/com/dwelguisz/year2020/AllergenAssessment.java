package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AllergenAssessment extends AoCDay {

    public Map<String, List<String>> reducedList;

    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2020/day21/input.txt");
        Map<String,List<List<String>>> food = parseLine(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(food);
        Long part1Time = Instant.now().toEpochMilli();
        String part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %s",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Map<String,List<List<String>>> parseLine(List<String> lines) {
        Map<String, List<List<String>>> food = new HashMap<>();
        for (String l : lines) {
            String parts[] = l.split(" \\(");
            List<String> unknowns = Arrays.stream(parts[0].split(" ")).collect(Collectors.toList());
            String knowns = parts[1].substring(9,parts[1].length()-1);
            List<String> knownIngredients = Arrays.stream(knowns.split(", ")).collect(Collectors.toList());
            for (String k : knownIngredients) {
                List<List<String>> prev = food.getOrDefault(k, new ArrayList<>());
                prev.add(unknowns);
                food.put(k, prev);
            }
        }
        return food;
    }

    List<String> listIntersections(List<String> list1, List<String> list2) {
        List<String> intersection = new ArrayList<>();
        for (String l : list1) {
            if (list2.contains(l)) {
                intersection.add(l);
            }
        }
        return intersection;
    }

    Set<String> setNotIntersects(Set<String> list1, Set<String> list2) {
        Set<String> noIntersection = new HashSet<>();
        for (String l : list1) {
            if (!list2.contains(l)) {
                noIntersection.add(l);
            }
        }
        return noIntersection;
    }
    Long solutionPart1(Map<String, List<List<String>>> food) {
        Set<String> unknownFoods = new HashSet<>();
        for (Map.Entry<String, List<List<String>>> f : food.entrySet()) {
            for (List<String> i : f.getValue()) {
                for (String j : i) {
                    unknownFoods.add(j);
                }
            }
        }
        reducedList = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> f : food.entrySet()) {
            String ingredient = f.getKey();
            List<String> unknowns = f.getValue().get(0);
            for (int i = 1; i < f.getValue().size(); i++) {
                unknowns = listIntersections(unknowns, f.getValue().get(i));
            }
            reducedList.put(ingredient, unknowns);
        }
        Set<String> somewhatKnown = new HashSet<>();
        for (Map.Entry<String, List<String>> small : reducedList.entrySet()) {
            for (String i : small.getValue()) {
                somewhatKnown.add(i);
            }
        }
        Set<String> noAllergens = setNotIntersects(unknownFoods, somewhatKnown);
        Set<List<String>> lists = new HashSet<>();
        for (Map.Entry<String, List<List<String>>> f : food.entrySet()) {
            for (List<String> l : f.getValue()) {
                lists.add(l);
            }
        }
        Long count = 0L;
        for (List<String> foodItems : lists) {
            for (String i : foodItems) {
                if (noAllergens.contains(i)) {
                    count++;
                }
            }
        }
        return count;
    }

    String solutionPart2() {
        Map<String, String> knownIngredients = new HashMap<>();
        for (Map.Entry<String, List<String>> i : reducedList.entrySet()) {
            if (i.getValue().size() == 1) {
                knownIngredients.put(i.getKey(), i.getValue().get(0));
            }
        }
        while (!reducedList.isEmpty()) {
            List<String> known = knownIngredients.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
            Map<String, String> completelyKnown = new HashMap<>(knownIngredients);
            for (Map.Entry<String, String> i : knownIngredients.entrySet()) {
                Map<String, List<String>> newList = new HashMap<>();
                for (Map.Entry<String, List<String>> j : reducedList.entrySet()) {
                    List<String> tmp = new ArrayList<>();
                    for (String t : j.getValue()) {
                        if (!known.contains(t)) {
                            tmp.add(t);
                        }
                    }
                    if (tmp.size() == 1) {
                        completelyKnown.put(j.getKey(), tmp.get(0));
                        known.add(tmp.get(0));
                    } else if (tmp.size() > 1) {
                        newList.put(j.getKey(), tmp);
                    }
                }
                reducedList = new HashMap<>(newList);
            }
            knownIngredients = completelyKnown;
        }
        List<String> orderedIngredients = knownIngredients.entrySet().stream().map(e -> e.getKey()).sorted().collect(Collectors.toList());
        final Map<String, String> knownIng = new HashMap<>(knownIngredients);
        return orderedIngredients.stream().map(i -> knownIng.get(i)).collect(Collectors.joining(","));
    }
}
