package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dwelguisz.utilities.Grid.createCharGridMap;

public class Laboratories extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 7, false, 0);
        Map<Coord2D, Character> grid = createCharGridMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    long solutionPart1(Map<Coord2D, Character> grid) {
        Coord2D startingPoint = grid.entrySet().stream().filter(e -> e.getValue() == 'S')
                .findFirst().get().getKey();
        int maxY = grid.entrySet().stream().map(e -> e.getKey().x).max(Integer::compare).get();

        Set<Coord2D> currentRow = new HashSet<>();
        currentRow.add(startingPoint);
        int split = 0;
        for(int i = 1; i < maxY; i++) {
            Set<Coord2D> nextRow = new HashSet<>();
            for (Coord2D loc : currentRow) {
                if (grid.get(new Coord2D(i, loc.y)) == '.') {
                    nextRow.add(new Coord2D(i,loc.y));
                } else if (grid.get(new Coord2D(i,loc.y)) == '^') {
                    split++;
                    nextRow.add(new Coord2D(i,loc.y - 1));
                    nextRow.add(new Coord2D(i,loc.y + 1));
                }
            }
            currentRow = nextRow;
        }
        return split;
    }

    long solutionPart2(Map<Coord2D, Character> grid) {
        Coord2D startingPoint = grid.entrySet().stream().filter(e -> e.getValue() == 'S')
                .findFirst().get().getKey();
        int maxY = grid.entrySet().stream().map(e -> e.getKey().x).max(Integer::compare).get();
        HashMap<Coord2D, Long> counts = new HashMap<>();
        Set<Coord2D> currentRow = new HashSet<>();
        currentRow.add(startingPoint);
        counts.put(startingPoint, 1l);
        for(int i = 1; i < maxY; i++) {
            Set<Coord2D> nextRow = new HashSet<>();
            for (Coord2D loc : currentRow) {
                if (grid.get(new Coord2D(i,loc.y)) == '.') {
                    Coord2D thisLoc = new Coord2D(i,loc.y);
                    nextRow.add(thisLoc);
                    counts.put(thisLoc, counts.get(loc) + counts.getOrDefault(thisLoc, 0L));
                } else if (grid.get(new Coord2D(i,loc.y)) == '^') {
                    Coord2D thisRow = new Coord2D(i, loc.y-1);
                    counts.put(thisRow, counts.get(loc) + counts.getOrDefault(thisRow, 0l));
                    thisRow = new Coord2D(i, loc.y+1);
                    counts.put(thisRow, counts.get(loc) + counts.getOrDefault(thisRow, 0l));
                    nextRow.add(new Coord2D(i, loc.y - 1));
                    nextRow.add(new Coord2D(i, loc.y + 1));
                }
            }
            currentRow = nextRow;
        }

        final int lastRow = maxY-1;
        return counts.entrySet().stream().filter(k -> k.getKey().x == lastRow)
                .map(e -> e.getValue())
                .reduce(0l, Long::sum);
    }

}
