package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class KnightsOfTheDinnerTable extends AoCDay {
    Map<String, Integer> dinnerTable;
    Set<String> people;


    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day13/input.txt");
        createTable(lines);
        Integer part1 = solutionPart1();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        addMe();
        Integer part2 = solutionPart1();
        System.out.println(String.format("Part 2 Answer: %d", part2));

    }

    public void addMe() {
        String myName = "Steven";
        List<String> allPeople = people.stream().collect(Collectors.toList());
        for (String neighbor : allPeople) {
            dinnerTable.put(neighbor+myName,0);
            dinnerTable.put(myName+neighbor,0);
        }
        people.add(myName);
    }
    public void createTable(List<String> lines) {
        dinnerTable = new HashMap<>();
        people = new HashSet<>();
        for (String fullline:lines) {
            String line = fullline.substring(0,fullline.length()-1);
            String[] points = line.split(" ");
            Integer multiplier = points[2].equals("gain") ? 1 : -1;
            Integer distance = parseInt(points[3]) * multiplier;
            people.add(points[0]);
            people.add(points[10]);
            dinnerTable.put(points[0]+points[10], distance);
        }
    }

    public Integer solutionPart1() {
        int max = Integer.MIN_VALUE;
        for (List<String> perm : Collections2.permutations(people)) {
            int len = 0;
            for (int i = 0; i < perm.size(); i++) {
                int nextNum = (i + 1 == perm.size()) ? 0 : i + 1;
                len += dinnerTable.get(perm.get(i)+perm.get(nextNum));
                len += dinnerTable.get(perm.get(nextNum)+perm.get(i));
            }
            if (len > max) {
                max = len;
            }
        }
        return max;
    }

}
