package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrovePositioningSystem extends AoCDay {

    class CircleDeque<T> extends ArrayDeque<T> {
        void mix(int num) {
            if (num == 0) return;
            if (num > 0) {
                T targetValue = this.remove();
                for (int i = 0; i < num; i++) {
                    T t = this.remove();
                    this.addLast(t);
                }
                this.addLast(targetValue);
            } else {
                T targetValue = this.remove();
                for (int i = 0; i < Math.abs(num);i++) {
                    T t = this.removeLast();
                    this.addFirst(t);
                }
                this.addFirst(targetValue);
            }
        }

        T rotateTillValueAtZero(T target) {
            T t = this.peekFirst();
            boolean zeroAtPos0 = target.equals(t);
            while (!zeroAtPos0) {
                t = this.remove();
                this.addLast(t);
                t = this.peekFirst();
                zeroAtPos0 = target.equals(t);
            }
            return t;
        }

        T rotateAndGetValue(Integer turns) {
            for (int i = 0; i < turns; i++) {
                T t = this.remove();
                this.addLast(t);
            }
            return this.peekFirst();
        }
    }


    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day20/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(lines);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Long solutionPart1(List<String> lines) {
        return decrypt(lines, 1L, 1);
    }

    Long solutionPart2(List<String> lines) {
        return decrypt(lines, 811589153L, 10);
    }

    public Long decrypt(List<String> lines, Long decryptKey, Integer mixingTime) {
        List<Long> values = lines.stream().map(Long::parseLong)
                .collect(Collectors.toList());
        CircleDeque<Pair<Integer,Long>> numbers = new CircleDeque<>();
        Integer idx = 0;
        Pair<Integer, Long> zeroItem = null;
        for (Long v : values) {
            if (v == 0L) {
                zeroItem = Pair.of(idx, v);
            }
            numbers.add(Pair.of(idx, v* decryptKey));
            idx++;
        }
        for (int i = 0; i < mixingTime; i++) {
            idx = 0;
            for (Long v : values) {
                Pair<Integer,Long> removedValue = numbers.rotateTillValueAtZero(Pair.of(idx, v*decryptKey));
                Long turns = removedValue.getRight() % (values.size() - 1);
                numbers.mix(turns.intValue());
                idx++;
            }
        }
        numbers.rotateTillValueAtZero(zeroItem);
        List<Long> positions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            positions.add(numbers.rotateAndGetValue(1000).getRight());
        }
        return positions.stream().mapToLong(l -> l).sum();
    }
}
