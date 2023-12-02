package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3D;

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
            String[] split1 = line.split(": ");
            id = Integer.parseInt(split1[0].split(" ")[1]);
            results = new ArrayList<>();
            String[] split2 = split1[1].split("; ");
            for(String sp : split2) {
                String[] tmp = sp.split(", ");
                Map<String, Integer> map = new HashMap<>();
                for (String t : tmp) {
                    String info[] = t.split(" ");
                    map.put(info[1], Integer.parseInt(info[0]));
                }
                results.add(new Coord3D(map.getOrDefault("blue",0),map.getOrDefault("red",0),map.getOrDefault("green",0)));
            }
        }

        public boolean isPossible(int blueNum, int redNum, int greenNum) {
            boolean bluePossible = results.stream().allMatch(c -> c.x <= blueNum);
            boolean redPossible = results.stream().allMatch(c -> c.y <= redNum);
            boolean greenPossible = results.stream().allMatch(c -> c.z <= greenNum);
            return bluePossible && redPossible && greenPossible;
        }

        public int gameId(int blueNum, int redNum, int greenNum) {
            return isPossible(blueNum, redNum, greenNum) ? id : 0;
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
        return games.stream().mapToInt(g -> g.gameId(14,12,13)).sum();
    }

    public Long solutionPart2(List<Games> games) {
        return games.stream().mapToLong(g -> g.part2()).sum();
    }
}
