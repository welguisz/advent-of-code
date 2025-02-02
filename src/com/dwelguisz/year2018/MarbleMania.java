package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarbleMania extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,9,false,0);
        List<Integer> values = parseLine(lines.get(0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values.get(1), values.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(values.get(1) * 100, values.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }


    List<Integer> parseLine(String line) {
        Pattern pattern = Pattern.compile("(?<playerSize>\\d+) players; last marble is worth (?<lastMarblePoint>\\d+) points");
        Matcher matcher = pattern.matcher(line);
        List<Integer> values = new ArrayList<>();
        if (matcher.find()) {
            values.add(Integer.parseInt(matcher.group("playerSize")));
            values.add(Integer.parseInt(matcher.group("lastMarblePoint")));
        }
        return values;
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
