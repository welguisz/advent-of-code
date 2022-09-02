package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.google.common.collect.Collections2;

import java.util.*;

import static java.lang.Integer.parseInt;

public class AllInASingleNight extends AoCDay {

    Map<String, Integer> distances;
    Set<String> places;

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2015/day09/input.txt");
        createMap(lines);
        Integer part1 = solutionPart1();
        Integer part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public void createMap(List<String> lines) {
        distances = new HashMap<>();
        places = new HashSet<>();
        for (String line:lines) {
            String[] points = line.split(" ");
            Integer distance = parseInt(points[4]);
            places.add(points[0]);
            places.add(points[2]);
            distances.put(points[0]+points[2], distance);
            distances.put(points[2]+points[0], distance);
        }
    }

    public Integer solutionPart1() {
        int min = Integer.MAX_VALUE;
        for (List<String> perm : Collections2.permutations(places)) {
            int len = 0;
            for (int i = 0; i < perm.size()-1; i++) {
                len += distances.get(perm.get(i)+perm.get(i+1));
            }
            if (len < min) {
                min = len;
            }
        }
        return min;
    }

    public Integer solutionPart2() {
        int max = Integer.MIN_VALUE;
        for (List<String> perm : Collections2.permutations(places)) {
            int len = 0;
            for (int i = 0; i < perm.size()-1; i++) {
                len += distances.get(perm.get(i)+perm.get(i+1));
            }
            if (len > max) {
                max = len;
            }
        }
        return max;
    }


}
