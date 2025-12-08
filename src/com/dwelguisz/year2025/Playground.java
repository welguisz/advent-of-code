package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Playground extends AoCDay {
    List<Coord3D> points;

    class UnionFind {
        Integer[] sizes;
        Integer[] parents;
        public UnionFind(int size) {
            sizes = new Integer[size];
            parents = new Integer[size];
            for(int i = 0; i < size; i++) {
                parents[i] = i;
                sizes[i] = 1;
            }
        }

        public Integer find(Integer x) {
            if (x.equals(parents[x]))
                return x;
            int parent = find(parents[x]);
            parents[x] = parent;
            return parent;
        }

        public void union(Integer a, Integer b) {
            Integer f_a = find(a);
            Integer f_b = find(b);
            if (f_a.equals(f_b)) {
                return;
            }
            if (sizes[f_a] <= sizes[f_b]) {
                parents[f_a] = f_b;
                sizes[f_b] += sizes[f_a];
            } else {
                parents[f_b] = f_a;
                sizes[f_a] += sizes[f_b];
            }
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 8, false, 0);

        List<Coord2D> distances = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(distances);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(distances);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    List<Coord2D> parseLines(List<String> lines) {
        points = new ArrayList<>();
        for (String line : lines) {
            String[] splits = line.split(",");
            points.add(new Coord3D(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Integer.parseInt(splits[2])));
        }
        List<Pair<Coord2D, Long>> distances = new ArrayList<>(1000);

        for(int i = 0; i < points.size(); i++) {
            for(int j = i + 1; j < points.size(); j++) {
                distances.add(Pair.of(new Coord2D(i, j), points.get(i).euclideanDistance(points.get(j))));
            }
        }
        distances.sort(Comparator.comparing(Pair::getRight));
        return distances.stream().map(Pair::getLeft).toList();
    }

    Long solutionPart1(List<Coord2D> distances) {
        UnionFind unionFind = new UnionFind(points.size());
        for (int i = 0; i < 1000; i++) {
            Coord2D junctions = distances.get(i);
            unionFind.union(junctions.x, junctions.y);
        }
        Map<Integer, Long> counter = IntStream.range(0, 1000).map(unionFind::find).boxed()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        List<Long> values = counter.values().stream().sorted().collect(Collectors.toList());
        Collections.reverse(values);
        List<Long> subList = values.subList(0,3);
        System.out.println(subList);
        return values.subList(0,3).stream().reduce(1L, (a,b)->a*b);
    }

    long solutionPart2(List<Coord2D> distances) {
        UnionFind unionFind = new UnionFind(points.size());
        int lcv = 0;
        while(true) {
            Coord2D junctions = distances.get(lcv);
            unionFind.union(junctions.x, junctions.y);
            if (unionFind.sizes[unionFind.find(junctions.x)] == points.size()) {
                Coord3D point1 = points.get(junctions.x);
                Coord3D point2 = points.get(junctions.y);
                return point1.x * point2.x;
            }
            lcv++;
        }
    }

}
