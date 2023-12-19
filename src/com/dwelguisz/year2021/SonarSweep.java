package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SonarSweep extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2021,1,false,0);
        List<Integer> values = convertStringsToInts(instructions);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = calculateIncreases(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        List<Integer> windowValues = calculateWindow(values);
        part2Answer = calculateIncreases(windowValues);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Integer calculateIncreases(List<Integer> values) {
        int increased = 0;
        for(int i = 0; i < values.size(); i++) {
            if (i == 0) {
                continue;
            }
            if (values.get(i) > values.get(i-1)) {
                increased++;
            }
        }
        return increased;
    }

    private List<Integer> calculateWindow(List<Integer> depths) {
        List<Integer> sums = new ArrayList<>();
        for(int i = 0; i < depths.size(); i++) {
            if (i < 2) {
                continue;
            }
            sums.add(depths.get(i) + depths.get(i-1) + depths.get(i-2));
        }
        return sums;
    }

}
