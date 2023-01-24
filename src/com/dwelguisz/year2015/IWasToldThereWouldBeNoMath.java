package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class IWasToldThereWouldBeNoMath extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,2,false,0);
        List<List<Integer>> ribbons = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(ribbons);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(ribbons);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<List<Integer>> parseLines(List<String> lines) {
        List<List<Integer>> ints = new ArrayList<>();
        for (String l : lines) {
            ints.add(Arrays.stream(l.split("x")).map(str -> parseInt(str)).collect(Collectors.toList()));
        }
        return ints;
    }

    Long solutionPart1(List<List<Integer>> ribbons) {
        Long total = 0L;
        for(List<Integer> dims : ribbons) {
            Integer l = dims.get(0);
            Integer w = dims.get(1);
            Integer h = dims.get(2);
            List<Integer> areas = Arrays.asList(l*w, l*h, w*h);
            Integer extra = areas.stream().min(Integer::compareTo).get();
            Integer totalArea = ((areas.get(0) + areas.get(1) + areas.get(2)) * 2)  + extra;
            total += totalArea;
        }
        return total;
    }

    Long solutionPart2(List<List<Integer>> ribbons) {
        Long total = 0L;
        for(List<Integer> dims : ribbons) {
            Integer l = dims.get(0);
            Integer w = dims.get(1);
            Integer h = dims.get(2);
            List<Integer> perimeters = Arrays.asList(2 * (l + w), 2 * (l + h), 2 * (h + w));
            Integer perimeter = perimeters.stream().min(Integer::compareTo).get();
            Integer volume = l * w * h;
            Integer totalRibbon = perimeter + volume;
            total += totalRibbon;
        }
        return total;
    }
}
