package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SettlersOfTheNorthPole extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day18/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Map<Coord2D,String> map = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(new HashMap<>(map));
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(new HashMap<>(map));
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Map<Coord2D, String> parseLines(List<String> lines) {
        Integer y = 0;
        Map<Coord2D, String> map = new HashMap<>();
        for (String l : lines) {
            String[] t = l.split("");
            for (int x = 0; x < t.length; x++) {
                map.put(new Coord2D(y,x),t[x]);
            }
            y++;
        }
        return map;
    }

    Long solutionPart1(Map<Coord2D, String> map) {
        for (int i = 1; i <= 10; i++) {
            map = goOneMinute(map);
        }
        return calculateResourceValue(map);
    }

    Long solutionPart2(Map<Coord2D, String> map) {
        Map<Map<Coord2D, String>, Long> previousMaps = new HashMap<>();
        Long currentMinute = 0L;
        Boolean noPattern = true;
        while (currentMinute < 1000000000L) {
            if (previousMaps.containsKey(map) && noPattern) {
                Long repeatMinutes = currentMinute - previousMaps.get(map);
                Long diff = 1000000000L - currentMinute;
                Long multiplier = diff/repeatMinutes;
                Long result = repeatMinutes * multiplier + currentMinute;
                currentMinute = result;
                noPattern = false;
            } else {
                previousMaps.put(map, currentMinute);
                map = goOneMinute(map);
                currentMinute++;
            }
        }
        return calculateResourceValue(map);
    }

    Long calculateResourceValue(Map<Coord2D, String> map) {
        Long treeSpaces = map.entrySet().stream().filter(n -> n.getValue().equals("|")).count();
        Long lumberyardSpaces = map.entrySet().stream().filter(n -> n.getValue().equals("#")).count();
        return treeSpaces * lumberyardSpaces;
    }

    List<Coord2D> NEIGHBORS = List.of(new Coord2D(-1,-1), new Coord2D(-1,0), new Coord2D(-1,1),
            new Coord2D(0,-1), new Coord2D(0,1),
            new Coord2D(1,-1), new Coord2D(1,0), new Coord2D(1,1));

    Map<Coord2D, String> goOneMinute(Map<Coord2D, String> map) {
        Map<Coord2D, String> newMap = new HashMap<>();
        for (Map.Entry<Coord2D, String> grid : map.entrySet()) {
            final Coord2D current = grid.getKey();
            List<String> neighbors = NEIGHBORS.stream().map(n -> current.add(n))
                    .map(n -> map.getOrDefault(n,"$"))
                    .collect(Collectors.toList());
            Long openSpaces = neighbors.stream().filter(n -> n.equals(".")).count();
            Long treeSpaces = neighbors.stream().filter(n -> n.equals("|")).count();
            Long lumberyardSpaces = neighbors.stream().filter(n -> n.equals("#")).count();
            if (grid.getValue().equals(".")) {
                if (treeSpaces >= 3L) {
                    newMap.put(current, "|");
                } else {
                    newMap.put(current,".");
                }
            } else if (grid.getValue().equals("|")) {
                if (lumberyardSpaces >= 3L) {
                    newMap.put(current, "#");
                } else {
                    newMap.put(current, "|");
                }
            } else if (grid.getValue().equals("#")) {
                if (lumberyardSpaces >= 1L && treeSpaces >= 1L) {
                    newMap.put(current, "#");
                } else {
                    newMap.put(current, ".");
                }
            }
        }
        return newMap;
    }
}
