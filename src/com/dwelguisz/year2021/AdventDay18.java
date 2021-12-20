package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day18.SnailNumber;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class AdventDay18 {

    static List<SnailNumber> combinations = new ArrayList<>();
    static Stack<SnailNumber> sums = new Stack<>();

    public static SnailNumber parse(Integer i) {
        return new SnailNumber(i);
    }

    public static SnailNumber parse(JSONArray a, int level) {
        return new SnailNumber(parse(a.get(0).toString(), level), parse(a.get(1).toString(), level));
    }

    public static SnailNumber parse(String s, int level) {
        if (s.length() == 1) {
            return parse(Integer.parseInt(s));
        }
        return parse(new JSONArray(s), level+1);
    }

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day18/input.txt");
        Integer part1 = solutionPart1(lines);
        Integer part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static Integer solutionPart1(List<String> lines) {
        List<SnailNumber> snailNumbers = new ArrayList<>();
        for (String line : lines) {
            SnailNumber sn = parse(line, 0);
            sums.insertElementAt(sn.clone(), 0);
            combinations.add(sn.clone());
        }
        SnailNumber sum = sums.pop();
        while (!sums.isEmpty()) {
            sum = sum.add(sums.pop());
        }
        return sum.magnitude();
    }

    public static Integer solutionPart2() {
        Integer maximumN = -1;
        for (SnailNumber left : combinations) {
            for (SnailNumber right: combinations) {
                if (left.equals(right)) continue;
                maximumN = Math.max(maximumN, left.clone().add(right.clone()).magnitude());
                maximumN = Math.max(maximumN, right.clone().add(left.clone()).magnitude());
            }
        }
        return maximumN;
    }

}
