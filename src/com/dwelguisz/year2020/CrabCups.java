package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CrabCups extends AoCDay {

    public static class CircleCupNode {
        Integer value;
        CircleCupNode next;
        CircleCupNode previous;
        final private int hashCode;

        public CircleCupNode(Integer value) {
            this.value = value;
            next = null;
            previous = null;
            this.hashCode = Objects.hash(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            CircleCupNode other = (CircleCupNode) o;
            return (this.value == other.value);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        public Integer getValue() {
            return this.value;
        }

        public CircleCupNode getNext() {
            return this.next;
        }

        public CircleCupNode getPrevious() {
            return this.previous;
        }

        public void setNext(CircleCupNode next) {
            this.next = next;
        }

        public void setPrevious(CircleCupNode previous) {
            this.previous = previous;
        }

        public CircleCupNode removeNext() {
            CircleCupNode next = this.next;
            CircleCupNode nextNext = next.getNext();
            nextNext.setPrevious(this);
            this.setNext(nextNext);
            return next;
        }

        public void insert(CircleCupNode next) {
            this.next.setPrevious(next);
            next.setNext(this.next);
            this.next = next;
        }

        @Override
        public String toString() {
            return "" + previous.getValue() + "->" + value + "->" + next.getValue();
        }
    }

    public static class CircleOfCups {
        Map<Integer, CircleCupNode> cups;
        Integer maxValue;
        Integer current;
        CircleCupNode last;
        public CircleOfCups(List<Integer> startingCupOrder, Integer maxValue) {
            this.maxValue = maxValue;
            current = startingCupOrder.get(0);
            cups = new HashMap<>();
            for (int i = 1; i <= maxValue; i++) {
                cups.put(i, new CircleCupNode(i));
                if (i == maxValue) {
                    last = cups.get(i);
                }
            }
            for (int i = 1; i <= maxValue; i++) {
                CircleCupNode c = cups.get(i);
                Integer previous = (i == 1) ? maxValue : i-1;
                Integer next = (i == maxValue) ? 0 : i+1;
                c.setNext(cups.get(next));
                c.setPrevious(cups.get(previous));
            }
            for (int i = 0; i < startingCupOrder.size()-1; i++) {
                CircleCupNode c = cups.get(startingCupOrder.get(i));
                CircleCupNode next = cups.get(startingCupOrder.get(i+1));
                c.setNext(next);
            }
            CircleCupNode c = cups.get(startingCupOrder.get(startingCupOrder.size()-1));
            Integer nextInteger = startingCupOrder.size() % maxValue;
            if (nextInteger == 0) {
                c.setNext(cups.get(startingCupOrder.get(nextInteger)));
            } else {
                c.setNext(cups.get(nextInteger+1));
                cups.get(nextInteger+1).setPrevious(c);
                last.setNext(cups.get(startingCupOrder.get(0)));
            }
            for (int i = 1; i < startingCupOrder.size(); i++) {
                CircleCupNode prev = cups.get(startingCupOrder.get(i-1));
                CircleCupNode cur = cups.get(startingCupOrder.get(i));
                cur.setPrevious(prev);
            }
            c = cups.get(startingCupOrder.get(0));
            Integer prevValue = maxValue - 1;
            if (prevValue < startingCupOrder.size()) {
                c.setPrevious(cups.get(startingCupOrder.get(prevValue)));
            } else {
                c.setPrevious(cups.get(maxValue));
            }
        }


        public void move() {
            Integer currentCup = current;
            List<CircleCupNode> removedCups = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                removedCups.add(cups.get(currentCup).removeNext());
            }
            List<Integer> values = new ArrayList<>();
            for (CircleCupNode c : removedCups) {
                values.add(c.getValue());
            }
            Integer destinationCup = currentCup - 1;
            if (destinationCup == 0) {
                destinationCup = maxValue;
            }
            while (values.contains(destinationCup)) {
                destinationCup--;
                if (destinationCup == 0) {
                    destinationCup = maxValue;
                }
            }
            CircleCupNode destination = cups.get(destinationCup);
            for (int i = 2; i >= 0; i--) {
                destination.insert(removedCups.get(i));
            }
            this.current = cups.get(current).getNext().getValue();
        }

        public Long afterCup1() {
            CircleCupNode currentCup = cups.get(1);
            Long tmp = 0L;
            currentCup = currentCup.getNext();
            while (!currentCup.getValue().equals(1)) {
                tmp *= 10;
                tmp += currentCup.getValue();
                currentCup = currentCup.getNext();
            }
            return tmp;
        }

        public Long afterCup1Part2() {
            CircleCupNode currentCup = cups.get(1);
            Integer cup1 = currentCup.getNext().getValue();
            Integer cup2 = currentCup.getNext().getNext().getValue();
            return 1L * cup1 * cup2;
        }
    }

    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        String testString = "389125467";
        String inputString = "952438716";
        Boolean test = false;
        String actualString = test ? testString : inputString;
        CircleOfCups cups = parseString(actualString, 9);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(cups);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(parseString(actualString,1000000));
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public CircleOfCups parseString(String str, Integer maxValue) {
        String tmp[] = str.split("");
        List<Integer> cups = new ArrayList<>();
        for (int i = 0; i < tmp.length; i++) {
            cups.add(Integer.parseInt(tmp[i]));
        }
        return new CircleOfCups(cups, maxValue);
    }

    public Long solutionPart1(CircleOfCups cups) {
        for (int i = 0; i < 100; i++) {
            cups.move();
        }
        return cups.afterCup1();
    }

    public Long solutionPart2(CircleOfCups cups) {
        for (int i = 0; i < 10000000; i++) {
            if (i % 100000 == 0) {
                System.out.println(i);
            }
            cups.move();
        }
        return cups.afterCup1Part2();
    }
}
