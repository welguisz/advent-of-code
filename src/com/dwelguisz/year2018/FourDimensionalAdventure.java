package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord4D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FourDimensionalAdventure extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day25/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<Coord4D> coordinates = parsedLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(coordinates);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public List<Coord4D> parsedLines(List<String> lines) {
        List<Coord4D> coordinates = new ArrayList<>();
        for (String l : lines) {
            String tmp[] = l.split(",");
            coordinates.add(new Coord4D(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3])));
        }
        Coord4D origin = new Coord4D(0,0,0,0);
        return coordinates.stream().sorted(Comparator.comparing(a -> a.manhattanDistance(origin))).collect(Collectors.toList());
    }

    public Integer solutionPart1(List<Coord4D> coordinates) {
        List<Set<Coord4D>> constellations = new ArrayList<>();
        for(Coord4D coordinate : coordinates) {
            List<Set<Coord4D>> newConstellations = new ArrayList<>();
            Boolean found = false;
            for (Set<Coord4D> constell : constellations) {
                if (found) {
                    newConstellations.add(constell);
                    continue;
                }
                Set<Coord4D> currentConstellation = new HashSet<>();
                for (Coord4D star : constell) {
                    if (coordinate.manhattanDistance(star) <= 3) {
                        currentConstellation.add(coordinate);
                        found = true;
                    }
                    currentConstellation.add(star);
                }
                newConstellations.add(currentConstellation);
            }
            if (!found) {
                Set<Coord4D> newConstellation = new HashSet<>();
                newConstellation.add(coordinate);
                newConstellations.add(newConstellation);
            }
            constellations = newConstellations;
        }
        Boolean reducedSize = true;
        while (reducedSize) {
            List<Set<Coord4D>> newConstellations = new ArrayList<>();
            List<Integer> ignoreIndices = new ArrayList<>();
            Integer outerIndex = 0;
            for (Set<Coord4D> constellation : constellations) {
                if (ignoreIndices.contains(outerIndex)) {
                    outerIndex++;
                    continue;
                }
                Set<Coord4D> newConstellation = new HashSet<>(constellation);
                ignoreIndices.add(outerIndex);
                Integer innerIndex = 0;
                for (Set<Coord4D> constell : constellations) {
                    if (ignoreIndices.contains(innerIndex)) {
                        innerIndex++;
                        continue;
                    }
                    for (Coord4D coordinate : constell) {
                        if (constellation.stream().anyMatch(c -> c.manhattanDistance(coordinate) <= 3)) {
                            ignoreIndices.add(innerIndex);
                            newConstellation.addAll(constell);
                        }
                    }
                    innerIndex++;
                }
                newConstellations.add(newConstellation);
                outerIndex++;
            }
            if (newConstellations.size() == constellations.size()) {
                reducedSize = false;
            }
            constellations = newConstellations;
        }
        return constellations.size();
    }

    public Integer solutionPart2() {
        return 0;

}
}
