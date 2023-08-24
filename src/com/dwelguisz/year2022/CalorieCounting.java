package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class CalorieCounting extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,1,false,0);
        PriorityQueue<Integer> queue = createQueue(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = queue.peek();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = queue.poll() + queue.poll() + queue.poll();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public PriorityQueue<Integer> createQueue(List<String> lines) {
        PriorityQueue<Integer> queue = new PriorityQueue<>(200, (a,b) -> b- a);
        Arrays.stream(
                lines.stream()
                        .collect(Collectors.joining(","))
                        .split(",,"))
                .forEach(elf -> queue.add(Arrays.stream(elf.split(","))
                        .mapToInt(s -> Integer.parseInt(s)).sum()));
        return queue;
    }
}
