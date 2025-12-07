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

public class Laboratories extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 7, false, 0);
        int maxLine = lines.size();
        Map<Coord2D, Character> fullgrid = createCharGridMap(lines);
        Map<Coord2D, Character> grid = fullgrid.entrySet().stream()
                .filter(e -> e.getValue() != '.')
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid, maxLine);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid, maxLine);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    long solutionPart1(Map<Coord2D, Character> grid, int maxLine) {
        Coord2D startingPoint = grid.entrySet().stream().filter(e -> e.getValue() == 'S')
                .findFirst().get().getKey();

        Set<Coord2D> currentRow = new HashSet<>();
        currentRow.add(startingPoint);
        int split = 0;
        for(int i = 1; i < maxLine; i++) {
            Set<Coord2D> nextRow = new HashSet<>();
            for (Coord2D loc : currentRow) {
                if (!grid.containsKey(new Coord2D(i, loc.y))) {
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

    long solutionPart2(Map<Coord2D, Character> grid, int maxLine) {
        Coord2D startingPoint = grid.entrySet().stream().filter(e -> e.getValue() == 'S')
                .findFirst().get().getKey();
        HashMap<Coord2D, Long> counts = new HashMap<>();
        Set<Coord2D> currentRow = new HashSet<>();
        currentRow.add(startingPoint);
        counts.put(startingPoint, 1l);
        for(int i = 1; i < maxLine; i++) {
            Set<Coord2D> nextRow = new HashSet<>();
            for (Coord2D loc : currentRow) {
                if (!grid.containsKey(new Coord2D(i,loc.y))) {
                    Coord2D thisLoc = new Coord2D(i,loc.y);
                    nextRow.add(thisLoc);
                    counts.put(thisLoc, counts.get(loc) + counts.getOrDefault(thisLoc, 0L));
                } else {
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

        final int lastRow = maxLine-1;
        return counts.entrySet().stream().filter(k -> k.getKey().x == lastRow)
                .map(e -> e.getValue())
                .reduce(0l, Long::sum);
    }

}
