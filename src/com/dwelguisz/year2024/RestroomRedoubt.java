package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RestroomRedoubt extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 14, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        List<Pair<Coord2D, Coord2D>> robots = parseLines(lines);
        part1Answer = solutionPart1(robots, 101, 103, 100);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(robots, 101, 103);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Pair<Coord2D, Coord2D>> parseLines(List<String> lines) {
        List<Pair<Coord2D, Coord2D>> robots = new ArrayList<>();
        Pattern pattern = Pattern.compile("p=(?<px>-?\\d+),(?<py>-?\\d+)\\s+v=(?<vx>-?\\d+),(?<vy>-?\\d+)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                robots.add(
                        Pair.of(
                                new Coord2D(Integer.parseInt(matcher.group("px")), Integer.parseInt(matcher.group("py"))),
                                new Coord2D(Integer.parseInt(matcher.group("vx")), Integer.parseInt(matcher.group("vy")))
                        ));
            }

        }

        return robots;
    }

    int getQuadrant(Coord2D point, int width, int height) {
        if ((point.x == width / 2) || (point.y == height / 2)) {
            return -1;
        }
        int x1 = point.x < width / 2 ? 0 : 1;
        int y1 = point.y < height / 2 ? 0 : 1;
        return (x1*2)+y1;
    }

    Coord2D findPositionAtTime(Pair<Coord2D, Coord2D> robot, int gridX, int gridY, int turn) {
        int x = robot.getLeft().x + robot.getRight().x*turn;
        int y = robot.getLeft().y + robot.getRight().y*turn;
        //This does (x % gridX). I think Java does something weird with negative numbers.
        //so make sure that the robots are on the grid by teleporting the robot
        while (x < 0) {
            x += gridX;
        }
        while (y < 0) {
            y += gridY;
        }
        while (y >= gridY) {
            y -= gridY;
        }
        while (x >= gridX) {
            x -= gridX;
        }
        return new Coord2D(x, y);
    }

    long solutionPart1(List<Pair<Coord2D, Coord2D>> robots, int gridX, int gridY, int turns) {
        Map<Integer, Long> quads = robots.stream()
                .map(r -> findPositionAtTime(r, gridX, gridY, turns))
                .map(r -> getQuadrant(r, gridX, gridY))
                .filter(q -> q >= 0)
                .collect(Collectors.groupingBy(q -> q, Collectors.counting()));
        return quads.values().stream().reduce(1L, (a, b) -> a * b);
    }

    long countNeighbors(List<Coord2D> positions, int gridX, int gridY) {
        long neighbors = 0;
        Set<Coord2D> neighborSet = new HashSet<>(positions);
        for (int x = 0; x < gridX; x++) {
            for (int y = 0; y < gridY; y++) {
                if (neighborSet.contains(new Coord2D(x, y)) && neighborSet.contains(new Coord2D(x + 1, y))) {
                    neighbors++;
                }
            }
        }
        return neighbors;
    }

    long solutionPart2(List<Pair<Coord2D, Coord2D>> robots, int gridX, int gridY) {
        int time = 0;
        long neighbors = 0;
        while (neighbors < 200) {
            time++;
            final int tf = time;
            List<Coord2D> positions = robots.stream()
                    .map(r -> findPositionAtTime(r, gridX, gridY, tf))
                    .collect(Collectors.toList());
            neighbors = countNeighbors(positions, gridX, gridY);
        }
        return time;
    }

}
