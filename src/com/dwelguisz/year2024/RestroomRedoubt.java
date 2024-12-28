package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.ChineseRemainderTheorem;
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
        int x = (((robot.getLeft().x + robot.getRight().x*turn) % gridX) + gridX) % gridX;
        int y = (((robot.getLeft().y + robot.getRight().y*turn) % gridY) + gridY) % gridY;
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

    List<Coord2D> NEIGHBORS = List.of(new Coord2D(-1, -1), new Coord2D(0, -1), new Coord2D(1, -1),
            new Coord2D(-1, 0), new Coord2D(1, 0),
            new Coord2D(-1, 1), new Coord2D(0, 1), new Coord2D(1, 1));

    List<Coord2D> X_AXIS_ORDER = List.of(new Coord2D(-1,0), new Coord2D(1,0));
    List<Coord2D> Y_AXIS_ORDER = List.of(new Coord2D(0,-1), new Coord2D(0,1));

    long countNeighbors(Set<Coord2D> positions, Coord2D test, int order) {
        List<Coord2D> neighbors;
        if (order == 0) {
            neighbors = NEIGHBORS;
        } else if (order == 1) {
            neighbors = X_AXIS_ORDER;
        } else {
            neighbors = Y_AXIS_ORDER;
        }
        return neighbors.stream()
                .map(n -> n.add(test))
                .filter(positions::contains)
                .count();
    }

    long calculateOrder(List<Coord2D> positions, final int order) {
        Set<Coord2D> pos = new HashSet<>(positions);
        Map<Long, Long> neighborCount = pos.stream().map(n -> countNeighbors(pos, n, order))
                .collect(Collectors.groupingBy(m -> m, Collectors.counting()));
        return neighborCount.entrySet().stream()
                .map(e -> e.getKey() * e.getValue())
                .reduce(0L, Long::sum);
    }

    long solutionPart2(List<Pair<Coord2D, Coord2D>> robots, int gridX, int gridY) {
        long orderMax = Long.MIN_VALUE;
        int timeX = 0;
        int timeY = 0;
        for (int i = 0; i< gridX; i++) {
            final int tf = i;
            List<Coord2D> positions = robots.stream()
                    .map(r -> findPositionAtTime(r, gridX, gridY, tf))
                    .collect(Collectors.toList());
            long order = calculateOrder(positions, 2);
            if (order > orderMax) {
                orderMax = order;
                timeX = i;
            }
        }
        orderMax = Long.MIN_VALUE;
        for (int i = 0; i< gridY; i++) {
            final int tf = i;
            List<Coord2D> positions = robots.stream()
                    .map(r -> findPositionAtTime(r, gridX, gridY, tf))
                    .collect(Collectors.toList());
            long order = calculateOrder(positions, 1);
            if (order > orderMax) {
                orderMax = order;
                timeY = i;
            }
        }
        Long[] divisors = new Long[]{(long) gridX, (long) gridY};
        Long[] remainders = new Long[]{(long) timeX, (long) timeY};
        return ChineseRemainderTheorem.chineseRemainder(divisors, remainders);
    }

    void printGrid(List<Pair<Coord2D, Coord2D>> robots, int gridX, int gridY, int time) {
        List<Coord2D> positions = robots.stream()
                .map(r -> findPositionAtTime(r, gridX, gridY, time))
                .collect(Collectors.toList());
        for (int x = 0; x < gridX; x++) {
            StringBuilder buffer = new StringBuilder();
            for (int y = 0; y < gridY; y++) {
                if (positions.contains(new Coord2D(y, x))) {
                    buffer.append("#");
                } else {
                    buffer.append(".");
                }
            }
            System.out.println(buffer.toString());
        }

    }

}
