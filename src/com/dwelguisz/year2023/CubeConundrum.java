package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3D;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CubeConundrum extends AoCDay {

    private static Coord3D MAXIMA = new Coord3D(14,12,13);
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 2, false, 0);
        Map<Integer,Coord3D> games = lines.stream().map(l -> parseLine(l)).collect(Collectors.toMap(p -> p.getLeft(), p->p.getRight()));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(games);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(games);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Pair<Integer, Coord3D> parseLine(String line) {
        String[] spaceSplits = line.split(" ");
        String idString = spaceSplits[1].substring(0,spaceSplits[1].length()-1);
        Integer id = Integer.parseInt(idString);
        Map<String, Integer> map = new HashMap<>();
        map.put("blue",0);
        map.put("green",0);
        map.put("red",0);
        List<String> specialChars = Lists.newArrayList(",", ";");
        for (int i = 2; i < spaceSplits.length; i+=2) {
            final Integer lcv = i;
            Integer lookback = specialChars.stream().anyMatch(c -> spaceSplits[lcv+1].contains(c)) ? 1 : 0;
            String color = spaceSplits[i+1].substring(0,spaceSplits[i+1].length()-lookback);
            Integer num = Integer.parseInt(spaceSplits[i]);
            map.compute(color,(k,v)-> Integer.max(v,num));
        }
        return Pair.of(id, new Coord3D(map.get("blue"), map.get("red"), map.get("green")));
    }

    public Integer solutionPart1(Map<Integer,Coord3D> games) {
        return games.entrySet().stream()
                .filter(e -> (e.getValue().x <= MAXIMA.x) && (e.getValue().y <= MAXIMA.y) && (e.getValue().z <= MAXIMA.z))
                .mapToInt(e -> e.getKey())
                .sum();
    }

    public Long solutionPart2(Map<Integer, Coord3D> games) {
        return games.values().stream().mapToLong(v -> v.x * v.y * v.z).sum();
    }
}
