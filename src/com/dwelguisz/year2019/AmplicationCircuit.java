package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class AmplicationCircuit extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,7,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<String> lines) {
        Long maxValue = Long.MIN_VALUE;
        for (List<Integer> perm : Collections2.permutations(List.of(0, 1, 2, 3, 4))) {
            Long previousValue = 0L;
            for (Integer inputValue : perm) {
                IntCodeComputer computer = new IntCodeComputer();
                computer.initializeIntCode(lines);
                computer.setInputValue(inputValue.longValue());
                computer.setInputValue(previousValue);
                computer.run();
                Pair<Boolean, Long> result = computer.getOutputValue();
                previousValue = result.getRight();
            }
            maxValue = Long.max(previousValue, maxValue);
        }
        return maxValue;
    }

    public Long solutionPart2(List<String> lines) {
        List<Long> thrustOutput = new ArrayList<>();
        for (List<Integer> perm : Collections2.permutations(List.of(5, 6, 7, 8, 9))) {
            List<IntCodeComputer> computers = new ArrayList<>();
            List<ArrayDeque<Long>> deques = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                deques.add(new ArrayDeque<>());
            }
            for (Long i = 0L; i < 5; i++) {
                Long inputQueueNo = (i == 0L) ? 4L : i - 1;
                IntCodeComputer computer = new IntCodeComputer();
                computer.setId(i);
                computer.setInputQueue(deques.get(inputQueueNo.intValue()));
                computer.setOutputQueue(deques.get(i.intValue()));
                computer.initializeIntCode(lines);
                computer.setInputValue(perm.get(i.intValue()).longValue());
                computers.add(computer);
            }
            computers.get(0).setInputValue(0L);
            Thread[] threads = new Thread[computers.size()];
            for (int i = 0; i < 5; i++) {
                threads[i] = new Thread(computers.get(i));
                threads[i].start();
            }
            while (!computers.stream().allMatch(c -> c.isDone()));
            for (int i = 0; i < 5; i++) {
                threads[i].interrupt();
            }
            thrustOutput.add(computers.get(4).getDebugValue());
        }
        return thrustOutput.stream().mapToLong(i -> i).max().getAsLong();
    }


}
