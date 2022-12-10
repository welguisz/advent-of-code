package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class ChocolateCharts extends AoCDay {

    public class ChocolateDeque<T> extends ArrayDeque<T> {

    }

    public void solve() {
        String part1 = solutionPart1(633601);
        System.out.println(String.format("Part 1 Answer: %s", part1));
        Integer part2 = solutionPart2("633601");
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public String solutionPart1(Integer count) {
        String result = "";
        Integer elves[] = new Integer[] {3,7};
        Integer elfPosition[] = new Integer[]{0,1};
        List<Integer> scores = new ArrayList<>();
        scores.addAll(List.of(elves));
        for (int i = 0; i < count + 10; i++) {
            Integer newScore = List.of(elves).stream().mapToInt(e -> e).sum();
            if (newScore > 9) {
                scores.add(newScore / 10);
            }
            scores.add(newScore % 10);
            for (int j = 0; j < 2; j++) {
                Integer newPosition = (1 + elfPosition[j] + elves[j]) % scores.size();
                elfPosition[j] = newPosition;
                elves[j] = scores.get(elfPosition[j]);
            }
        }
        for (int i = count; i < count + 10;i++) {
            result += scores.get(i);
        }
        return result;
    }

    public Integer solutionPart2(String target) {
        int length = target.length();
        String currentValue = "";
        for (int i = 0; i < length-2; i++) {
            currentValue += " ";
        }
        currentValue += "37";
        Integer elves[] = new Integer[] {3,7};
        Integer elfPosition[] = new Integer[]{0,1};
        List<Integer> scores = new ArrayList<>();
        scores.addAll(List.of(elves));
        Boolean done = false;
        while (!done) {
            Integer newScore = List.of(elves).stream().mapToInt(e -> e).sum();
            if (newScore > 9) {
                scores.add(newScore / 10);
                currentValue = currentValue.substring(1) + newScore/10;
                if (currentValue.equals(target)) {
                    done = true;
                }
            }
            if (!done) {
                scores.add(newScore % 10);
                currentValue = currentValue.substring(1) + newScore % 10;
                if (currentValue.equals(target)) {
                    done = true;
                }
            }
            for (int j = 0; j < 2; j++) {
                Integer newPosition = (1 + elfPosition[j] + elves[j]) % scores.size();
                elfPosition[j] = newPosition;
                elves[j] = scores.get(elfPosition[j]);
            }
        }
        return scores.size()-length;
    }

}
