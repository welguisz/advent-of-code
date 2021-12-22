package com.dwelguisz.year2021.helper.day19;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("SuspiciousNameCombination")
@EqualsAndHashCode
public class Scanner {
    // To understand these functions, go to https://www.euclideanspace.com/maths/algebra/matrix/transforms/examples/index.htm
    public static final List<Function<Coordinate, Coordinate>> ROTATION_FUNCTIONS = List.of(
            Function.identity(),
            c -> new Coordinate(-c.y, c.x, c.z),
            c -> new Coordinate(-c.x, -c.y, c.z),
            c -> new Coordinate(c.y, -c.x, c.z));
    public static final List<Function<Coordinate, Coordinate>> DIRECTION_FUNCTIONS = List.of(
            Function.identity(),
            c -> new Coordinate(c.x, -c.y, -c.z),
            c -> new Coordinate(c.x, -c.z, c.y),
            c -> new Coordinate(-c.y, -c.z, c.x),
            c -> new Coordinate(-c.x, -c.z, -c.y),
            c -> new Coordinate(c.y, -c.z, -c.x));
    private final Integer number;
    private final List<Coordinate> coordinates;
    private final Coordinate scannerLocation;

    public Scanner(List<String> inputList) {
        number = Integer.parseInt(inputList.get(0).split(" ")[2]);
        coordinates = new ArrayList<>();
        for (int i = 1; i < inputList.size(); i++) {
            var line = inputList.get(i).trim();
            if (line.length() > 0) {
                coordinates.add(new Coordinate(line));
            }
        }
        // During matching, we should have a list of distances between beacons.
        // By having the distances between the beacons, we can just compare the distances
        // of the two coordinates.  If both coordinates have 12 or more relative distances to other
        // beacons, we know that these scanners are overlapping.
        coordinates.forEach(coordinate ->
                coordinates.forEach(other -> {
                    if (coordinate != other) {
                        coordinate.addDistance(other);
                    }
                })
        );
        //We are the center of the universe!!! AND DON'T TELL ME OTHERWISE
        scannerLocation = new Coordinate(0, 0, 0);
    }

    public Scanner(Integer number, List<Coordinate> coordinates, Coordinate scannerLocation) {
        this.number = number;
        this.coordinates = coordinates;
        coordinates.forEach(coordinate ->
                coordinates.forEach(other -> {
                    if (coordinate != other) {
                        coordinate.addDistance(other);
                    }
                })
        );
        this.scannerLocation = scannerLocation;
    }

    public Set<Scanner> rotate() {
        Set<Scanner> scanners = new HashSet<>();
        for (Function<Coordinate, Coordinate> rotFunction : ROTATION_FUNCTIONS) {
            for(Function<Coordinate, Coordinate> dirFunction : DIRECTION_FUNCTIONS) {
                List<Coordinate> copied = coordinates.stream()
                        .map(dirFunction)
                        .map(rotFunction)
                        .collect(toList());
                scanners.add(new Scanner(number, copied, null));
            }
        }
        return scanners;
    }

    public Scanner add(Coordinate offset) {
        return new Scanner(number, coordinates.stream()
                .map(coordinate -> new Coordinate(coordinate.x + offset.x, coordinate.y + offset.y, coordinate.z + offset.z))
                .collect(toList()), offset);
    }


    public Integer getNumber() {
        return number;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public Coordinate getScannerLocation() {
        return scannerLocation;
    }

    @Override
    public String toString() {
        return "Scanner{" +
                "number=" + number +
                ", coordinates=" + coordinates +
                '}';
    }
}