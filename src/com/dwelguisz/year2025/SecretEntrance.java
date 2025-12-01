package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SecretEntrance extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 1, false, 0);
        List<Integer> values = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Integer> parseLines(List<String> lines) {
        List<Integer> values = new ArrayList<>();
        for(String line : lines) {
            int sign = line.substring(0,1).equals("R") ? 1 : -1;
            int value = Integer.parseInt(line.substring(1));
            values.add(sign * value);
        }
        return values;
    }

    public long solutionPart1(List<Integer> values) {
        int current = 50;
        int hit = 0;
        for (int value : values) {
            current += value;
            while (current >= 100) {
                current -= 100;
            }
            while (current < 0) {
                current += 100;
            }
            if (current == 0)
                hit++;
        }
        return hit;
    }

    public long solutionPart2(List<Integer> values) {
        int current = 50;
        int hit = 0;
        for (int value : values) {
            int newValue = current + value;
            hit += rotation(current, newValue);
            current = newValue;
        }
        return hit;
    }

    int rotation(int current, int newValue) {
        if (current < newValue) {
            return abs(Math.floorDiv(newValue, 100) - Math.floorDiv(current, 100));
        }
        else if (current > newValue)
            return rotation(-current, -newValue);
        else
            return 0;
    }
}
