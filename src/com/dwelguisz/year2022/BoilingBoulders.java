package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3D;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoilingBoulders extends AoCDay {

    public static class Boulder extends Coord3D {
        public Integer sidesExposed;

        public Boulder(int x, int y, int z) {
            super(x,y,z);
            this.sidesExposed = 6;
        }

        public void touchingOtherBoulder(Boulder other) {
            sidesExposed -= (manhattanDistance(other) == 1) ? 1 : 0;
            other.sidesExposed -= (manhattanDistance(other) == 1) ? 1 : 0;
        }

        List<Boulder> getNeighbors() {
            List<Integer> deltas = List.of(-1,1);
            List<Boulder> neighbors = new ArrayList<>();
            deltas.stream().forEach(d -> neighbors.addAll(List.of(new Boulder(x+d,y,z), new Boulder(x,y+d,z), new Boulder(x,y,z+d))));
            return neighbors;
        }
    }
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,18,false,0);
        List<Boulder> parsedClass = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(parsedClass);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(parsedClass);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<Boulder> parseLines(List<String> lines) {
        List<Boulder> values = new ArrayList<>();
        for (String l : lines) {
            List<Integer> vals = Arrays.stream(l.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            values.add(new Boulder(vals.get(0),vals.get(1),vals.get(2)));
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
                sum+= (!outsideAir.contains(n) || values.contains(n)) ? 0 : 1;
            }
        }
        return sum;
    }
}
