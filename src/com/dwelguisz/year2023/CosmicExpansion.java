package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CosmicExpansion extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 11, false, 0);
        char[][] map = parseMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(map);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(map);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }
    Set<Integer> emptyColNum;
    Set<Integer> emptyRowNum;
    List<Coord2D> galaxies;
    char[][] parseMap(List<String> lines) {
        char[][] map = convertToCharGrid(lines);
        galaxies = IntStream.range(0, map.length).boxed()
                .flatMap(row -> IntStream.range(0, map[row].length).boxed()
                        .map(col -> new Coord2D(row,col)))
                .filter(p -> map[p.x][p.y] == '#')
                .collect(Collectors.toList());
        Set<Integer> rowCoordinatesWithGalaxies = galaxies.stream().map(g -> g.x).collect(Collectors.toSet());
        Set<Integer> colCoordinatesWithGalaxies = galaxies.stream().map(g -> g.y).collect(Collectors.toSet());
        Set<Integer> allRows = IntStream.range(0,lines.size()).boxed().collect(Collectors.toSet());
        Set<Integer> allCols = IntStream.range(0,lines.get(0).length()).boxed().collect(Collectors.toSet());
        emptyRowNum = Sets.difference(allRows, rowCoordinatesWithGalaxies);
        emptyColNum = Sets.difference(allCols, colCoordinatesWithGalaxies);
        return map;
    }

    Long findSteps(Coord2D starA, Coord2D starB, Long expansion) {
        Integer minX = Integer.min(starA.x, starB.x);
        Integer maxX = Integer.max(starA.x, starB.x)+1;
        Integer minY = Integer.min(starA.y, starB.y);
        Integer maxY = Integer.max(starA.y, starB.y)+1;
        Long skippedRows =  IntStream.range(minX, maxX).boxed()
                .filter(i -> emptyRowNum.contains(i))
                .count();
        Long skippedCols = IntStream.range(minY, maxY).boxed()
                .filter(i -> emptyColNum.contains(i))
                .count();
        return starA.manhattanDistance(starB) + ((skippedRows+skippedCols)*(expansion-1));
    }
    Long calculateItems(char[][] map, Long expansion) {
        return IntStream.range(0, galaxies.size()).boxed()
                .flatMap(i -> IntStream.range(i+1,galaxies.size()).boxed().map(j -> Pair.of(galaxies.get(i), galaxies.get(j))))
                .mapToLong(p -> findSteps(p.getLeft(), p.getRight(), expansion))
                .sum();
    }
    Long solutionPart1(char[][] map) {
        return calculateItems(map, 2L);
    }

    Long solutionPart2(char[][] map) {
        return calculateItems(map, 1000000L);
    }
}
