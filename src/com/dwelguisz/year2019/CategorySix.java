package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.List;

public class CategorySix extends AoCDay {

    public static class NetworkSwitch extends Thread implements Runnable {
        ArrayDeque<Long> inputQueues[];
        ArrayDeque<Long> outputQueues[];
        ArrayDeque<Long> natQueue;
        Boolean part2;
        private boolean done;
        public NetworkSwitch(ArrayDeque<Long> iQueues[], ArrayDeque<Long> oQueues[], ArrayDeque natQueue, Boolean part2){
            this.inputQueues = iQueues;
            this.outputQueues = oQueues;
            this.natQueue = natQueue;
            this.part2 = part2;
            this.done = false;
        }

        public boolean isDone() {
            return done;
        }

        public void run() {
            boolean continueToRun = true;
            Long lastX = 0L;
            Long lastY = 0L;
            while(continueToRun) {
                for (int i = 0; i < 50; i++) {
                    if (outputQueues[i].size() > 2) {
                        Long address = outputQueues[i].pollFirst();
                        Long natX = outputQueues[i].pollFirst();
                        Long natY = outputQueues[i].pollFirst();
                        if (address > 50L) {
                            natQueue.add(natX);
                            natQueue.add(natY);
                            if (!part2) {
                                System.out.println(String.format("Part 1 answer: %d", natY));
                                part2 = true;
                            }
                            if (allInputQueuesEmpty()) {
                                if (natY.equals(lastY)) {
                                    System.out.println(String.format("Part 2 answer: %d", natY));
                                    System.out.println("End program by pressing Ctrl-C now");
                                    continueToRun = false;
                                }
                                inputQueues[0].add(natX);
                                inputQueues[0].add(natY);
                            }
                            lastY = natY;
                        } else {
                            inputQueues[address.intValue()].add(natX);
                            inputQueues[address.intValue()].add(natY);
                        }
                    }
                }
            }
            done = true;
        }

        public boolean allInputQueuesEmpty() {
            for (int i = 0; i < 50; i++) {
                if (!inputQueues[i].isEmpty()) {
                    return false;
                }
            }
            return true;
        }

    }

    public static class NetworkInterfaceCard extends IntCodeComputer {

        public NetworkInterfaceCard() {
            super();
        }

        @Override
        public Pair<Boolean, Long> getInputValue() {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
            if (inputValues.isEmpty()) {
                return Pair.of(true, -1L);
            }
            return Pair.of(true, inputValues.pollFirst());
        }

    }


    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day23/input.txt");
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(lines);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Long solutionPart1(List<String> lines) {
        NetworkInterfaceCard[] nics = new NetworkInterfaceCard[50];
        ArrayDeque<Long> inputQueues[] = new ArrayDeque[50];
        ArrayDeque<Long> outputQueues[] = new ArrayDeque[50];
        ArrayDeque<Long> natQueue = new ArrayDeque<>();
        for (Integer i = 0; i < 50; i++) {
            nics[i] = new NetworkInterfaceCard();
            inputQueues[i] = new ArrayDeque<>();
            outputQueues[i] = new ArrayDeque<>();
            nics[i].setId(i.longValue());
            nics[i].setInputQueue(inputQueues[i]);
            nics[i].setOutputQueue(outputQueues[i]);
            inputQueues[i].add(i.longValue());
            nics[i].initializeIntCode(lines);
        }
        NetworkSwitch netWorkSwitch = new NetworkSwitch(inputQueues, outputQueues, natQueue, false);
        Thread.UncaughtExceptionHandler h = (thread, exception) -> {
            System.out.println(exception.getMessage());
            for (int i = 0; i < 50; i++) {
                nics[i].interrupt();
            }
            netWorkSwitch.interrupt();
        };
        for (int i = 0; i < 50; i++) {
            nics[i].setUncaughtExceptionHandler(h);
        }
        netWorkSwitch.setUncaughtExceptionHandler(h);
        for (int i = 0; i < 50; i++) {
            new Thread(nics[i]).start();
        }
        new Thread(netWorkSwitch).start();
        while (!netWorkSwitch.isDone()) {};
        return natQueue.getLast();
    }

    Long solutionPart2(List<String> lines) {
        return 0L;
    }
}
