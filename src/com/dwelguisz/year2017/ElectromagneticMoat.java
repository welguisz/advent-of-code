package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElectromagneticMoat extends AoCDay {

    public static class Bridge {
        List<Coord2D> magnets;
        Integer nextNumber;
        boolean done;
        public Bridge(List<Coord2D> magnets, Integer nextNumber) {
            this.magnets = magnets;
            this.nextNumber = nextNumber;
            this.done = false;
        }

        public Stream<Bridge> addNextPart(List<Coord2D> pairs) {
            if (done) {
                return Stream.of(this);
            }
            List<Bridge> nextParts = new ArrayList<>();
            for (Coord2D p : pairs) {
                if (magnets.contains(p)) {
                    continue;
                }
                if (p.x == nextNumber || p.y == nextNumber) {
                    List<Coord2D> mags = new ArrayList<>(magnets);
                    mags.add(p);
                    nextParts.add(new Bridge(mags, p.x == nextNumber ? p.y : p.x));
                }
            }
            this.done = true;
            nextParts.add(this);
            return nextParts.stream();
        }

        Integer getStrength() {
            Integer sum = 0;
            for (Coord2D m : magnets) {
                sum += m.x + m.y;
            }
            return sum;
        }

    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2017/day24/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<Bridge> bridges = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(bridges);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(bridges);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    List<Bridge> parseLines(List<String> lines) {
        List<Coord2D> pairs = new ArrayList<>();
        for (String l : lines) {
            String[] p = l.split("/");
            pairs.add(new Coord2D(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
        }
        List<Bridge> bridgesInProgress = new ArrayList<>();
        for (Coord2D p : pairs) {
            if (p.x == 0 || p.y == 0) {
                List<Coord2D> parts = new ArrayList<>();
                parts.add(p);
                bridgesInProgress.add(new Bridge(parts, p.x == 0 ? p.y : p.x));
            }
        }
        while (bridgesInProgress.stream().anyMatch(b -> !b.done)) {
            bridgesInProgress = bridgesInProgress.stream().flatMap(b -> b.addNextPart(pairs)).collect(Collectors.toList());
        }
        return bridgesInProgress;
    }


    Integer solutionPart1(List<Bridge> bridges) {
        return bridges.stream().mapToInt(b -> b.getStrength()).max().getAsInt();
    }

    Integer solutionPart2(List<Bridge> bridges) {
        Integer maxBridgeLength = bridges.stream().mapToInt(b -> b.magnets.size()).max().getAsInt();
        return bridges.stream().filter(b -> b.magnets.size() == maxBridgeLength).mapToInt(b -> b.getStrength()).max().getAsInt();
    }



}
