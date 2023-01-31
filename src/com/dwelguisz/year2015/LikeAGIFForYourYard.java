package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LikeAGIFForYourYard extends AoCDay {

    public static Coord2D NORTHWEST = new Coord2D(-1,-1);
    public static Coord2D NORTH = new Coord2D(-1,0);
    public static Coord2D NORTHEAST = new Coord2D(-1,1);
    public static Coord2D WEST = new Coord2D(0,-1);
    public static Coord2D EAST = new Coord2D(0,1);
    public static Coord2D SOUTHWEST = new Coord2D(1,-1);
    public static Coord2D SOUTH = new Coord2D(1,0);
    public static Coord2D SOUTHEAST = new Coord2D(1,1);
    public static List<Coord2D> NEIGHBORS = List.of(NORTHWEST, NORTH, NORTHEAST, WEST, EAST, SOUTHWEST, SOUTH, SOUTHEAST);

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,18,false,0);
        Set<Coord2D> lightsOn = createGrid(lines, false);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lightsOn, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        lightsOn = createGrid(lines, true);
        part2Answer = solutionPart1(lightsOn, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(Set<Coord2D> lights, Boolean part2) {
        for (int i = 0; i < 100; i++) {
            lights = updateGrid(lights, part2);
        }
        return lights.size();
    }

    public Set<Coord2D> createGrid(List<String> lines, boolean part2) {
        Set<Coord2D> newGrid = new HashSet<>();
        Integer row = 0;
        for (String line : lines) {
            char[] lights = line.toCharArray();
            int col = 0;
            for (Character l : lights) {
                if (l == '#') {
                    newGrid.add(new Coord2D(row,col));
                }
                col++;
            }
            row++;
        }
        if (part2) {
            newGrid.add(new Coord2D(0,0));
            newGrid.add(new Coord2D(0,99));
            newGrid.add(new Coord2D(99,0));
            newGrid.add(new Coord2D(99,99));
        }
        return newGrid;
    }

    public Set<Coord2D> updateGrid(final Set<Coord2D> lights, Boolean part2) {
        Set<Coord2D> lightsOn = new HashSet<>();
        IntStream.range(0,100).boxed().forEach(row -> {
            IntStream.range(0,100).boxed().forEach(col -> {
                if (newLight(lights, new Coord2D(row,col))) {
                    lightsOn.add(new Coord2D(row,col));
                }
            });
        });
        if (part2) {
            lightsOn.add(new Coord2D(0,0));
            lightsOn.add(new Coord2D(0,99));
            lightsOn.add(new Coord2D(99,0));
            lightsOn.add(new Coord2D(99,99));
        }
        return lightsOn;
    }

    public Boolean newLight(Set<Coord2D> lights, Coord2D coordinate) {
        Long sum = NEIGHBORS.stream().map(n -> coordinate.add(n)).filter(n -> lights.contains(n)).count();
        List<Long> keepOn = List.of(2L,3L);
        return (lights.contains(coordinate) && (keepOn.contains(sum))) || (!lights.contains(coordinate) && sum.equals(3L));
    }
}
