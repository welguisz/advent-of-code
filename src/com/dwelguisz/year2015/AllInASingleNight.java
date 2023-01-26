package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class AllInASingleNight extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,9,false,0);
        List<Integer> allDistances = findAllDistances(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(allDistances);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(allDistances);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<Integer> findAllDistances(List<String> lines) {
        Map<Pair<String,String>, Integer> distances = new HashMap<>();
        Set<String> places = new HashSet<>();
        for (String line:lines) {
            String[] points = line.split(" ");
            Integer distance = parseInt(points[4]);
            places.add(points[0]);
            places.add(points[2]);
            distances.put(Pair.of(points[0],points[2]), distance);
            distances.put(Pair.of(points[2],points[0]), distance);
        }
        Collection<List<String>> permutations = Collections2.permutations(places);
        List<Integer> travelledDistance = new ArrayList<>();
        for (List<String> p : permutations) {
            Integer distance = 0;
            for(int i = 0; i < p.size()-1; i++) {
                distance += distances.get(Pair.of(p.get(i),p.get(i+1)));
            }
            travelledDistance.add(distance);
        }
        return travelledDistance;
    }

    public Integer solutionPart1(List<Integer> distances) {
        return distances.stream().mapToInt(d -> d).min().getAsInt();
    }

    public Integer solutionPart2(List<Integer> distances) {
        return distances.stream().mapToInt(d -> d).max().getAsInt();
    }
}
