package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpacePolice extends AoCDay {

    public static class HullPainterRobot extends IntCodeComputer{
        Coord2D currentLocation;
        Coord2D currentDirection;
        Map<Coord2D, Long> hullPaint;
        Integer action;
        public HullPainterRobot() {
            super();
            this.currentLocation = new Coord2D(0,0);
            this.currentDirection = new Coord2D(0,-1);
            this.hullPaint = new HashMap<>();
            this.action = 0;
        }

        @Override
        public Pair<Boolean, Long> getInputValue() {
            return Pair.of(true, hullPaint.getOrDefault(currentLocation, 0L));
        }

        @Override
        public void addOutputValue(Long outputValue) {
            if (action == 0) {
                hullPaint.put(currentLocation, outputValue);
                action++;
            } else {
                if (outputValue == 0L) {
                    Coord2D newDirection = new Coord2D(currentDirection.y, -1 * currentDirection.x);
                    currentDirection = newDirection;
                } else {
                    Coord2D newDirection = new Coord2D(-1*currentDirection.y, currentDirection.x);
                    currentDirection = newDirection;
                }
                currentLocation = new Coord2D(currentLocation.x + currentDirection.x, currentLocation.y + currentDirection.y);
                action--;
            }
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,11,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(List<String> lines) {
        HullPainterRobot robot = new HullPainterRobot();
        robot.setId(1L);
        robot.initializeIntCode(lines);
        robot.run();
        return robot.hullPaint.size();
    }

    String solutionPart2(List<String> lines) {
        HullPainterRobot robot = new HullPainterRobot();
        robot.setId(1L);
        robot.initializeIntCode(lines);
        robot.hullPaint.put(new Coord2D(0,0),1L);
        robot.run();

        Map<Coord2D, Long> values = robot.hullPaint;
        int minX = values.keySet().stream().mapToInt(c -> c.x).min().getAsInt();
        int maxX = values.keySet().stream().mapToInt(c -> c.x).max().getAsInt();
        int minY = values.keySet().stream().mapToInt(c -> c.y).min().getAsInt();
        int maxY = values.keySet().stream().mapToInt(c -> c.y).max().getAsInt();
        String retVal = "\n";
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Long val = values.getOrDefault(new Coord2D(x, y),0L);
                if (val == 1L) {
                    retVal += "#";
                } else {
                    retVal += " ";
                }
            }
            retVal += "\n";
        }
        return retVal;
    }
}
