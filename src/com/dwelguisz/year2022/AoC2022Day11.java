package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class AoC2022Day11 extends AoCDay {

    public static class Monkey {
        Integer id;
        List<Long> items;
        Map<Boolean, Integer> nextMonkey;
        String operation;
        Integer divisbleBy;
        Integer inspections;

        public Monkey(Integer id, List<Long> items, Map<Boolean, Integer> nextMonkey, Integer divisbleBy, String operation) {
            this.id = id;
            this.items = items;
            this.nextMonkey = nextMonkey;
            this.divisbleBy = divisbleBy;
            this.operation = operation;
            inspections = 0;
        }

        public void oneRound(List<Monkey> monkeys, Boolean part2, Integer totalModulus) {
            for (Long item : items) {
                inspections++;
                Long newValue = 0L;
                if (operation.contains("*")) {
                    String operand1 = operation.substring(operation.indexOf("*")+2);
                    if (operand1.equals("old")) {
                        newValue = item * item;
                    } else {
                        newValue = item * Long.parseLong(operand1);
                    }
                } else {
                    String operand1 = operation.substring(operation.indexOf("+")+2);
                    if (operand1.equals("old")) {
                        newValue = item + item;
                    } else {
                        newValue = item + Long.parseLong(operand1);
                    }
                }
                newValue %= totalModulus;
                if (!part2) {
                    newValue /= 3;
                }
                if (newValue % divisbleBy == 0) {
                    monkeys.get(nextMonkey.get(true)).items.add(newValue);
                } else {
                    monkeys.get(nextMonkey.get(false)).items.add(newValue);
                }
            }
            items = new ArrayList<>();
        }

    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day11/input.txt");
        List<Monkey> monkeys = parseLines(lines);
        Long part1 = simulate(monkeys, 20, false);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        monkeys = parseLines(lines);
        Long part2 = simulate(monkeys, 10000, true);
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

    public List<Monkey> parseLines(List<String> lines) {
        Integer id = 0;
        List<Long> items = new ArrayList<>();
        Integer divisibleBy = 0;
        Map<Boolean, Integer> nextMonkey = new HashMap<>();
        List<Monkey> monkeys = new ArrayList<>();
        String operation = "";
        for (String line : lines) {
            if (line.contains("Monkey")) {
                id = Integer.parseInt(line.substring(0,line.length()-1).split(" ")[1]);
            } else if (line.contains("Starting items")) {
                items = Arrays.stream(line.substring(line.indexOf(":") + 2).split(", ")).map(Long::parseLong).collect(Collectors.toList());
            } else if (line.contains("Operation")) {
                operation = line.substring(line.indexOf(":") + 2);
            } else if (line. contains("Test")) {
                divisibleBy = Integer.parseInt(line.substring(line.indexOf("by") + 3));
            } else if (line.contains("true")) {
                nextMonkey.put(true,Integer.parseInt(line.substring(line.indexOf("monkey") + 7)));
            } else if (line.contains("false")) {
                nextMonkey.put(false,Integer.parseInt(line.substring(line.indexOf("monkey") + 7)));
            }
            if (line.length() == 0) {
                monkeys.add(new Monkey(id, items, nextMonkey, divisibleBy, operation));
                id = -1;
                items = new ArrayList<>();
                divisibleBy = 0;
                nextMonkey = new HashMap<>();
                operation = "";
            }
        }
        monkeys.add(new Monkey(id, items, nextMonkey, divisibleBy, operation));
        return monkeys;
    }

    public Long simulate(List<Monkey> monkeys, Integer rounds, Boolean worryLevel) {
        Integer totalModulus = monkeys.stream().mapToInt(m -> m.divisbleBy).reduce(1, (a,b) -> a*b);
        for (int i = 0; i < rounds; i++) {
            for (Monkey monkey : monkeys) {
                monkey.oneRound(monkeys, worryLevel, totalModulus);
            }
        }
        PriorityQueue<Integer> queue = new PriorityQueue<>(200, (a, b) -> b- a);
        monkeys.stream().forEach(m -> queue.add(m.inspections));
        return 1L * queue.poll() * queue.poll();
    }
}
