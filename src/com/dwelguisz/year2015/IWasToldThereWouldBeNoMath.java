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
            ints.add(Arrays.stream(l.split("x")).map(Integer::parseInt).collect(Collectors.toList()));
        }
        return ints;
    }

    Integer solutionPart1(List<List<Integer>> ribbons) {
        List<List<Integer>> ribbonAreas = ribbons.stream()
                .map(dim -> List.of(dim.get(0)*dim.get(1),dim.get(0)*dim.get(2),dim.get(1)*dim.get(2)))
                .toList();
        List<Integer> extraArea = ribbonAreas.stream()
                .map(areas -> areas.stream().mapToInt(a -> a).min().getAsInt())
                .toList();
        List<Integer> totalArea = ribbonAreas.stream()
                .map(areas -> areas.stream().mapToInt(a -> a).sum())
                .toList();
        return extraArea.stream().mapToInt(a -> a).sum() + totalArea.stream().mapToInt(a -> 2*a).sum();
    }

    Integer solutionPart2(List<List<Integer>> ribbons) {
        List<Integer> minimumPerimeter = ribbons.stream()
                .map(dims -> List.of(2*(dims.get(0)+dims.get(1)),2*(dims.get(0)+dims.get(2)),2*(dims.get(1)+dims.get(2))))
                .map(perimeters -> perimeters.stream().mapToInt(p -> p).min().getAsInt())
                .toList();
        List<Integer> ribbonVolume = ribbons.stream().map(dims -> dims.get(0)*dims.get(1)*dims.get(2))
                .toList();
        return minimumPerimeter.stream().mapToInt(p->p).sum() + ribbonVolume.stream().mapToInt(v->v).sum();
    }
}
