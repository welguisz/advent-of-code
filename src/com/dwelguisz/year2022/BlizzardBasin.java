package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dwelguisz.utilities.Coord2D.lcm;

public class BlizzardBasin extends AoCDay {

    String[][] map;
    List<Set<Coord2D>> freePath;

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day24/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        parsedLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1();
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public Integer modulo(Integer a, Integer b) {
        return (a >= 0) ? a % b : b - (Math.abs(a) % b);
    }

    Set<Coord2D> findFreeSpaces(Integer time) {
        Set<Coord2D> spaces = new HashSet<>();
        Integer yModulo = map.length - 2;
        Integer xModulo = map[0].length - 2;
        Set<Coord2D> blizzards = new HashSet<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length;x++) {
                if (!map[y][x].equals("#")) {
                    spaces.add(new Coord2D(y, x));
                }
                if (map[y][x].equals("^")) {
                    blizzards.add(new Coord2D(modulo(y-time-1,yModulo)+1,x));
                } else if (map[y][x].equals("v")) {
                    blizzards.add(new Coord2D(modulo(y+time-1,yModulo)+1,x));
                } else if (map[y][x].equals("<")) {
                    blizzards.add(new Coord2D(y,modulo(x-time-1,xModulo)+1));
                } else if (map[y][x].equals(">")) {
                    blizzards.add(new Coord2D(y,modulo(x+time-1,xModulo)+1));
                }
            }
        }
        spaces.removeAll(blizzards);
        return spaces;
    }
    void parsedLines(List<String> lines) {
        map = convertToGrid(lines);
        freePath = new ArrayList<>();
        //Blizzards will repeat since they will go in the same direction. So Blizzards going in the vertical
        //positions will repeat after map.length -2 (walls).  Blizzards going in the horizontal direction will
        //repeat after map[0].length -2.  Will create blizzards. After creating the blizzards through time, create
        //a sequence of maps that are available during each time period.
        int maxBlizzards = lcm((lines.size()-2),(lines.get(0).length()-2));
        for (int i = 0; i < maxBlizzards; i++) {
            freePath.add(findFreeSpaces(i));
        }
    }


    Integer solutionPart1() {
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(map.length-1,map[0].length-2);
        return findShortestPath(0,startingPoint, endingPoint);
    }

    Integer solutionPart2() {
        Coord2D startingPoint = new Coord2D(0,1);
        Coord2D endingPoint = new Coord2D(map.length-1,map[0].length-2);
        Integer time =  findShortestPath(0, startingPoint, endingPoint);
        time = findShortestPath(time+1, endingPoint, startingPoint);
        return findShortestPath(time+1,startingPoint, endingPoint);
    }

    Stream<Coord2D> nextSpots(Coord2D loc, Set<Coord2D> freeSpace) {
        List<Coord2D> possibleLocs = List.of(loc,
                loc.add(new Coord2D(-1,0)),
                loc.add(new Coord2D(0,1)),
                loc.add(new Coord2D(1,0)),
                loc.add(new Coord2D(0,-1)));
        return possibleLocs.stream()
                .filter(n -> freeSpace.contains(n));

    }
    public Integer findShortestPath(
            Integer initialTime,
            Coord2D startingLocation,
            Coord2D endingLocation
    ) {
        Set<Coord2D> states = new HashSet<>();
        states.add(startingLocation);
        for (Integer i = initialTime; true; i++) {
            Set<Coord2D> openSpaces = freePath.get((i +1 )% freePath.size());
            states = states.stream().flatMap(s -> nextSpots(s,openSpaces)).collect(Collectors.toSet());
            if (states.stream().anyMatch(s -> s.equals(endingLocation))) {
                return i+1;
            }
        }
    }


}
