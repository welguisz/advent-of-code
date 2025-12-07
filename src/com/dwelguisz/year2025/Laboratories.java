package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.utilities.Grid.createCharGridMap;
import static com.dwelguisz.utilities.Grid.getStartingPoint;

public class Laboratories extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 7, false, 0);
        int maxLine = lines.size();
        Map<Coord2D, Character> grid = createCharGridMap(lines);
        Coord2D startingPoint = getStartingPoint(grid, 'S');
        Set<Coord2D> splitters = grid.entrySet().stream().filter(e -> e.getValue() == '^')
                .map(e -> e.getKey()).collect(Collectors.toSet());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(splitters, startingPoint, maxLine);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(splitters, startingPoint, maxLine);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    long solutionPart1(Set<Coord2D> splitters, Coord2D startingPoint, int maxLine) {
        Set<Coord2D> currentRow = new HashSet<>();
        currentRow.add(startingPoint);
        int split = 0;
        for(int i = 1; i < maxLine; i++) {
            Set<Coord2D> nextRow = new HashSet<>();
            for (Coord2D loc : currentRow) {
                if (!splitters.contains(new Coord2D(i, loc.y))) {
                    nextRow.add(new Coord2D(i,loc.y));
                } else {
                    split++;
                    nextRow.add(new Coord2D(i,loc.y - 1));
                    nextRow.add(new Coord2D(i,loc.y + 1));
                }
            }
            currentRow = nextRow;
        }
        return split;
    }

    long solutionPart2(Set<Coord2D> splitters, Coord2D startingPoint, int maxLine) {
        HashMap<Coord2D, Long> counts = new HashMap<>();
        Set<Coord2D> currentRow = new HashSet<>();
        currentRow.add(startingPoint);
        counts.put(startingPoint, 1l);
        for(int i = 1; i < maxLine; i++) {
            Set<Coord2D> nextRow = new HashSet<>();
            for (Coord2D loc : currentRow) {
                if (!splitters.contains(new Coord2D(i,loc.y))) {
                    Coord2D thisLoc = new Coord2D(i,loc.y);
                    nextRow.add(thisLoc);
                    counts.merge(thisLoc, counts.get(loc), (a, b) -> a + b);
                } else {
                    Coord2D thisRow = new Coord2D(i, loc.y-1);
                    counts.merge(thisRow, counts.get(loc), (a, b) -> a + b);
                    thisRow = new Coord2D(i, loc.y+1);
                    counts.merge(thisRow, counts.get(loc), (a, b) -> a + b);
                    nextRow.add(new Coord2D(i, loc.y - 1));
                    nextRow.add(new Coord2D(i, loc.y + 1));
                }
            }
            currentRow = nextRow;
        }

        final int lastRow = maxLine-1;
        return counts.entrySet().stream().filter(k -> k.getKey().x == lastRow)
                .map(e -> e.getValue())
                .reduce(0l, Long::sum);
    }

}
