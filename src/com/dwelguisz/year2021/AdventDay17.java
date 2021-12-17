package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day13.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class AdventDay17 {

    public static Integer minX;
    public static Integer maxX;
    public static Integer minY;
    public static Integer maxY;

    public static void main(String[] args) {
        String line = "target area: x=57..116, y=-198..-148";
        String[] coordinates = line.split(": ")[1].split(", ");
        String[] xNumbers = coordinates[0].split("=")[1].split("\\.\\.");
        String[] yNumbers = coordinates[1].split("=")[1].split("\\.\\.");
        minX = parseInt(xNumbers[0]);
        maxX = parseInt(xNumbers[1]);
        minY = parseInt(yNumbers[0]);
        maxY = parseInt(yNumbers[1]);
        System.out.println(String.format("Solution Part1: %d",solutionPart1()));
        System.out.println(String.format("Solution Part2: %d",solutionPart2()));
    }

    public static Integer solutionPart1() {
        return sumIntegersFrom0toN(minY);
    }

    public static Integer solutionPart2() {
        return elegantSolutionPart2().size();
    }

    public static Set<Point> elegantSolutionPart2() {
        List<Integer> points = List.of(minX, maxX, minY, maxX);
        Integer maxPoint = points.stream().map(i -> Math.abs(i)).max(Integer::compareTo).get();
        Set<Point> foundPoints = new HashSet<>();
        Map<Integer, Integer> velocityToDistanceMap = new HashMap<>();
        int sum = 0;
        int step = 0;
        while (sum <= (2*maxPoint)) {
            velocityToDistanceMap.put(step, sum);
            step += 1;
            sum += step;
        }
        List<Integer> interestingVelocitiesX = interestingVelocities(velocityToDistanceMap, minX, maxX);
        List<Integer> interestingVelocitiesY = interestingVelocities(velocityToDistanceMap, Math.abs(maxY), Math.abs(minY));
        foundPoints.addAll(deadXSteps(interestingVelocitiesX, interestingVelocitiesY));
        Integer maxStep = velocityToDistanceMap.entrySet().stream().map(entry -> entry.getKey()).max(Integer::compareTo).get();
        for (Integer currentStep = 1; currentStep < maxStep; currentStep++) {
            Set<Point> newPoints = velocitiesForStepN(currentStep, velocityToDistanceMap);
            foundPoints.addAll(newPoints);
        }
        return foundPoints;
    }

    // For 1 step, it is just each point.
    // For 2 steps, it is figure out the minSpeed to get to minX and the maxSpeed to get to maxX. (IV = InitialVelocity)
    //    Step 2: For x: distance = 2 * IV - 1
    //            For y: distance = 2 * IV - 1
    //    Step 3: For x: distance = 3 * IV - 3
    //            For y: distance = 3 * IV - 3 =
    //    For any step, the equation to find Initial Velocity for x:  x = (distance + sum(step-1))/step
    //                                                            y:  y = (distance + sum(step-1))/step
    public static Set<Point> velocitiesForStepN(Integer step, Map<Integer, Integer> sumMap) {
        Set<Point> points = new HashSet<>();
        Integer minVelocityX = (minX + sumMap.get(step-1))/step;
        Integer maxVelocityX = (maxX + sumMap.get(step-1))/step;
        Integer minVelocityY = (minY + sumMap.get(step-1))/step;
        Integer maxVelocityY = (maxY + sumMap.get(step-1))/step;
        for (Integer x = minVelocityX; x <= maxVelocityX; x++) {
            for (Integer y = minVelocityY; y < maxVelocityY;y++) {
                if (simulationPart2(x,y,step)) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    public static List<Integer> interestingVelocities(Map<Integer, Integer> velocityMap, Integer minValue, Integer maxValue) {
        return velocityMap.entrySet().stream()
                .filter(entry -> entry.getValue() <= maxValue)
                .filter(entry -> entry.getValue() >= minValue)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    public static List<Integer> step(Integer x, Integer y, Integer vx, Integer vy) {
        List<Integer> values = new ArrayList<>();
        values.add(x+vx);
        values.add(y+vy);
        values.add(vx > 0 ? vx-1 : 0);
        values.add(vy-1);
        return values;
    }

    public static boolean simulationPart2(Integer vx0, Integer vy0, Integer steps) {
        Integer x = 0;
        Integer y = 0;
        Integer vx = vx0;
        Integer vy = vy0;
        for (int step = 0; step <= steps; step++) {
            List<Integer> values = step(x,y,vx,vy);
            x = values.get(0);
            y = values.get(1);
            vx = values.get(2);
            vy = values.get(3);
            if (isInArea(x,y)) {
                return true;
            }
        }
        return false;

    }

    public static Set<Point> deadXSteps(List<Integer> fallingX, List<Integer> fallingY) {
        Set<Point> points = new HashSet<>();
        for (Integer x: fallingX) {
            for (Integer y: fallingY) {
                points.add(new Point(x,y));
            }
        }
        return points;

    }

    public static Integer sumIntegersFrom0toN(int num) {
        return (num*(num+1)/2);
    }

    public static boolean isInArea(Integer x, Integer y) {
        return (minX <= x) && (x <= maxX) && (minY <= y) && (y <= maxY);
    }
}
