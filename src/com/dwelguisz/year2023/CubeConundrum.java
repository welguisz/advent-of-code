package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3D;
import com.google.common.collect.Lists;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CubeConundrum extends AoCDay {

    public static class Games {
        public Integer id;
        public List<Coord3D> results;

        public Games(String line) {
            String[] spaceSplits = line.split(" ");
            String idString = spaceSplits[1].substring(0,spaceSplits[1].length()-1);
            id = Integer.parseInt(idString);
            results = new ArrayList<>();
            Map<String, Integer> map = new HashMap<>();
            List<String> specialChars = Lists.newArrayList(",", ";");
            for (int i = 2; i < spaceSplits.length; i+=2) {
                final Integer lcv = i;
                Integer lookback = specialChars.stream().anyMatch(c -> spaceSplits[lcv+1].contains(c)) ? 1 : 0;
                String color = spaceSplits[i+1].substring(0,spaceSplits[i+1].length()-lookback);
                Integer num = Integer.parseInt(spaceSplits[i]);
                map.put(color,num);
                if (spaceSplits[i+1].contains(";")) {
                    results.add(new Coord3D(map.getOrDefault("blue",0),map.getOrDefault("red",0),map.getOrDefault("green",0)));
                    map = new HashMap<>();
                }
            }
            results.add(new Coord3D(map.getOrDefault("blue",0),map.getOrDefault("red",0),map.getOrDefault("green",0)));
        }

        public boolean isPossible(Coord3D maxima) {
            boolean bluePossible = results.stream().allMatch(c -> c.x <= maxima.x);
            boolean redPossible = results.stream().allMatch(c -> c.y <= maxima.y);
            boolean greenPossible = results.stream().allMatch(c -> c.z <= maxima.z);
            return bluePossible && redPossible && greenPossible;
        }

        public int gameId(Coord3D maxima) {
            return isPossible(maxima) ? id : 0;
        }

        public int part2() {
            Integer blueNeeded = results.stream().mapToInt(c -> c.x).max().getAsInt();
            Integer redNeeded = results.stream().mapToInt(c -> c.y).max().getAsInt();
            Integer greenNeeded = results.stream().mapToInt(c -> c.z).max().getAsInt();
            return blueNeeded*redNeeded*greenNeeded;
        }
    }
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 2, false, 0);
        List<Games> games = lines.stream().map(l -> new Games(l)).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(games);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(games);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(List<Games> games) {
        return games.stream().mapToInt(g -> g.gameId(new Coord3D(14,12,13))).sum();
    }

    public Long solutionPart2(List<Games> games) {
        return games.stream().mapToLong(g -> g.part2()).sum();
    }
}
