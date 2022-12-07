package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarbleMania extends AoCDay {
    public void solve() {
        Long part1 = solutionPart1(71170, 411);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Long part2 = solutionPart1(7117000, 411);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    class CircleDeque<T> extends ArrayDeque<T> {
        void rotate(int num) {
            if (num == 0) return;
            if (num > 0) {
                for (int i = 0; i < num; i++) {
                    T t = this.removeLast();
                    this.addFirst(t);
                }
            } else {
                for (int i = 0; i < Math.abs(num)-1;i++) {
                    T t = this.remove();
                    this.addLast(t);
                }
            }
        }
    }

    public Long solutionPart1(Integer lastMarble, Integer numberOfElves) {
        CircleDeque<Integer> circle = new CircleDeque<>();
        circle.addFirst(0);
        Long[] scores = new Long[numberOfElves];
        for (int i = 0; i < numberOfElves; i++) {
            scores[i] = 0L;
        }
        for (int i = 1; i <= lastMarble; i++) {
            if (i % 23 == 0) {
                circle.rotate(-7);
                scores[i % numberOfElves] += i + circle.pop();
            } else {
                circle.rotate(2);
                circle.addLast(i);
            }
        }
        return Arrays.stream(scores).max(Long::compareTo).get();
    }
}
