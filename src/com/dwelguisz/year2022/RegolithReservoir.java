package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegolithReservoir extends AoCDay {
    public void solve() {
        System.out.println("Day 14 ready to go");
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day14/input.txt");
        Set<Pair<Integer,Integer>> cave = createCave(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(cave, false);
        Long part1Time = Instant.now().toEpochMilli();
        cave = createCave(lines);
        Integer part2 = solutionPart1(cave, true);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public Set<Pair<Integer,Integer>> createCave(List<String> lines) {
        Set<Pair<Integer,Integer>> cave = new HashSet<>();
        for (String line : lines) {
            List<Pair<Integer, Integer>> pairLine = new ArrayList<>();
            String pairs[] = line.split(" -> ");
            for (int i = 0; i < pairs.length; i++) {
                String parts[] = pairs[i].split(",");
                Integer x = Integer.parseInt(parts[0]);
                Integer y = Integer.parseInt(parts[1]);
                pairLine.add(Pair.of(x,y));
                if (i == 0) {
                    continue;
                }
                Pair<Integer, Integer> prevPair = pairLine.get(i-1);
                Pair<Integer, Integer> curPair = pairLine.get(i);
                drawLine(cave, prevPair, curPair);
            }
        }
        return cave;
    }

    public void drawLine(Set<Pair<Integer, Integer>> cave, Pair<Integer, Integer> startingPoint, Pair<Integer, Integer> endingPoint) {
        Integer minY = Integer.min(startingPoint.getLeft(), endingPoint.getLeft());
        Integer minX = Integer.min(startingPoint.getRight(), endingPoint.getRight());
        Integer maxY = Integer.max(startingPoint.getLeft(), endingPoint.getLeft());
        Integer maxX = Integer.max(startingPoint.getRight(), endingPoint.getRight());
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                cave.add(Pair.of(x,y));
            }
        }
    }

    public Integer solutionPart1(Set<Pair<Integer,Integer>> cave, Boolean part2) {
        Integer maxY = cave.stream().mapToInt(e -> e.getLeft()).max().getAsInt();
        if (part2) {
            drawLine(cave, Pair.of(0, maxY+2), Pair.of(999, maxY+2));
        }
        Integer sandCount=0;
        while(addSand(cave, maxY)) {
            sandCount++;
        }
        return sandCount;
    }

    public boolean addSand(Set<Pair<Integer,Integer>> cave, Integer maxY) {
        if (cave.contains(Pair.of(0,500))) {
            return false;
        }
        int x = 500;
        int y = 0;
        while (y <= maxY + 3) {
            if (!cave.contains(Pair.of(y+1,x))) {
                y++;
                continue;
            }
            if (!cave.contains(Pair.of(y+1,x-1))) {
                y++;
                x--;
                continue;
            }
            if (!cave.contains(Pair.of(y+1,x+1))) {
                y++;
                x++;
                continue;
            }
            cave.add(Pair.of(y,x));
            return true;
        }
        return false;
    }
}
