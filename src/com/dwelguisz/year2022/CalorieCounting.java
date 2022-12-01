package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.PriorityQueue;

public class CalorieCounting extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day01/input.txt");
        Integer part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines) {
        Integer elfMaxCalories = 0;
        Integer currentCalories = 0;
        for (String line : lines) {
            if (line.length() == 0) {
                if (elfMaxCalories < currentCalories) {
                    elfMaxCalories = currentCalories;
                }
                currentCalories = 0;
            } else {
                currentCalories += Integer.parseInt(line);
            }
        }
        return elfMaxCalories;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer currentCalories = 0;
        Integer currentElf = 1;
        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(200, (a,b) -> b.getRight() - a.getRight());

        for (String line : lines) {
            if (line.length() == 0) {
                queue.add(Pair.of(currentElf, currentCalories));
                currentElf++;
                currentCalories = 0;
            } else {
                currentCalories += Integer.parseInt(line);
            }
        }
        Integer sum = queue.poll().getRight();
        sum+= queue.poll().getRight();
        sum+= queue.poll().getRight();
        return sum;
    }
}
