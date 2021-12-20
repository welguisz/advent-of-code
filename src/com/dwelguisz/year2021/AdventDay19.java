package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day19.Beacon;
import com.dwelguisz.year2021.helper.day19.Scanner;
import com.dwelguisz.year2021.helper.day19.ScannerMatch;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Scanner> resolvedScanners = new ArrayList<>();
        List<Scanner> toHandleScanners = new ArrayList<>(scanners);
        Scanner first = scanners.get(0);
        resolvedScanners.add(first);
        toHandleScanners.remove(first);
        Scanner known = scanners.get(0);
        scanners.remove(known);
        List<Beacon> offsets = new ArrayList<>();
        offsets.add(new Beacon(0,0,0));
        while (!scanners.isEmpty()) {
            System.out.println(String.format("%d scanners left", scanners.size()));
            ScannerMatch matched = findScannerMatches(known, scanners);
            if (matched != null) {
                known.addAllBeacons(matched.getBeacons());
                scanners.remove(matched.getRemoveScanner());
            }
            else {
                return List.of(-15,-16);
            }
        }
        Integer manhattan = Integer.MIN_VALUE;
        for (Beacon beacon1 : offsets) {
            for (Beacon beacon2 : offsets) {
                Beacon tempBeacon = beacon1.diffBeacon(beacon2);
                Beacon absBeacon = new Beacon(Math.abs(tempBeacon.getX()), Math.abs(tempBeacon.getY()), Math.abs(tempBeacon.getZ()));
                Integer sumBeacon = absBeacon.getX() + absBeacon.getY() + absBeacon.getZ();
                manhattan = Integer.max(manhattan, sumBeacon);
            }
        }
        return List.of(known.getBeacons().size(), manhattan);
    }

    public static ScannerMatch findScannerMatches(Scanner knownScanner, List<Scanner> unknownScanners) {
        for (Scanner scanner : unknownScanners) {
            for (Scanner altScanner : scanner.getAlternateScanners()) {
                List<Beacon> testBeacons = new ArrayList<>(altScanner.getBeacons());
                for (Beacon currentBeacon : knownScanner.getBeacons()) {
                    if (currentBeacon.equals(new Beacon(-618,-824,-621))) {
                        System.out.println("Find!!");
                    }
                    int idx2 = 0;
                    for (Beacon testBeacon : altScanner.getBeacons()) {
                        if (idx2 + 11 >= testBeacons.size()) {
                            break; // not enough
                        }
                        Beacon possibleScannerLoc = currentBeacon.diffBeacon(testBeacon);
                        if (possibleScannerLoc.equals(new Beacon(68,-1246,-43))) {
                            System.out.println("Find possible Scanner Location");
                        }
                        int matches = 1;
                        for (int idx3 = idx2+1; idx3 < testBeacons.size(); idx3++) {
                            Beacon tempBeacon = testBeacons.get(idx3);
                            Beacon newBeaconLoc = possibleScannerLoc.diffBeacon(tempBeacon);
                            if (knownScanner.getBeacons().contains(newBeaconLoc)) {
                                matches += 1;
                            }
                        }
                        if (matches >= 12) {
                            return new ScannerMatch(possibleScannerLoc, altScanner, altScanner.getBeacons());
                        }
                        idx2++;
                    }
                }
            }
        }
        return null;
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
                currentScanner.addNewBeacon(new Beacon(coordinates.get(0), coordinates.get(1), coordinates.get(2)));
            }
        }
        currentScanner.generateAlternateOrientation();
        scanners.add(currentScanner);
    }
}
