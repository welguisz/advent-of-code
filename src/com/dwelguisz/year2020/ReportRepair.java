package com.dwelguisz.year2020;

import com.dwelguisz.year2021.helper.Tuple;

import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

//Year 2021, Day 01
public class ReportRepair {
    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day01/input.txt");
        List<Integer> values = lines.stream().map(str -> parseInt(str)).collect(Collectors.toList());
        Tuple<Integer, Integer> answer = findValues(values);
        Integer part1 = answer.y * answer.x;
        Long part2 = findValuesPart2(values);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part2: %d",part2));

    }

    public static Tuple<Integer, Integer> findValues(List<Integer> values) {
        Tuple<Integer, Integer> answer = new Tuple(0,0);
        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                if (values.get(i) + values.get(j) == 2020) {
                    answer = new Tuple(values.get(i),values.get(j));
                }
            }
        }
        return answer;
    }

    public static Long findValuesPart2(List<Integer> values) {

        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                for (int k = j + 1; k < values.size(); k++) {
                    if (values.get(i) + values.get(j) + values.get(k) == 2020) {
                        return 1L * values.get(i) * values.get(j) * values.get(k);
                    }
                }
            }
        }
        return -1L;
    }


}
