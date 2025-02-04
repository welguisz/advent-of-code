package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RocketEquation extends AoCDay {
    public void solve(){
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,1,false,0);
        List<Integer> values = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<Integer> values) {
        Long sum = 0L;
        for (Integer value : values) {
            sum += (value / 3) - 2;
        }
        return sum;
    }

    public Long solutionPart2(List<Integer> values) {
        Long sum = 0L;
        for (Integer value : values) {
            Integer addedFuel = value;
            List<Integer> addedFuelMass = new ArrayList<>();
            while (addedFuel > 0) {
                addedFuel = (addedFuel / 3) - 2;
                if (addedFuel > 0) {
                    addedFuelMass.add(addedFuel);
                }
            }
            sum += addedFuelMass.stream().reduce(0, Integer::sum);
        }
        return sum;
    }
}
