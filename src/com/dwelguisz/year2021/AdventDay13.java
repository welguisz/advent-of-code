package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day13.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay13 {

    static List<String> processInstructions = new ArrayList<>();

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent_of_code/src/resources/year2021/day13/input.txt");
        Set<Point> points = processLines(lines);
        Integer part1 = part1(points, processInstructions);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        part2(points, processInstructions);
    }

    public static Set<Point> processLines(List<String> strings) {
        Set<Point> points = new HashSet<>();
        for (String str : strings) {
            if (str.length() == 0) {
                continue;
            }
            if (str.length() > 10) {
                processInstructions.add(str);
            }
            else {
                List<Integer> pointsT = Arrays.stream(str.split(",")).map(s -> parseInt(s)).collect(Collectors.toList());
                points.add(new Point (pointsT.get(0), pointsT.get(1)));
            }
        }
        return points;
    }

    public static void printGrid(Set<Point> points) {
        Integer minX = Integer.MAX_VALUE;
        Integer minY = Integer.MAX_VALUE;
        Integer maxX= Integer.MIN_VALUE;
        Integer maxY = Integer.MIN_VALUE;
        for (Point point : points) {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }
        for (int y = minY; y <= maxY; y++) {
            StringBuffer sb = new StringBuffer();
            for(int x = minX; x <= maxX; x++) {
                final Point point = new Point(x, y);
                if (points.contains(point)) {
                    sb.append("#");
                } else {
                    sb.append(" ");
                }
            }
            System.out.println(sb.toString());
        }
    }

    public static int part1(Set<Point> points, List<String> lines) {
        points = foldOnce(points, lines.get(0));
        return points.size();
    }

    public static void part2(Set<Point> points, List<String> lines) {
        for (String line : lines) {
            points = foldOnce(points, line);
        }
        printGrid(points);
    }

    public static Set<Point> foldOnce(Set<Point> points, String line) {
        String[] foldLine = line.split(" ");
        String[] foldInstr = foldLine[2].split("=");
        String axis = foldInstr[0];
        Integer foldLocation = parseInt(foldInstr[1]);
        final Set<Point> previousPoints = new HashSet<>(points);
        for (final Point point : previousPoints) {
            Integer pointCheck = (axis.equals("x")) ? point.x : point.y;
            if (pointCheck > foldLocation) {
                points.remove(point);
                pointCheck = foldLocation - (pointCheck - foldLocation);
                Integer pointX = (axis.equals("x")) ? pointCheck : point.x;
                Integer pointY = (axis.equals("y")) ? pointCheck : point.y;
                points.add(new Point(pointX, pointY));
            }
        }
        return points;
    }
}
