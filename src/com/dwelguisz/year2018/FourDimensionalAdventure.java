package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord4D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FourDimensionalAdventure extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,25,false,0);
        List<Coord4D> coordinates = parsedLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(coordinates);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "Press Link to continue";
        timeMarkers[3] = Instant.now().toEpochMilli();
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
