package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChronalCoordinates extends AoCDay {
    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Integer getManhattanDistance(Point that) {
            return Math.abs(that.x - this.x) + Math.abs(that.y - this.y);
        }
    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day06/input.txt");
        List<Point> points = createPoints(lines);
        Integer part1 = solutionPart1(points);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(points);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public List<Point> createPoints(List<String> lines) {
        List<Point> points = new ArrayList<>();
        for (String line : lines) {
            String tmp[] = line.split(", ");
            points.add(new Point(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])));
        }
        return points;
    }

    public Integer solutionPart1(List<Point> points) {
        Integer minX = points.stream().mapToInt(p -> p.x).min().getAsInt();
        Integer minY = points.stream().mapToInt(p -> p.y).min().getAsInt();
        Integer maxX = points.stream().mapToInt(p -> p.x).max().getAsInt();
        Integer maxY = points.stream().mapToInt(p -> p.y).max().getAsInt();
        Map<Point, Integer> count = new HashMap<>();
        for (int x = minX-50; x < maxX+50;x++) {
            for (int y = minY-50; y < maxY+50;y++) {
                Map<Integer, List<Point>> minPoints = new HashMap<>();
                Point tmpPoint = new Point(x,y);
                Integer minKey = Integer.MAX_VALUE;
                for (Point p : points) {
                    Integer md = p.getManhattanDistance(tmpPoint);
                    minKey = Integer.min(minKey, md);
                    List<Point> tmp = minPoints.getOrDefault(md, new ArrayList<>());
                    tmp.add(p);
                    minPoints.put(md, tmp);
                }
                if (minPoints.get(minKey).size() == 1) {
                    Point minPoint = minPoints.get(minKey).get(0);
                    Integer cnt = count.getOrDefault(minPoint,0);
                    cnt += 1;
                    count.put(minPoint,cnt);
                }
            }
        }
        return count.entrySet().stream().mapToInt(e -> e.getValue()).max().getAsInt();
    }

    public Integer solutionPart2(List<Point> points) {
        Integer minX = points.stream().mapToInt(p -> p.x).min().getAsInt();
        Integer minY = points.stream().mapToInt(p -> p.y).min().getAsInt();
        Integer maxX = points.stream().mapToInt(p -> p.x).max().getAsInt();
        Integer maxY = points.stream().mapToInt(p -> p.y).max().getAsInt();
        Integer totalCount = 0;
        for (int x = minX-1; x < maxX+1;x++) {
            for (int y = minY-1; y < maxY+1;y++) {
                Integer distance = 0;
                Integer minKey = Integer.MAX_VALUE;
                Point tmpPoint = new Point(x,y);
                for (Point p : points) {
                    distance += p.getManhattanDistance(tmpPoint);
                }
                if (distance < 10000) {
                    totalCount++;
                }
            }
        }
        return totalCount;
    }


}
