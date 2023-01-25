package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class ProbablyAFireHazard extends AoCDay {

    public static class Instruction {
        Boolean toggle;
        Boolean turnOn;
        Coord2D startingPoint;
        Coord2D endingPoint;

        public Instruction(Boolean toggle, Boolean turnOn, Coord2D startingPoint, Coord2D endingPoint) {
            this.toggle = toggle;
            this.turnOn = turnOn;
            this.startingPoint = startingPoint;
            this.endingPoint = endingPoint;
        }

        List<Coord2D> pointsToGet() {
            List<Coord2D> points = new ArrayList<>();
            for (Integer x = startingPoint.x; x <= endingPoint.x; x++) {
                for (Integer y = startingPoint.y; y <= endingPoint.y; y++) {
                    points.add(new Coord2D(x,y));
                }
            }
            return points;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,6,false,0);
        List<Instruction> instructions = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(instructions);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(instructions);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Instruction> parseLines(List<String> lines) {
        List<Instruction> instructions = new ArrayList<>();
        for (String l : lines) {
            String split[] = l.split(" ");
            if (split[0].equals("turn")) {
                String point1 = split[2];
                String point2 = split[4];
                List<Integer> point1I = Arrays.stream(point1.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                List<Integer> point2I = Arrays.stream(point2.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                Coord2D startingPoint = new Coord2D(point1I.get(0), point1I.get(1));
                Coord2D endingPoint = new Coord2D(point2I.get(0), point2I.get(1));
                Boolean position = split[1].equals("on");
                instructions.add(new Instruction(false, position, startingPoint, endingPoint));
            } else {
                String point1 = split[1];
                String point2 = split[3];
                List<Integer> point1I = Arrays.stream(point1.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                List<Integer> point2I = Arrays.stream(point2.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                Coord2D startingPoint = new Coord2D(point1I.get(0), point1I.get(1));
                Coord2D endingPoint = new Coord2D(point2I.get(0), point2I.get(1));
                instructions.add(new Instruction(true,false,startingPoint,endingPoint));
            }
        }
        return instructions;
    }
    Long solutionPart1(List<Instruction> instructions) {
        Map<Coord2D, Boolean> map = new HashMap<>();
        for (Instruction instruction : instructions) {
            List<Coord2D> points = instruction.pointsToGet();
            for (Coord2D point : points) {
                Boolean value = map.getOrDefault(point, false);
                if (instruction.toggle) {
                    value ^= true;
                } else {
                    if (instruction.turnOn) {
                        value = true;
                    } else {
                        value = false;
                    }
                }
                map.put(point, value);
            }
        }
        return map.entrySet().stream().filter(e -> e.getValue()).count();
    }

    Integer solutionPart2(List<Instruction> instructions) {
        Map<Coord2D, Integer> map = new HashMap<>();
        for (Instruction instruction : instructions) {
            List<Coord2D> points = instruction.pointsToGet();
            for (Coord2D point : points) {
                Integer value = map.getOrDefault(point, 0);
                if (instruction.toggle) {
                    value += 2;
                } else {
                    if (instruction.turnOn) {
                        value += 1;
                    } else {
                        value -= (value == 0) ? 0 : 1;
                    }
                }
                map.put(point, value);
            }
        }
        return map.entrySet().stream().mapToInt(e -> e.getValue()).sum();
    }
}
