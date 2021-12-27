package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.Tuple;
import com.dwelguisz.year2021.helper.day19.Coordinate;
import com.dwelguisz.year2021.helper.day19.Scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BeaconScanner extends AoCDay {
    private List<Scanner> scannerList;

    public BeaconScanner() {
        super();
        scannerList = new ArrayList<>();
    }

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day19/input.txt");
        parseLines(lines);
        System.out.println(String.format("Number of scanners: %d", scannerList.size()));
        Tuple<Set<Coordinate>,Integer> answers = compare();
        System.out.println("--------- Day 19: Beacon Scanner------------");
        System.out.println(String.format("Solution Part1: %d",answers.x.size()));
        System.out.println(String.format("Solution Part1: %d",answers.y));
    }

    private void parseLines(List<String> lines) {
        List<String> inputLines = new ArrayList<>();
        for (String line: lines) {
            if (line.contains("---")) {
                if (!inputLines.isEmpty()) {
                    Scanner newScanner = new Scanner(inputLines);
                    inputLines = new ArrayList<>();
                    scannerList.add(newScanner);
                }
            }
            inputLines.add(line);
        }
        Scanner newScanner = new Scanner(inputLines);
        scannerList.add(newScanner);
    }

   private Tuple<Set<Coordinate>, Integer> compare() {
        List<Scanner> resolvedScanners = new ArrayList<>();
        List<Scanner> toHandleScanners = new ArrayList<>(scannerList);
        Scanner first = scannerList.get(0);
        resolvedScanners.add(first);
        toHandleScanners.remove(first);

        while (toHandleScanners.size() > 0) {
            Scanner newMatchedScanner = getNewMatchedScanner(resolvedScanners, toHandleScanners);
            resolvedScanners.add(newMatchedScanner);
            toHandleScanners.removeIf(scanner -> scanner.getNumber().equals(newMatchedScanner.getNumber()));
        }

        Set<Coordinate> beacons = resolvedScanners.stream()
                .flatMap(scanner -> scanner.getCoordinates().stream())
                .collect(Collectors.toSet());
        int maxDistance = 0;
        for (int i = 0; i < resolvedScanners.size(); i++) {
            for (int j = i + 1; j < resolvedScanners.size(); j++) {
                Scanner scanner1 = resolvedScanners.get(i);
                Scanner scanner2 = resolvedScanners.get(j);
                int manhattanDistance = scanner1.getScannerLocation().manhattanDistance(scanner2.getScannerLocation());
                if (manhattanDistance > maxDistance) {
                    maxDistance = manhattanDistance;
                }
            }
        }
        return new Tuple<>(beacons, maxDistance);

    }

    private Scanner getNewMatchedScanner(List<Scanner> resolvedScanners, List<Scanner> toHandleScanners) {
        for (Scanner scanner : resolvedScanners) {
            for (Scanner matchScanner : toHandleScanners) {
                List<Tuple<Coordinate, Coordinate>> coordinateList = getMatches(scanner, matchScanner);
                if (coordinateList.size() >= 12) {
                    return determine(scanner, matchScanner);
                }
            }

        }
        return null;
    }

    private List<Tuple<Coordinate, Coordinate>> getMatches(Scanner scanner, Scanner matchScanner) {
        List<Tuple<Coordinate, Coordinate>> coordinateList = new ArrayList<>();
        scanner.getCoordinates().forEach(coordinate -> {
                    matchScanner.getCoordinates().forEach(coordinate2 -> {
                        List<Long> collect = coordinate.getRelativeDistances().stream()
                                .filter(distance -> coordinate2.relativeDistances.contains(distance))
                                .collect(toList());
                        if (collect.size() > 10) {
                            coordinateList.add(new Tuple<>(coordinate, coordinate2));
                        }
                    });
                }
        );
        return coordinateList;
    }

    private Scanner determine(Scanner scanner, Scanner matchScanner) {
        Set<Scanner> rotatedScanners = matchScanner.rotate();

        List<Scanner> matchedScanner = rotatedScanners.stream()
                .filter(rotated -> isMatch(scanner, rotated) != null)
                .collect(toList());
        assert matchedScanner.size() == 1;

        Scanner matched = matchedScanner.get(0);
        Coordinate matchedCoordinate = isMatch(scanner, matched);

        return matched.add(matchedCoordinate);
    }

    private Coordinate isMatch(Scanner scanner, Scanner matchScanner) {
        List<Tuple<Coordinate, Coordinate>> matches = getMatches(scanner, matchScanner);
        return isMatching(matches);
    }

    private Coordinate isMatching(List<Tuple<Coordinate, Coordinate>> list) {
        Coordinate matchScannerPosition = null;
        for (Tuple<Coordinate, Coordinate> tuple : list) {
            Coordinate temp = tuple.y.determineNewPosition(tuple.x);
            if (matchScannerPosition == null) {
                matchScannerPosition = temp;
            } else if (!matchScannerPosition.equals(temp)) {
                return null;
            }
        }
        return matchScannerPosition;
    }

}
