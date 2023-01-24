package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerfectlySphericalHousesInAVacuum extends AoCDay {
    public static Coord2D UP = new Coord2D(-1,0);
    public static Coord2D RIGHT = new Coord2D(0,1);
    public static Coord2D DOWN = new Coord2D(1,0);
    public static Coord2D LEFT = new Coord2D(0,-1);
    public static Map<String, Coord2D> DIRECTIONS = Map.of("^",UP,">",RIGHT,"v",DOWN,"<",LEFT);

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,3,false,0);
        List<String> directions = parseLine(instructions.get(0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(directions);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(directions);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<String> parseLine(String line) {
        return Arrays.asList(line.split(""));
    }

    Integer visitedHouse(List<String> directions, Integer numberOfSantas) {
        Set<Coord2D> visited = new HashSet<>();
        List<Coord2D> locations = new ArrayList<>();
        for (int i = 0; i < numberOfSantas; i++) {
            locations.add(new Coord2D(0, 0));
        }
        Integer counter = 0;
        visited.addAll(locations);
        for (String direction : directions) {
            int choice = counter % locations.size();
            Coord2D currentTurn = locations.remove(choice);
            currentTurn = currentTurn.add(DIRECTIONS.get(direction));
            visited.add(currentTurn);
            locations.add(choice,currentTurn);
            counter++;
        }
        return visited.size();
    }

    Integer solutionPart1(List<String> directions) {
        return visitedHouse(directions, 1);
    }

    Integer solutionPart2(List<String> directions) {
        return visitedHouse(directions, 2);
    }


}
