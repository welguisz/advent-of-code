package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayDeque;
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
        printExplanation = true;
        documentation();
    }

    public List<String> parseLine(String line) {
        return Arrays.asList(line.split(""));
    }

    Integer visitedHouse(List<String> directions, Integer numberOfSantas) {
        Set<Coord2D> visited = new HashSet<>();
        ArrayDeque<Coord2D> locations = new ArrayDeque<>();
        for (int i = 0; i < numberOfSantas; i++) {
            locations.add(new Coord2D(0, 0));
        }
        visited.addAll(locations);
        for (String direction : directions) {
            Coord2D currentTurn = locations.pollFirst();
            currentTurn = currentTurn.add(DIRECTIONS.get(direction));
            visited.add(currentTurn);
            locations.addLast(currentTurn);
        }
        return visited.size();
    }

    Integer solutionPart1(List<String> directions) {
        return visitedHouse(directions, 1);
    }

    Integer solutionPart2(List<String> directions) {
        return visitedHouse(directions, 2);
    }

    void documentation() {
        puzzleName = "Perfectly Spherical Houses In A Vacuum";
        difficultLevel = "2 out of 10";
        inputDescription = "Lines of text with `^`,`v`,`>`,`<` characters corresponding to movements";
        skills = List.of("Set", "Coordinates");
        part1Solution = """
By setting up the Map, Part 1 just becomes storing the visited locations in a Set
and counting the number of elements in the set.

```java
    Integer solutionPart1(List<String> directions) {
        Set<Coord2D> visited = new HashSet<>();
        Coord2D santaLocation = new Coord2D(0,0);
        visited.add(santaLocation);
        for (String dir : directions) {
            santaLocation = santaLocation.add(DIRECTIONS.get(dir));
            visited.add(santaLocation);
        }
        return visited.size();
    }
```
""";
        part2Solution = """
For Part 2, we need to keep track of two Santas, the real one and a roboSanta.
We can use Part1 as a starting Point for Part 2 and add a new Coord2D for roboSanta.
For every iteration, we switch from Santa to Robo Santa and vice versa.

There are many ways to keep track of the two Santas, I prefer to keep track in a `ArrayDeque`.
This way if we add more Santas, it is just adding one more Coordinate.

```java
    Integer visitedHouse(List<String> directions, Integer numberOfSantas) {
        Set<Coord2D> visited = new HashSet<>();
        ArrayDeque<Coord2D> locations = new ArrayDeque<>();
        for (int i = 0; i < numberOfSantas; i++) {
            locations.add(new Coord2D(0, 0));
        }
        visited.addAll(locations);
        for (String direction : directions) {
            Coord2D currentTurn = locations.pollFirst();
            currentTurn = currentTurn.add(DIRECTIONS.get(direction));
            visited.add(currentTurn);
            locations.addLast(currentTurn);
        }
        return visited.size();
    }

    Integer solutionPart2(List<String> directions) {
        return visitedHouse(directions, 2);
    }
```
""";
        setup = """
In this problem, we are keeping track of houses visited.  This can be done in many ways. In today's explanation, we will just be using a Set that contains 2 dimensional values.

To make this problem easier, I created four 2-dimensional tuples that contain
the desired movement. From those movements, we can create a Map of the input
characters and the desired movement.

```java
public static Coord2D UP = new Coord2D(-1,0);
public static Coord2D RIGHT = new Coord2D(0,1);
public static Coord2D DOWN = new Coord2D(1,0);
public static Coord2D LEFT = new Coord2D(0,-1);
public static Map<String, Coord2D> DIRECTIONS = Map.of("^",UP,">",RIGHT,"v",DOWN,"<",LEFT);
```
""";
    }

}
