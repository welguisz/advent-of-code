package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MovieTheater extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 9, false, 0);

        List<Coord2D> red_tiles = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(red_tiles);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(red_tiles);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    public List<Coord2D> parseLines(List<String> lines) {
        return lines.stream().map(s -> s.split(","))
                .map(sA -> new Coord2D(Integer.parseInt(sA[0]), Integer.parseInt(sA[1])))
                .collect(Collectors.toList());
    }

    public long solutionPart1(List<Coord2D> tiles) {
        List<Pair<Coord2D, Coord2D>> pairs = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i+1; j < tiles.size(); j++) {
                int minX = min(tiles.get(i).x, tiles.get(j).x);
                int minY = min(tiles.get(i).y, tiles.get(j).y);
                int maxX = max(tiles.get(i).x, tiles.get(j).x);
                int maxY = max(tiles.get(i).y, tiles.get(j).y);

                pairs.add(Pair.of(new Coord2D(minX, minY), new Coord2D(maxX, maxY)));
            }
        }
        Long max = Long.MIN_VALUE;
        for (Pair<Coord2D, Coord2D> pair : pairs) {
            int minX = pair.getLeft().x;
            int minY = pair.getLeft().y;
            int maxX = pair.getRight().x;
            int maxY = pair.getRight().y;
            long lengthA = (maxX - minX) + 1;
            long lengthB = (maxY - minY) + 1;
            long area = lengthA * lengthB;
            if (area > max) {
                max = area;
            }
        }
        return max;
    }

    public long solutionPart2(List<Coord2D> tiles) {
        List<Pair<Coord2D, Coord2D>> pairs = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i+1; j < tiles.size(); j++) {
                int minX = min(tiles.get(i).x, tiles.get(j).x);
                int minY = min(tiles.get(i).y, tiles.get(j).y);
                int maxX = max(tiles.get(i).x, tiles.get(j).x);
                int maxY = max(tiles.get(i).y, tiles.get(j).y);

                pairs.add(Pair.of(new Coord2D(minX, minY), new Coord2D(maxX, maxY)));
            }
        }
        Long max = Long.MIN_VALUE;
        for (Pair<Coord2D, Coord2D> pair : pairs) {
            int minX = pair.getLeft().x;
            int minY = pair.getLeft().y;
            int maxX = pair.getRight().x;
            int maxY = pair.getRight().y;
            if (rectangleInPolygon(pair.getLeft(), pair.getRight(), tiles)) {
                long lengthA = (maxX - minX) + 1;
                long lengthB = (maxY - minY) + 1;
                long area = lengthA * lengthB;
                if (area > max) {
                    max = area;
                }
            }
        }
        return max;
    }

    boolean isPointOnSegment(Coord2D point, Coord2D start, Coord2D end) {
        long crossProduct = (long) (end.x - start.x) * (point.y - start.y) - (long) (end.y - start.y) * (point.x - start.x);
        if (crossProduct != 0) {
            return false;
        }
        if ((point.x < min(start.x,end.x)) ||(point.x > max(start.x,end.x))) {
            return false;
        }
        if ((point.y < min(start.y,end.y)) ||(point.y > max(start.y,end.y))) {
            return false;
        }
        return true;
    }

    boolean isPointInPolygon(Coord2D point, List<Coord2D> polygon) {
        boolean inside = false;
        for (int i = 0; i < polygon.size(); i++) {
            int nextI = (i + 1) % polygon.size();
            Coord2D start = polygon.get(i);
            Coord2D end = polygon.get(nextI);
            if (isPointOnSegment(point, start, end)) {
                return true;
            }
            boolean crossHorizontalLine = (start.y > point.y) != (end.y > point.y);
            if (crossHorizontalLine) {
                long numerator = (long) (end.x - start.x) * (point.y - start.y);
                long denominator = (long) (end.y - start.y);
                long dx = point.x - start.x;
                if (denominator > 0) {
                    if (dx * denominator < numerator) {
                        inside = !inside;
                    }
                } else if (denominator < 0) {
                    if (dx * denominator > numerator) {
                        inside = !inside;
                    }
                }
            }
        }
        return inside;
    }

    int getOrientation(Coord2D a, Coord2D b, Coord2D c) {
        long v = ((long) (b.x - a.x) * (c.y - a.y)) - ((long) (b.y - a.y) * (c.x - a.x));
        if (v > 0) {
            return 1;
        } else if (v < 0) {
            return -1;
        }
        return 0;
    }


    boolean getSegmentIntersection(Coord2D a1, Coord2D a2, Coord2D b1, Coord2D b2) {
        int orientation1 = getOrientation(a1, a2, b1);
        int orientation2 = getOrientation(a1, a2, b2);
        int orientation3 = getOrientation(b1, b2, a1);
        int orientation4 = getOrientation(b1, b2, a2);
        return (((orientation1 * orientation2) < 0) && ((orientation3 * orientation4) < 0));
    }

    boolean rectangleInPolygon(Coord2D start, Coord2D end, List<Coord2D> points) {
        List<Coord2D> corners = List.of(new Coord2D(start.x, start.y), new Coord2D(start.x, end.y),
                new Coord2D(end.x, start.y), new Coord2D(end.x, end.y));
        for (Coord2D corner : corners) {
            if (!isPointInPolygon(corner, points)) {
                return false;
            }
        }
        int size = points.size();
        List<Pair<Coord2D, Coord2D>> lines = List.of(
                Pair.of(corners.get(0), corners.get(2)),
                Pair.of(corners.get(2), corners.get(3)),
                Pair.of(corners.get(3), corners.get(1)),
                Pair.of(corners.get(1), corners.get(0))
        );
        for(Pair<Coord2D, Coord2D> line : lines) {
            for (int i = 0; i < size; i++) {
                int nextI = (i + 1) % size;
                if (getSegmentIntersection(line.getLeft(), line.getRight(), points.get(i), points.get(nextI))) {
                    return false;
                }
            }
        }
        return true;
    }
}
