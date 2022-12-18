package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AoC2022Day18 extends AoCDay {

    public static class Boulder {
        final int x;
        final int y;
        final int z;
        private int hashCode;
        public Integer sidesExposed;

        public Boulder(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.hashCode = Objects.hash(x, y, z);
            this.sidesExposed = 6;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass());
            Boulder other = (Boulder) o;
            return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
        public void touchingOtherBoulder(Boulder other) {
            Integer totalDiff = Math.abs(other.x - this.x) + Math.abs(other.y - this.y) + Math.abs(other.z - this.z);
            sidesExposed -= (totalDiff == 1) ? 1 : 0;
            other.sidesExposed -= (totalDiff == 1) ? 1 : 0;
        }

        List<Boulder> getNeighbors() {
            List<Integer> deltas = List.of(-1,1);
            List<Boulder> neighbors = new ArrayList<>();
            deltas.stream().forEach(d -> neighbors.add(new Boulder(x+d,y,z)));
            deltas.stream().forEach(d -> neighbors.add(new Boulder(x,y+d,z)));
            deltas.stream().forEach(d -> neighbors.add(new Boulder(x,y,z+d)));
            return neighbors;
        }
    }
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day18/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<Boulder> parsedClass = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(parsedClass);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(parsedClass);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public List<Boulder> parseLines(List<String> lines) {
        List<Boulder> values = new ArrayList<>();
        for (String l : lines) {
            String vals[] = l.split(",");
            values.add(new Boulder(Integer.parseInt(vals[0]),Integer.parseInt(vals[1]),Integer.parseInt(vals[2])));
        }
        return values;
    }

    public Integer solutionPart1(List<Boulder> values) {
        for (int i = 0; i < values.size();i++) {
            for (int j = i+1; j < values.size();j++) {
                values.get(i).touchingOtherBoulder(values.get(j));
            }
        }
        return values.stream().mapToInt(v -> v.sidesExposed).sum();
    }

    public Integer solutionPart2(List<Boulder> values) {
        Integer minX = values.stream().mapToInt(b -> b.x).min().getAsInt();
        Integer minY = values.stream().mapToInt(b -> b.y).min().getAsInt();
        Integer minZ = values.stream().mapToInt(b -> b.z).min().getAsInt();
        Integer maxX = values.stream().mapToInt(b -> b.x).max().getAsInt();
        Integer maxY = values.stream().mapToInt(b -> b.y).max().getAsInt();
        Integer maxZ = values.stream().mapToInt(b -> b.z).max().getAsInt();

        List<Integer> rangeX = IntStream.range(minX-1,maxX+2).boxed().collect(Collectors.toList());
        List<Integer> rangeY = IntStream.range(minY-1,maxY+2).boxed().collect(Collectors.toList());
        List<Integer> rangeZ = IntStream.range(minZ-1,maxZ+2).boxed().collect(Collectors.toList());

        Set<Boulder> outsideAir = new HashSet<>();
        Boulder start = new Boulder(minX-1,minY-1,minZ-1);
        outsideAir.add(start);
        ArrayDeque<Boulder> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Boulder b = queue.poll();
            if (!(rangeX.contains(b.x) && rangeY.contains(b.y) && rangeZ.contains(b.z))) {
                continue;
            }
            List<Boulder> neighbors = b.getNeighbors();
            for (Boulder n : neighbors) {
                if (values.contains(n) || outsideAir.contains(n)) {
                    continue;
                }
                outsideAir.add(n);
                queue.add(n);
            }
        }
        Integer sum = 0;
        for (Boulder b : values) {
            List<Boulder> neighbors = b.getNeighbors();
            for (Boulder n : neighbors) {
                if (!outsideAir.contains(n)) {
                    continue;
                }
                if (values.contains(n)) {
                    continue;
                }
                sum++;
            }
        }
        return sum;
    }
}
