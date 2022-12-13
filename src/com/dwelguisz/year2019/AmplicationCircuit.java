package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

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
                previousValue = computer.getOutputValue();
            }
            maxValue = Integer.max(previousValue, maxValue);
        }
        return maxValue;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer maxValue = Integer.MIN_VALUE;
        BlockingQueue<Integer> queues[] = new BlockingQueue[5];
        for (int i = 0; i < 5; i++) {
            queues[i] = new LinkedBlockingQueue<>();
        }
        for (List<Integer> perm : Collections2.permutations(List.of(5, 6, 7, 8, 9))) {
            IntCodeComputer computers[] = new IntCodeComputer[5];
            for (int i = 0; i < 5; i++) {
                computers[i] = new IntCodeComputer();
                computers[i].setId(i);
                computers[i].initializeIntCode(lines);
                computers[i].setInputValue(perm.get(i));
            }
            computers[0].setInputValue(0);
            for (int i = 0; i < 5; i++) {
                new Thread(computers[i]).start();
            }
            Boolean done = false;
            Integer fromAmplifierE = 0;
            while (!done) {
                List<Boolean> computerDone = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    Integer prevComputer = i == 0 ? 4 : i - 1;
                    Optional<Integer> outputValue = computers[prevComputer].getOutputValuePolling();
                    if (outputValue.isPresent()) {
                        if (i == 0) {
                            fromAmplifierE = outputValue.get();
                        }
                        computers[i].setInputValue(outputValue.get());
                    }
                    computerDone.add(computers[i].isDone());
                    done = computerDone.stream().allMatch(d -> d);
                }
            }
            maxValue = Integer.max(fromAmplifierE, maxValue);
        }
        return maxValue;
    }


}
