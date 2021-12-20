package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day19.Coordinate;
import com.dwelguisz.year2021.helper.day19.Scanner;
import com.dwelguisz.year2021.helper.day19.ScannerMatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay19 {
    public static List<Scanner> scanners = new ArrayList<>();

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day19/testcase.txt");

        parseLines(lines);
        System.out.println(String.format("Number of scanners: %d", scanners.size()));
        List<Integer> answers = solutionPart1();
        System.out.println(String.format("Solution Part1: %d",answers.get(0)));
        System.out.println(String.format("Solution Part1: %d",answers.get(1)));
    }

    public static List<Integer> solutionPart1() {
        Scanner knownScanner = new Scanner(-5);
        List<Scanner> toHandleScanners = new ArrayList<>(scanners);
        Scanner first = scanners.get(0);
        knownScanner = first;
        toHandleScanners.remove(first);
        List<Coordinate> offsets = new ArrayList<>();
        offsets.add(new Coordinate(0,0,0));
        while (!toHandleScanners.isEmpty()) {
            System.out.println(String.format("%d scanners left", toHandleScanners.size()));
            ScannerMatch matched = findScannerMatches(knownScanner, scanners);
            if (matched != null) {
                knownScanner.addAllBeacons(matched.getBeacons());
                toHandleScanners.remove(matched.getRemoveScanner());
            }
            else {
                return List.of(-15,-16);
            }
        }
        Integer manhattan = Integer.MIN_VALUE;
        for (Coordinate beacon1 : offsets) {
            for (Coordinate beacon2 : offsets) {
                Coordinate tempBeacon = beacon1.subtract(beacon2);
                Coordinate absBeacon = new Coordinate(Math.abs(tempBeacon.x), Math.abs(tempBeacon.y), Math.abs(tempBeacon.z));
                Integer sumBeacon = absBeacon.x + absBeacon.y + absBeacon.z;
                manhattan = Integer.max(manhattan, sumBeacon);
            }
        }
        return List.of(knownScanner.getBeacons().size(), manhattan);
    }

    public static ScannerMatch findScannerMatches(Scanner knownScanner, List<Scanner> unknownScanners) {
        for (Scanner scanner : unknownScanners) {
            // Get all rotations
            for (Scanner altScanner : scanner.getAlternateScanners()) {
                // Let's start looking at each knownBeacon and compare to the current scanner and orientation
                for (Coordinate currentBeacon : knownScanner.getBeacons()) {
                    //Have to test this known alternate scanner individually
                    for (Coordinate testBeacon : altScanner.getBeacons()) {
                        Coordinate possibleScannerLoc = currentBeacon.subtract(testBeacon);
                        Set<Coordinate> newCoordinates = beaconLocationsGivenNewScannerLoc(possibleScannerLoc, altScanner.getBeacons());
                        if (checkBeacons(knownScanner.getBeacons(), newCoordinates)) {
                            return new ScannerMatch(possibleScannerLoc, altScanner, altScanner.getBeacons());
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Set<Coordinate> beaconLocationsGivenNewScannerLoc(Coordinate scanner, Set<Coordinate> beacons) {
        Set<Coordinate> newCoordinates = new HashSet<>();
        for (Coordinate b: beacons) {
            newCoordinates.add(scanner.add(b));
        }
        return newCoordinates;
    }

    public static boolean checkBeacons(Set<Coordinate> knownBeacons, Set<Coordinate> testBeacons) {
        int matches = 0;
        for (Coordinate t : testBeacons) {
            if (!knownBeacons.stream().filter(k -> k.equals(t)).collect(Collectors.toList()).isEmpty()) {
                matches++;
            }
        }
        return (matches >= 12);
    }

    public static void parseLines(List<String> lines) {
        Scanner currentScanner = new Scanner(-1);
        for (String line: lines) {
            if (line.contains("---")) {
                String[] splitLines = line.split(" ");
                if (currentScanner.getId() != -1) {
                    currentScanner.generateAlternateOrientation();
                    scanners.add(currentScanner);
                }
                    currentScanner = new Scanner(parseInt(splitLines[2]));
            } else if (line.contains(",")){
                List<Integer> coordinates = Arrays.stream(line.split(",")).map(str -> parseInt(str)).collect(Collectors.toList());
                currentScanner.addNewBeacon(new Coordinate(coordinates.get(0), coordinates.get(1), coordinates.get(2)));
            }
        }
        currentScanner.generateAlternateOrientation();
        scanners.add(currentScanner);
    }
}
