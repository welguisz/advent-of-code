package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class ReserviorResearch extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day17/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Set<Coord2D> clayLocation = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer maxY = clayLocation.stream().mapToInt(p -> p.x).max().getAsInt();
        Set<Coord2D> stoppedWater = new HashSet<>();
        Set<Coord2D> flowingWater = new HashSet<>();
        addWater(new Coord2D(0,500), new Coord2D(1,0),clayLocation,stoppedWater, flowingWater, maxY);
        final Integer minY = clayLocation.stream().mapToInt(p -> p.x).min().getAsInt();
        Integer part1 = solutionPart1(stoppedWater, flowingWater, minY);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(stoppedWater);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public Set<Coord2D> parseLines(List<String> lines) {
        Set<Coord2D> clayLocation = new HashSet<>();
        for (String l : lines) {
            String pos[] = l.split(", ");
            boolean isFirstX = pos[0].startsWith("x");
            String firstPart[] = pos[0].split("=");
            String secondPart[] = pos[1].split("=");
            Integer constant = Integer.parseInt(firstPart[1]);
            String rangeStr[] = secondPart[1].split("\\.\\.");
            Integer rangeStart = Integer.parseInt(rangeStr[0]);
            Integer rangeEnd = Integer.parseInt(rangeStr[1]);
            for (int j = rangeStart; j <= rangeEnd; j++) {
                if (isFirstX) {
                    clayLocation.add(new Coord2D(j,constant));
                } else {
                    clayLocation.add(new Coord2D(constant,j));
                }
            }
        }
        return clayLocation;
    }

    public Integer solutionPart1(Set<Coord2D> stoppedWater, Set<Coord2D> flowingWater, Integer minY) {
        Set<Coord2D> total = new HashSet<>();
        total.addAll(stoppedWater);
        total.addAll(flowingWater);
        total = total.stream().filter(t -> t.x >= minY).collect(Collectors.toSet());
        return total.size();
    }

    public Integer solutionPart2(Set<Coord2D> stoppedWater) {
        return stoppedWater.size();
    }

    public boolean addWater(Coord2D point, Coord2D direction, Set<Coord2D> clayLocation, Set<Coord2D> stoppedWater, Set<Coord2D> flowingWater, Integer maxY) {
        flowingWater.add(point);
        Coord2D below = new Coord2D(point.x+1,point.y);
        if (!clayLocation.contains(below) && !flowingWater.contains(below) && (below.x >= 1) && (below.x <= maxY)) {
            addWater(below,new Coord2D(1,0),clayLocation,stoppedWater,flowingWater,maxY);
        }
        if (!clayLocation.contains(below) && !stoppedWater.contains(below)) {
            return false;
        }
        Coord2D left = new Coord2D(point.x,point.y-1);
        Coord2D right = new Coord2D(point.x, point.y+1);

        boolean leftFilled = clayLocation.contains(left) || (!flowingWater.contains(left) && addWater(left,new Coord2D(0,-1),clayLocation,stoppedWater,flowingWater,maxY));
        boolean rightFilled = clayLocation.contains(right) || (!flowingWater.contains(right) && addWater(right, new Coord2D(0,1),clayLocation,stoppedWater,flowingWater,maxY));

        if (direction.equals(new Coord2D(1,0)) && leftFilled && rightFilled) {
            stoppedWater.add(point);
            while (flowingWater.contains(left)) {
                stoppedWater.add(left);
                left = new Coord2D(left.x, left.y - 1);
            }

            while (flowingWater.contains(right)) {
                stoppedWater.add(right);
                right = new Coord2D(right.x, right.y + 1);
            }
        }

        return (direction.equals(new Coord2D(0,-1)) && (leftFilled || clayLocation.contains(left))) ||
                (direction.equals(new Coord2D(0,1)) && (rightFilled || clayLocation.contains(right)));
    }


}
