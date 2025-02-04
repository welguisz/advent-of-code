package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

public class MonitoringStation extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,10,false,0);
        List<Coord2D> asteriods = parseFile(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(asteriods);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(asteriods);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Coord2D> parseFile(List<String> lines) {
        String map[][] = convertToGrid(lines);
        List<Coord2D> asteriods = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].equals("#")) {
                    asteriods.add(new Coord2D(j,i));
                }
            }
        }
        return asteriods;
    }

    Integer solutionPart1(List<Coord2D> asteriods) {
        int maxAsteriodsSeen = Integer.MIN_VALUE;
        for (Coord2D asteriod : asteriods) {
            int asteriodsSeen = asteriods.stream().filter(a -> !a.equals(asteriod)).map(a -> a.slope(asteriod)).collect(Collectors.toSet()).size();
            maxAsteriodsSeen = Integer.max(asteriodsSeen, maxAsteriodsSeen);
        }
        return maxAsteriodsSeen;
    }

    Integer solutionPart2(List<Coord2D> asteriods) {
        Integer maxAsteriodsSeen = Integer.MIN_VALUE;
        Coord2D bestPoint = new Coord2D(0,0);
        for (Coord2D asteriod : asteriods) {
            int asteriodsSeen = asteriods.stream().filter(a -> !a.equals(asteriod)).map(a -> a.slope(asteriod)).collect(Collectors.toSet()).size();
            if (asteriodsSeen > maxAsteriodsSeen) {
                bestPoint = asteriod;
                maxAsteriodsSeen = asteriodsSeen;
            }
        }
        final Coord2D laserAsteriod = bestPoint;
        Map<Double, LinkedList<Coord2D>> pointsPerAngle = asteriods.stream()
                .filter(a -> !a.equals(laserAsteriod))
                .sorted(Comparator.comparingInt(a -> a.manhattanDistance(laserAsteriod)))
                .collect(groupingBy(a -> laserAsteriod.getAngle(a), toCollection(LinkedList::new)));
        Double[] angles = pointsPerAngle.keySet().stream().sorted().toArray(Double[]::new);
        LinkedList<Coord2D> removedPoints = new LinkedList<>();
        int i = 0;
        while (removedPoints.size() < 200) {
            LinkedList<Coord2D> points = pointsPerAngle.get(angles[i++ % angles.length]);
            ofNullable(points.poll()).ifPresent(removedPoints::add);
        }
        Coord2D point200Destroyed = removedPoints.getLast();
        return point200Destroyed.x * 100 + point200Destroyed.y;
    }
}
