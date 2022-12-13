package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class AmplicationCircuit extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day07/input.txt");
        Integer part1 = solutionPart1(lines);
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public Integer solutionPart1(List<String> lines) {
        Integer maxValue = Integer.MIN_VALUE;
        for (List<Integer> perm : Collections2.permutations(List.of(0, 1, 2, 3, 4))) {
            Integer previousValue = 0;
            for (Integer inputValue : perm) {
                IntCodeComputer computer = new IntCodeComputer();
                computer.initializeIntCode(lines);
                computer.setInputValue(inputValue);
                computer.setInputValue(previousValue);
                computer.run();
                Pair<Boolean, Integer> result = computer.getOutputValue();
                previousValue = result.getRight();
            }
            maxValue = Integer.max(previousValue, maxValue);
        }
        return maxValue;
    }

    public Integer solutionPart2(List<String> lines) {
        List<Integer> thrustOutput = new ArrayList<>();
        for (List<Integer> perm : Collections2.permutations(List.of(5, 6, 7, 8, 9))) {
            List<IntCodeComputer> computers = new ArrayList<>();
            List<ArrayDeque<Integer>> deques = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                deques.add(new ArrayDeque<>());
            }
            for (int i = 0; i < 5; i++) {
                Integer inputQueueNo = (i == 0) ? 4 : i - 1;
                IntCodeComputer computer = new IntCodeComputer();
                computer.setId(i);
                computer.setInputQueue(deques.get(inputQueueNo));
                computer.setOutputQueue(deques.get(i));
                computer.initializeIntCode(lines);
                computer.setInputValue(perm.get(i));
                computers.add(computer);
            }
            computers.get(0).setInputValue(0);
            for (int i = 0; i < 5; i++) {
                new Thread(computers.get(i)).start();
            }
            while (!computers.stream().allMatch(c -> c.isDone()));

            thrustOutput.add(computers.get(4).getDebugValue());
        }
        return thrustOutput.stream().mapToInt(i -> i).max().getAsInt();
    }


}
