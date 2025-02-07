package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class RambunctiousRecitation extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,15,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0), 2019);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lines.get(0), 30000000-1);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Integer solutionPart1(String line, int turns) {
        List<Integer> numbers = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Map<Integer, Integer> lastOccurence = numbers.stream()
                .collect(Collectors.toMap(Function.identity(), numbers::indexOf));
        int number = 0;
        for (int index = numbers.size(); index < turns;++index) {
            Integer last = lastOccurence.put(number, index);
            number = last == null ? 0 : index - last;
        }
        return number;
    }


}
