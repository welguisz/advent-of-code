package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChronalCalibration extends AoCDay {
    public void solve(){
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,1,false,0);
        List<Long> values = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Long> parseLines(List<String> lines){
        return lines.stream().map(s -> s.charAt(0) == '-' ? Long.parseLong(s.substring(1)) * -1 : Long.parseLong(s)).toList();
    }

    public Long solutionPart1(List<Long> values) {
        return values.stream().reduce(0L, Long::sum);
    }


    public Long solutionPart2(List<Long> values) {
        Long current = 0L;
        Set<Long> previousFrequencies = new HashSet<>();
        previousFrequencies.add(current);
        while(true) {
            for (Long value : values) {
                current += value;
                if (previousFrequencies.contains(current)) {
                    return current;
                }
                previousFrequencies.add(current);
            }
        }
    }

}
