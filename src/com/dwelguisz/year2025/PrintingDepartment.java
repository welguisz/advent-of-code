package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dwelguisz.utilities.Grid.createCharGridMap;

public class PrintingDepartment extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 4, false, 0);
        Map<Coord2D, Character> basement = createCharGridMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(basement);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(basement);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    final List<Coord2D> nextValues = List.of(new Coord2D(-1,-1), new Coord2D(-1, 0), new Coord2D(-1, 1),
            new Coord2D(0, -1), new Coord2D(0,1),
            new Coord2D(1, -1), new Coord2D(1, 0), new Coord2D(1, 1));

    public long solutionPart1(Map<Coord2D, Character> basement) {
        long answer = 0;
        for (final Map.Entry<Coord2D, Character> entry : basement.entrySet()) {
            if (entry.getValue() == '.') { continue;}
            long count = nextValues.stream()
                    .map(n -> n.add(entry.getKey()))
                    .map(n -> basement.getOrDefault(n, '.'))
                    .filter(v -> v == '@').count();
            answer += (count < 4) ? 1 : 0;
        }
        return answer;
    }

    public long solutionPart2(Map<Coord2D, Character> basement) {
        long answer = 0l;
        long touch = 1l;
        while (touch > 0) {
            touch = 0l;
            List<Coord2D> updateCoords = new ArrayList<>();
            for (final Map.Entry<Coord2D, Character> entry : basement.entrySet()) {
                if (entry.getValue() == '.') { continue;}
                long count = nextValues.stream()
                        .map(n -> n.add(entry.getKey()))
                        .map(n -> basement.getOrDefault(n, '.'))
                        .filter(v -> v == '@').count();
                if (count < 4) {
                    touch++;
                    updateCoords.add(entry.getKey());
                }
            }
            updateCoords.forEach(c -> basement.put(c, '.'));
            answer += touch;
        }
        return answer;
    }
}
