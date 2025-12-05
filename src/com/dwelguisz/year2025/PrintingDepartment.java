package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.utilities.Grid.createCharGridMap;

public class PrintingDepartment extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 4, false, 0);
        Set<Coord2D> coords = parseLines(lines);
        Map<Coord2D, Character> basement = createCharGridMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(coords);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(coords);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    Set<Coord2D> parseLines(List<String> lines) {
        Set<Coord2D> result = new HashSet<>();
        int x = 0;
        for (String line : lines) {
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '@') {
                    result.add(new Coord2D(x, i));
                }
            }
            x++;
        }
        return result;
    }

    final List<Coord2D> nextValues = List.of(new Coord2D(-1,-1), new Coord2D(-1, 0), new Coord2D(-1, 1),
            new Coord2D(0, -1), new Coord2D(0,1),
            new Coord2D(1, -1), new Coord2D(1, 0), new Coord2D(1, 1));

    public long solutionPart1(Set<Coord2D> coords) {
        long answer = 0;
        for (Coord2D coord : coords) {
            long count = nextValues.stream()
                    .map(n -> n.add(coord))
                    .filter(n -> coords.contains(n)).count();
            answer += (count < 4) ? 1 : 0;
        }
        return answer;
    }

    public long solutionPart2(Set<Coord2D> coords) {
        long answer = 0l;
        long touch = 1l;
        while (touch > 0) {
            touch = 0l;
            List<Coord2D> updateCoords = new ArrayList<>();
            for (Coord2D coord : coords) {
                long count = nextValues.stream()
                        .map(n -> n.add(coord))
                        .filter(n -> coords.contains(n)).count();
                if (count < 4) {
                    touch++;
                    updateCoords.add(coord);
                }
            }
            updateCoords.forEach(c -> coords.remove(c));
            answer += touch;
        }
        return answer;
    }
}
