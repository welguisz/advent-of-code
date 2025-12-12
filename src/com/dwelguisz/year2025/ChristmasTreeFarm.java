package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChristmasTreeFarm extends AoCDay {
    List<Long> density;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 12, false, 0);
        List<Pair<Coord2D, List<Integer>>> values = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    public List<Pair<Coord2D, List<Integer>>> parseLines(List<String> lines) {
        List<Pair<Coord2D, List<Integer>>> areas = new ArrayList<>();
        List<String> splits = Arrays.stream(lines.stream()
                .collect(Collectors.joining(","))
                .split(",,"))
                .collect(Collectors.toList());
        density = splits.subList(0, splits.size() - 1).stream()
                .map(s -> Arrays.stream(s.split("")).filter(s1 -> s1.equals("#")).count())
                .collect(Collectors.toList());
        List<String> parse = Arrays.stream(splits.get(splits.size()-1).split(",")).toList();
        for(String s : parse) {
            String[] temp = s.split(": ");
            String[] lengths = temp[0].split("x");
            List<Integer> quantities = Arrays.stream(temp[1].split(" ")).map(Integer::parseInt).collect(Collectors.toList());
            areas.add(Pair.of(new Coord2D(Integer.parseInt(lengths[0]), Integer.parseInt(lengths[1])), quantities));
        }
        return areas;
    }

    public long solutionPart1(List<Pair<Coord2D, List<Integer>>> values) {
        long results = 0l;
        for (Pair<Coord2D, List<Integer>> pair : values) {
            int min_space = 0;
            for (int i = 0; i < pair.getRight().size(); i++) {
                min_space += pair.getRight().get(i) * density.get(i);
            }
            int total_area = pair.getLeft().x * pair.getLeft().y;
            if (min_space > total_area) {
                results += 0;
            } else {
                int most_presents = Math.floorDiv(pair.getLeft().x,  3) * Math.floorDiv(pair.getLeft().y, 3);
                int total_presents = pair.getRight().stream().reduce(0, Integer::sum);
                results += (total_presents <= most_presents) ? 1 : 0;
            }
        }
        return results;
    }
    public long solutionPart2() {
        return 0l;
    }
}
