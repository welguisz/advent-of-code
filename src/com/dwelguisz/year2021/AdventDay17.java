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
        String myTargetArea = "target area: x=57..116, y=-198..-148";
        String sampleArea = "target area: x=20..30, y=-10..-5";
        String line = myTargetArea;
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

    // Logic here:
    // What comes up, must come down.  The y coordinates going up will be the same going down.
    // So y = 0 will happen at step 0, and step (2*vy0) - 1.
    // Go one more step and the location at step(2*vy0).  At step(2*vy0), vyN = vy0 + (-2*vy0) -1
    // So solve for vy, vyN = vy0 - 1.
    // So highest vy0 = yMin + 1.
    // Looking at the track, the pattern is:
    //   v0 = 0 => 0. 1 => 1; 2 => 3; 3 => 6; 4 => 10;... This is the classic Sum of Integers from 0 to N.
    // That equation is (v0*(v0+1))/2.
    public static Integer solutionPart1() {
        return sumIntegersFrom0toN(minY);
    }

    public static Integer solutionPart2() {
        List<Integer> points = List.of(minX, maxX, minY, maxX);
        Integer maxPoint = points.stream().map(i -> Math.abs(i)).max(Integer::compareTo).get();
        Set<Point> foundPoints = new HashSet<>();
        // Want a map that has given step, sum of integers from 0 to stepN
        // Should result in 0 => 0; 1 => 1; 2 => 3; 3 => 6; 4 => 10; ....
        Map<Integer, Integer> velocityToDistanceMap = new HashMap<>();
        int sum = 0;
        int step = 0;
        while (sum <= (2*maxPoint)) {
            velocityToDistanceMap.put(step, sum);
            step += 1;
            sum += step;
        }

        //Find interesting velocities where we shoot the probe as high as possible and the change in X becomes 0
        //because X's velocity has reached 0.
        List<Integer> interestingVelocitiesX = interestingVelocities(velocityToDistanceMap, minX, maxX);

        //Find interesting velocities where we shoot the probe as high as possible and the probe is falling straight
        //down.  We need to calculate Velocities which will land in the target area.
        List<Integer> interestingVelocitiesY = interestingVelocities(velocityToDistanceMap, Math.abs(maxY), Math.abs(minY));
        for (int y = 18; y <= minY *-1; y++) {
            interestingVelocitiesY.add(y);
        }
        foundPoints.addAll(deadXSteps(interestingVelocitiesX, interestingVelocitiesY));

        Integer maxStep = velocityToDistanceMap.entrySet().stream().map(entry -> entry.getKey()).max(Integer::compareTo).get();
        for (Integer currentStep = 1; currentStep < maxStep; currentStep++) {
            Set<Point> newPoints = velocitiesForStepN(currentStep, velocityToDistanceMap);
            foundPoints.addAll(newPoints);
        }
        return foundPoints.size();
    }

    // For an upward velocity, as it crosses the x axis, the differences between expectedSumOfIntegers and vY
    // For v = 1; {1 => -2; 3 => -5; 6 => -9; 10 => -15
    // For v = 2; {1 => -3; 3 => -7; 6 => -12; 10 => -18
    // For v = 3; {1 => -4; 3 => -9; 6 => -15; 10 => -22
    // For v = 4; {1 => -5; 3 => -11; 6 => -18; 10 => -26
    // ----
    // So looking at this, we can calculate the different velocities that might cause the probe to fall in the area
    //    New locations = -1*sumMap(step) + (-1*v)*step
    public static Set<Integer> calculateInterestingYs(Map<Integer, Integer> sumMap) {
        Set<Integer> lookAtTheseVs = new HashSet<>();
        for (int v = 0; v <= (-1 * minY); v ++) {

        }
        return lookAtTheseVs;
    }

    public static Boolean isInYTarget(Map<Integer, Integer> sumMap, Integer initialVelocity) {
        Integer closestStep = interestingVelocities(sumMap, Math.abs(maxY), Math.abs(minY)).get(0);
        Integer smallV = closestStep - 3;
        Integer bigV = closestStep + 3;
        for (Integer v = smallV; v < bigV; v++) {

        }
        return false;
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
            for (Integer y = minVelocityY; y <= maxVelocityY;y++) {
                if (x == 6 && y == 5) {
                    System.out.println("Stop");
                }
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

    public static boolean simulateBF(Integer vx0, Integer vy0) {
        Integer x = 0;
        Integer y = 0;
        Integer vx = vx0;
        Integer vy = vy0;
        while (y >= minY && x <= maxX) {
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

    public static boolean simulationPart2(Integer vx0, Integer vy0, Integer steps) {
        Integer x = 0;
        Integer y = 0;
        Integer vx = vx0;
        Integer vy = vy0;
        for (int step = 0; step <= steps+1; step++) {
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
                if (simulateBF(x,y)) {
                    points.add(new Point(x, y));
                }
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
