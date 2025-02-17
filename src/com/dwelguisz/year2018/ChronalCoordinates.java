package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChronalCoordinates extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,6,false,0);
        List<Coord2D> points = createPoints(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(points);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(points);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<Coord2D> createPoints(List<String> lines) {
        List<Coord2D> points = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?<xcoord>\\d+),\\s+(?<ycoord>\\d+)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                points.add(new Coord2D(Integer.parseInt(matcher.group("xcoord")), Integer.parseInt(matcher.group("ycoord"))));
            }
        }
        return points;
    }

    public Integer solutionPart1(List<Coord2D> points) {
        Integer minX = points.stream().mapToInt(p -> p.x).min().getAsInt();
        Integer minY = points.stream().mapToInt(p -> p.y).min().getAsInt();
        Integer maxX = points.stream().mapToInt(p -> p.x).max().getAsInt();
        Integer maxY = points.stream().mapToInt(p -> p.y).max().getAsInt();
        Map<Coord2D, Integer> count = new HashMap<>();
        for (int x = minX; x < maxX;x++) {
            for (int y = minY; y < maxY;y++) {
                Map<Integer, List<Coord2D>> minPoints = new HashMap<>();
                Coord2D tmpPoint = new Coord2D(x,y);
                Integer minKey = Integer.MAX_VALUE;
                for (Coord2D p : points) {
                    Integer md = p.manhattanDistance(tmpPoint);
                    minKey = Integer.min(minKey, md);
                    minPoints.computeIfAbsent(md, k -> new ArrayList<>()).add(p);
                }
                if (minPoints.get(minKey).size() == 1) {
                    Coord2D minPoint = minPoints.get(minKey).get(0);
                    Integer cnt = count.getOrDefault(minPoint,0);
                    cnt += 1;
                    count.put(minPoint,cnt);
                }
            }
        }
        return count.entrySet().stream().mapToInt(e -> e.getValue()).max().getAsInt();
    }

    public Integer solutionPart2(List<Coord2D> points) {
        Integer minX = points.stream().mapToInt(p -> p.x).min().getAsInt();
        Integer minY = points.stream().mapToInt(p -> p.y).min().getAsInt();
        Integer maxX = points.stream().mapToInt(p -> p.x).max().getAsInt();
        Integer maxY = points.stream().mapToInt(p -> p.y).max().getAsInt();
        Integer totalCount = 0;
        for (int x = minX-1; x < maxX+1;x++) {
            for (int y = minY-1; y < maxY+1;y++) {
                Integer distance = 0;
                Integer minKey = Integer.MAX_VALUE;
                Coord2D tmpPoint = new Coord2D(x,y);
                for (Coord2D p : points) {
                    distance += p.manhattanDistance(tmpPoint);
                }
                if (distance < 10000) {
                    totalCount++;
                }
            }
        }
        return totalCount;
    }


}
