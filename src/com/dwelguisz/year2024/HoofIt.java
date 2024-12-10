package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.*;

import static com.dwelguisz.utilities.Grid.createIntegerGridMap;
import static com.dwelguisz.utilities.Grid.getStartingPoints;

public class HoofIt extends AoCDay {

    List<Coord2D> nextLocs = List.of(
            new Coord2D(-1,0),
            new Coord2D(1,0),
            new Coord2D(0, -1),
            new Coord2D(0, 1)
    );

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 10, false, 0);
        Map<Coord2D, Integer> grid = createIntegerGridMap(lines);
        List<Coord2D> startingPoints = getStartingPoints(grid, 0);
        List<Coord2D> endingPoints = getStartingPoints(grid, 9);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid, startingPoints, endingPoints);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid, startingPoints);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    long solutionPart1(Map<Coord2D, Integer> grid, List<Coord2D> startingPoints, List<Coord2D> endingPoints) {
        long ans = 0;
        for (Coord2D startingPoint : startingPoints) {
            for (Coord2D endingPoint : endingPoints) {
                ans += findPath(grid, startingPoint, endingPoint);
            }
        }
        return ans;
    }

    long solutionPart2(Map<Coord2D, Integer> grid, List<Coord2D> startingPoints) {
        long ans = 0;
        for (Coord2D startingPoint : startingPoints) {
            ans += findRatingsPath(grid, startingPoint);
        }
        return ans;
    }

    long findPath(
            Map<Coord2D, Integer> grid,
            Coord2D startingPoint,
            Coord2D endingPoint
    ) {
        Queue<Coord2D> states = new LinkedList<>();
        states.add(startingPoint);
        while (!states.isEmpty()) {
            Coord2D currentPoint = states.poll();
            Integer currentlevel = grid.get(currentPoint);
            List<Coord2D> tmp = nextLocs.stream()
                    .map(nextLoc -> nextLoc.add(currentPoint))
                    .filter(loc -> grid.getOrDefault(loc, -1000) == currentlevel+1)
                    .toList();
            if (tmp.stream().anyMatch(loc -> loc.equals(endingPoint))) {
                return 1;
            } else if (currentlevel != 8) {
                states.addAll(tmp);
            }
        }
        return 0;
    }

    long findRatingsPath(
            Map<Coord2D, Integer> grid,
            Coord2D startingLocation
    ) {
        Queue<Coord2D> states = new LinkedList<>();
        states.add(startingLocation);
        long found = 0;
        while (!states.isEmpty()) {
            Coord2D currentPoint = states.poll();
            Integer currentlevel = grid.get(currentPoint);
            List<Coord2D> tmp = nextLocs.stream()
                    .map(nextLoc -> nextLoc.add(currentPoint))
                    .filter(loc -> grid.getOrDefault(loc, -1000) == currentlevel+1)
                    .toList();
            if (currentlevel == 8) {
                found += tmp.size();
            } else {
                states.addAll(tmp);
            }
        }
        return found;
    }
}