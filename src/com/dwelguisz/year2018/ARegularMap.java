package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ARegularMap extends AoCDay {

    public static Coord2D NORTH = new Coord2D(-1,0);
    public static Coord2D EAST = new Coord2D(0,1);
    public static Coord2D SOUTH = new Coord2D(1,0);
    public static Coord2D WEST = new Coord2D(0,-1);

    public static Map<String, Coord2D> MOVES = Map.of("N",NORTH,"E",EAST,"S",SOUTH,"W",WEST);

    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day20/input.txt");
        Map<Coord2D,Long> map = parseLines(lines.get(0));
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(map);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(map);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Map<Coord2D, Long> parseLines(String line) {
        Map<Coord2D, Long> map = new HashMap<>();
        Integer len = parse(line,1,map,new Coord2D(0,0));
        return map;
    }

    Integer parse(String expression, Integer position, Map<Coord2D, Long> map, Coord2D startingLocation) {
        Coord2D currentLocation = new Coord2D(startingLocation.x, startingLocation.y);
        Long distance = map.getOrDefault(currentLocation,0L);
        String currentChar = expression.substring(position, position+1);
        while (!currentChar.equals("$") && !currentChar.equals(")")) {
            if (currentChar.equals("(")) {
                position = parse(expression,position+1,map,currentLocation);
            } else if (currentChar.equals("|")) {
                currentLocation = new Coord2D(startingLocation.x, startingLocation.y);
                distance = map.getOrDefault(currentLocation, 0L);
            } else {
                currentLocation = currentLocation.add(MOVES.get(currentChar));
                if (map.containsKey(currentLocation)) {
                    distance = map.get(currentLocation);
                } else {
                    distance += 1;
                    map.put(currentLocation,distance);
                }
            }
            position++;
            currentChar = expression.substring(position, position+1);
        }
        return position;
    }
    Long solutionPart1(Map<Coord2D, Long> map) {
        return map.entrySet().stream().mapToLong(e -> e.getValue()).max().getAsLong();
    }

    Long solutionPart2(Map<Coord2D, Long> map) {
        return map.entrySet().stream().filter(e -> e.getValue() >= 1000).count();
    }

}
