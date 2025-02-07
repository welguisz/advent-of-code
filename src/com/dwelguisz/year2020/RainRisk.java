package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.List;

public class RainRisk extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,12,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Long solutionPart1(List<String> lines) {
        Integer direction = 0;  // 0 -> East; 90 ->South; 180 -> West; 270 -> North
        Long xLocation = 0L;
        Long yLocation = 0L;
        for (String line : lines) {
            String command = line.substring(0,1);
            Integer steps = Integer.parseInt(line.substring(1));
            if (command.equals("F")) {
                if (direction == 0) {
                    xLocation += steps;
                } else if (direction == 180) {
                    xLocation -= steps;
                } else if (direction == 90) {
                    yLocation -= steps;
                } else if (direction == 270) {
                    yLocation += steps;
                }
            } else if (command.equals("N")) {
                yLocation += steps;
            } else if (command.equals("S")) {
                yLocation -= steps;
            } else if (command.equals("E")) {
                xLocation += steps;
            } else if (command.equals("W")) {
                xLocation -= steps;
            } else if (command.equals("L")) {
                direction -= steps;
            } else if (command.equals("R")) {
                direction += steps;
            }
            while (direction < 0) {
                direction += 360;
            }
            while (direction >= 360) {
                direction -= 360;
            }
        }
        return (Math.abs(xLocation) + Math.abs(yLocation));
    }

    private Long solutionPart2(List<String> lines) {
        Long xLocation = 0L;
        Long yLocation = 0L;
        Long wayPointX = 10L;
        Long wayPointY = 1L;
        for (String line : lines) {
            String command = line.substring(0,1);
            Integer steps = Integer.parseInt(line.substring(1));
            if (command.equals("F")) {
                xLocation += (wayPointX * steps);
                yLocation += (wayPointY * steps);
            } else if (command.equals("N")) {
                wayPointY += steps;
            } else if (command.equals("S")) {
                wayPointY -= steps;
            } else if (command.equals("E")) {
                wayPointX += steps;
            } else if (command.equals("W")) {
                wayPointX -= steps;
            } else if (command.equals("L")) {
                Pair<Long, Long> newWaypoint = rotateWayPoint(wayPointX, wayPointY, steps/90);
                wayPointX = newWaypoint.getLeft();
                wayPointY = newWaypoint.getRight();
            } else if (command.equals("R")) {
                Pair<Long, Long> newWaypoint = rotateWayPoint(wayPointX, wayPointY, -steps/90);
                wayPointX = newWaypoint.getLeft();
                wayPointY = newWaypoint.getRight();
            }
        }
        return (Math.abs(xLocation) + Math.abs(yLocation));
    }

    private Pair<Long, Long> rotateWayPoint(Long wayPointX, Long wayPointY, Integer turns) {
        switch (turns) {
            case 1:
            case -3:
                return Pair.of(-wayPointY, wayPointX);
            case 2:
            case -2:
                return Pair.of(-wayPointX, -wayPointY);
            case 3:
            case -1:
                return Pair.of(wayPointY, -wayPointX);
            default:
                return Pair.of(wayPointX, wayPointY);
        }
    }

}
