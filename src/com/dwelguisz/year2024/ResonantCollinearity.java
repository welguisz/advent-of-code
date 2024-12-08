package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResonantCollinearity extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 8, false, 0);
        char[][] grid = convertToCharGrid(lines);
        Map<Character, List<Coord2D>> antennas = find_antennas(grid);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solution(antennas, grid.length, grid[0].length, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solution(antennas, grid.length, grid[0].length, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Map<Character, List<Coord2D>> find_antennas(char[][] grid) {
        Map<Character, List<Coord2D>> antennas = new HashMap<>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == '.') {
                    continue;
                }
                antennas.computeIfAbsent(grid[y][x], k -> new ArrayList<>()).add(new Coord2D(x, y));
            }
        }
        return antennas;
    }

    Set<Coord2D> addAntiNodes(Coord2D antennaA, Coord2D antennaB, List<Integer> possibleXs, List<Integer> possibleYs, boolean part2) {
        Set<Coord2D> antiNodes = new HashSet<>();
        Coord2D diff = antennaA.add(antennaB.multiply(-1));
        if (part2) {
            if (antennaA.equals(antennaB)) {
                return Set.of();
            }
            Coord2D nextAntinode = antennaA.add(diff);
            antiNodes.add(antennaB);
            while (possibleXs.contains(nextAntinode.x) && possibleYs.contains(nextAntinode.y)) {
                antiNodes.add(nextAntinode);
                nextAntinode = nextAntinode.add(diff);
            }
        }
        else {
            if (antennaA.equals(antennaB)) {
                return Set.of();
            }
            Coord2D antinode = antennaA.add(diff);
            if (possibleXs.contains(antinode.x) && possibleYs.contains(antinode.y)) {
                return Set.of(antinode);
            }
            return Set.of();
        }
        return antiNodes;
    }

    Set<Pair<Coord2D, Coord2D>> createPairs(List<Coord2D> coords) {
        Set<Pair<Coord2D, Coord2D>> pairs = new HashSet<>();
        for (Coord2D coordA : coords) {
            for (Coord2D coordB : coords) {
                pairs.add(Pair.of(coordA, coordB));
            }
        }
        return pairs;
    }

    long solution(Map<Character, List<Coord2D>> antennas, int maxX, int maxY, boolean part2) {
        Set<Coord2D> antinodes = new HashSet<>();
        final List<Integer> possibleXs = IntStream.range(0, maxX).boxed().toList();
        final List<Integer> possibleYs = IntStream.range(0, maxY).boxed().toList();
        for (Map.Entry<Character, List<Coord2D>> entry : antennas.entrySet()) {
            Set<Pair<Coord2D, Coord2D>> pairs = createPairs(entry.getValue());
            Set<Set<Coord2D>> results = pairs.stream()
                    .map(p -> addAntiNodes(p.getLeft(), p.getRight(), possibleXs, possibleYs, part2))
                    .collect(Collectors.toSet());
            results.forEach(antinodes::addAll);
        }
        return antinodes.size();
    }
}
