package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.Tuple;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

//Year 2021, Day 01
public class ReportRepair extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,1,false,0);
        List<Integer> values = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = findValues(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = findValuesPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Long findValues(List<Integer> values) {
        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                if (values.get(i) + values.get(j) == 2020) {
                    return (long) values.get(i) * values.get(j);
                }
            }
        }
        return -1L;
    }

    private Long findValuesPart2(List<Integer> values) {

        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                for (int k = j + 1; k < values.size(); k++) {
                    if (values.get(i) + values.get(j) + values.get(k) == 2020) {
                        return 1L * values.get(i) * values.get(j) * values.get(k);
                    }
                }
            }
        }
        return -1L;
    }


}
