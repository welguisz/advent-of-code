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
        private boolean running = true;
        Long part1Answer;
        Long part2Answer;

        public NetworkSwitch(ArrayDeque<Long> iQueues[], ArrayDeque<Long> oQueues[], ArrayDeque<Long>natQueue, Boolean part2){
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
            Long lastX = 0L;
            Long lastY = 0L;
            while(running) {
                for (int i = 0; i < 50; i++) {
                    if (outputQueues[i].size() > 2) {
                        Long address = outputQueues[i].pollFirst();
                        Long natX = outputQueues[i].pollFirst();
                        Long natY = outputQueues[i].pollFirst();
                        if (address > 50L) {
                            natQueue.add(natX);
                            natQueue.add(natY);
                            if (!part2) {
                                part1Answer = natY;
                                part2 = true;
                            }
                            if (allInputQueuesEmpty()) {
                                if (natY.equals(lastY)) {
                                    part2Answer = natY;
                                    running = false;
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

        public void shutdown() {
            running = false;
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,23,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        timeMarkers[3] = Instant.now().toEpochMilli();
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
        Thread[] nicThreads = new Thread[50];
        for (int i = 0; i < 50; i++) {
            nicThreads[i] = new Thread(nics[i]);
            nicThreads[i].start();
        }
        Thread runningThread = new Thread(netWorkSwitch);
        runningThread.start();
        while (!netWorkSwitch.done) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        netWorkSwitch.shutdown();
        runningThread.interrupt();
        for (int i = 0; i < 50; i++) {
            nics[i].shutdown();
            nicThreads[i].interrupt();
        }
        part1Answer = netWorkSwitch.part1Answer;
        part2Answer = netWorkSwitch.part2Answer;
        return natQueue.getLast();
    }
}
