package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class CalorieCounting extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day01/input.txt");
        PriorityQueue<Integer> queue = createQueue(lines);
        Integer part1 = queue.peek();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = queue.poll() + queue.poll() + queue.poll();
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public PriorityQueue<Integer> createQueue(List<String> lines) {
        PriorityQueue<Integer> queue = new PriorityQueue<>(200, (a,b) -> b- a);
        String oneString = lines.stream().collect(Collectors.joining(","));
        String elves[] = oneString.split(",,");
        for (String elf : elves) {
            String calories[] = elf.split(",");
            Integer sum = 0;
            for (String calorie : calories) {
                sum += Integer.parseInt(calorie);
            }
            queue.add(sum);
        }
        return queue;
    }
}
