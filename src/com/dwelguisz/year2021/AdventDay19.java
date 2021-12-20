package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day19.Coordinate;
import com.dwelguisz.year2021.helper.day19.Scanner;

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

    public static Scanner findScannerById(List<Scanner> scanners, Integer scannerId) {
        return scanners.stream().filter(scanner -> scanner.getId() == scannerId).findFirst().get();
    }

    public static List<Integer> solutionPart1() {
        // Going with 6812, the first MCU that I programmed on....
        Scanner knownScanner = new Scanner(6812);
        List<Scanner> toHandleScanners = new ArrayList<>(scanners);
        Scanner first = scanners.get(0);
        knownScanner.addAllBeacons(first.getBeacons());
        toHandleScanners.remove(first);
        List<Coordinate> offsets = new ArrayList<>();
        offsets.add(new Coordinate(0,0,0));

        while (!toHandleScanners.isEmpty()) {
            System.out.println(String.format("%d scanners left", toHandleScanners.size()));
            Scanner finalScanner = findScannerMatches(knownScanner, toHandleScanners);
            if (finalScanner != null) {
                Scanner findScanner = findScannerById(toHandleScanners, finalScanner.getId());
                knownScanner.addAllBeacons(finalScanner.getBeacons());
                toHandleScanners.remove(findScanner);
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

    public static Scanner findScannerMatches(Scanner knownScanner, List<Scanner> unknownScanners) {
        for (Scanner scanner : unknownScanners) {
            List<List<Coordinate>> reallyGoodMatches = getMatches(knownScanner, scanner);
            if (reallyGoodMatches.size() >= 12) {
                return determinedRotation(knownScanner, scanner);
            }
        }
        return null;
    }

    public static List<List<Coordinate>> getMatches(Scanner known, Scanner maybe) {
        List<List<Coordinate>> coordinateList = new ArrayList<>();
        known.getBeacons().forEach(coordinate -> {
            maybe.getBeacons().forEach(coordinate2 -> {
                List<Double> distances = coordinate.getRelativeDistance().stream()
                        .filter(distance -> coordinate2.relativeDistances.contains(distance))
                        .collect(Collectors.toList());
                if (distances.size() > 10) {
                    coordinateList.add(List.of(coordinate, coordinate2));
                }
            });
        });
        return coordinateList;
    }

    // 0 - scannerId (Integer)
    // 1 - rotatedId (Integer)
    // 2 - new scannerLocation (Coordinate)
    public static Scanner determinedRotation(Scanner knownScanner, Scanner matched) {
        List<Scanner> matchedRotation = matched.getAlternateScanners().stream()
                .filter(rotated -> isMatch(knownScanner, rotated) != null)
                .collect(Collectors.toList());
        assert matchedRotation.size() == 1;
        Scanner matchedScanner = matchedRotation.get(0);
        Coordinate matchedCoordinate = isMatch(knownScanner, matchedScanner);
        return matchedScanner.add(matchedCoordinate);
    }

    public static Coordinate isMatch(Scanner known, Scanner rotatedScanner) {
        List<List<Coordinate>> matches = getMatches(known, rotatedScanner);
        return isMatching(matches);
    }

    public static Coordinate isMatching(List<List<Coordinate>> list) {
        Coordinate matchScannerPosition = null;
        for(List<Coordinate> value : list) {
            Coordinate temp = value.get(1).determineNewPosition(value.get(0));
            if (matchScannerPosition == null) {
                matchScannerPosition = temp;
            } else if (!matchScannerPosition.equals(temp)) {
                return null;
            }
        }
        return matchScannerPosition;
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
