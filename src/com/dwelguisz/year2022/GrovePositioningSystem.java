package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AoC2022Day20 extends AoCDay {

    public static class Node {
        Long value;
        Long moduloValue;
        Node previous;
        Node next;

        public Node(Long value) {
            this(value, 0L);
        }

        public Node(Long value, Long modulo) {
            this.value = value;
            if (modulo == 0L) {
                this.moduloValue = value;
            } else {
                this.moduloValue = value % modulo;
            }
            this.next = this;
            this.previous = this;
        }

        public String toString() {
            return "Node("+value+")";
        }

        public void mix() {
            Node next = this.next;
            Node previous = this.previous;
            next.previous = previous;
            previous.next = next;
            Node selection = previous;
            if (moduloValue < 0) {
                for (Long i = moduloValue; i < 0; i++) {
                    selection = selection.previous;
                }
            } else {
                for (Long i = 0L; i < moduloValue; i++) {
                    selection = selection.next;
                }
            }
            this.next = selection.next;
            this.previous = selection;
            selection.next = this;
            this.next.previous = this;
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
        return decrypt(lines, 1L, 1, false);
    }

    Long solutionPart2(List<String> lines) {
        return decrypt(lines, 811589153L, 10, true);
    }

    public Long decrypt(List<String> lines, Long decryptKey, Integer mixingTime, Boolean part2) {
        int index = 0;
        Node zeroNode = null;
        List<Node> values = lines.stream().map(Long::parseLong)
                .map(v -> new Node(v * decryptKey, part2 ? lines.size()-1 : 0L))
                .collect(Collectors.toList());
        for (Node v : values) {
            v.next = values.get((index+1) % values.size());
            Integer prevIndex = index == 0 ? values.size() - 1 : index - 1;
            v.previous = values.get(prevIndex);
            if (v.value == 0L) {
                zeroNode = v;
            }
            index++;
        }
        for (int i = 0; i < mixingTime; i++) {
            for (Node v : values) {
                v.mix();
            }
        }
        Node pointer = zeroNode;
        List<Node> selected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 1000; j++) {
                pointer = pointer.next;
            }
            selected.add(pointer);
        }
        return selected.stream().mapToLong(n -> n.value).sum();

    }
}
