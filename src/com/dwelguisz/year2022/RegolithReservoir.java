package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RegolithReservoir extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day14/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Set<Pair<Integer,Integer>> cave = createCave(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = simulateSand(new HashSet<>(cave), false);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = simulateSand(cave, true);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public Set<Pair<Integer,Integer>> createCave(List<String> lines) {
        Set<Pair<Integer,Integer>> cave = new HashSet<>();
        for (String line : lines) {
            List<Pair<Integer, Integer>> pl = new ArrayList<>();
            String pairs[] = line.split(" -> ");
            for (int i = 0; i < pairs.length; i++) {
                List<Integer> vals = Arrays.stream(pairs[i].split(",")).map(Integer::parseInt).collect(Collectors.toList());
                pl.add(Pair.of(vals.get(0),vals.get(1)));
                if (i == 0) {
                    continue;
                }
                Pair<Integer, Integer> pp = pl.get(i-1);
                Pair<Integer, Integer> cp = pl.get(i);
                drawLine(cave, pp.getLeft(), pp.getRight(), cp.getLeft(), cp.getRight());
            }
        }
        return cave;
    }

    public void drawLine(Set<Pair<Integer, Integer>> cave, Integer py, Integer px, Integer cy, Integer cx) {
        Integer minY = Integer.min(py,cy);
        Integer minX = Integer.min(px,cx);
        Integer maxY = Integer.max(py,cy);
        Integer maxX = Integer.max(px,cx);
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                cave.add(Pair.of(x,y));
            }
        }
    }

    public Integer simulateSand(Set<Pair<Integer,Integer>> cave, Boolean part2) {
        Integer maxY = cave.stream().mapToInt(e -> e.getLeft()).max().getAsInt();
        if (part2) {
            drawLine(cave, 0, maxY+2, 999, maxY+2);
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
            Boolean spaceFree[] = new Boolean[] {
                    !cave.contains(Pair.of(y+1,x)),
                    !cave.contains(Pair.of(y+1,x-1)),
                    !cave.contains(Pair.of(y+1,x+1))};
            if (Arrays.stream(spaceFree).anyMatch(b -> b)) {
                x = spaceFree[0] ? x : spaceFree[1] ? x-1 : x+1;
                y++;
                continue;
            }
            cave.add(Pair.of(y,x));
            return true;
        }
        return false;
    }
}
