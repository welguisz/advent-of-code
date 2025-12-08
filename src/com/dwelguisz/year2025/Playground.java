package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;
import com.dwelguisz.utilities.UnionFind;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Playground extends AoCDay {
    List<Coord3D> points;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 8, false, 0);

        List<Pair<Coord3D, Coord3D>> distances = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(distances);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(distances);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    List<Pair<Coord3D, Coord3D>> parseLines(List<String> lines) {
        points = new ArrayList<>();
        for (String line : lines) {
            String[] splits = line.split(",");
            points.add(new Coord3D(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Integer.parseInt(splits[2])));
        }
        List<Pair<Pair<Coord3D, Coord3D>, Long>> distances = new ArrayList<>(1000);

        for(int i = 0; i < points.size(); i++) {
            for(int j = i + 1; j < points.size(); j++) {
                distances.add(Pair.of(Pair.of(points.get(i), points.get(j)), points.get(i).euclideanDistance(points.get(j))));
            }
        }
        distances.sort(Comparator.comparing(Pair::getRight));
        return distances.stream().map(Pair::getLeft).toList();
    }

    Long solutionPart1(List<Pair<Coord3D, Coord3D>> distances) {
        Coord3D[] elements = new Coord3D[points.size()];
        for (int i = 0; i < points.size(); i++) {
            elements[i] = points.get(i);
        }
        UnionFind<Coord3D> unionFind = new UnionFind(points.size(), elements);
        for (int i = 0; i < 1000; i++) {
            Pair<Coord3D, Coord3D> junctions = distances.get(i);
            unionFind.union(junctions.getLeft(), junctions.getRight());
        }
        Map<Coord3D, Long> counter = Arrays.stream(elements).map(unionFind::find)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        List<Long> values = counter.values().stream().sorted().collect(Collectors.toList());
        Collections.reverse(values);
        List<Long> subList = values.subList(0,3);
        System.out.println(subList);
        return values.subList(0,3).stream().reduce(1L, (a,b)->a*b);
    }

    long solutionPart2(List<Pair<Coord3D, Coord3D>> distances) {
        Coord3D[] elements = new Coord3D[points.size()];
        for (int i = 0; i < points.size(); i++) {
            elements[i] = points.get(i);
        }
        UnionFind<Coord3D> unionFind = new UnionFind(points.size(), elements);
        int lcv = 0;
        while(true) {
            Pair<Coord3D, Coord3D> junctions = distances.get(lcv);
            unionFind.union(junctions.getLeft(), junctions.getRight());
            if (unionFind.getElementSize(unionFind.find(junctions.getLeft())) == points.size()) {
                return junctions.getLeft().x * junctions.getRight().x;
            }
            lcv++;
        }
    }

}
