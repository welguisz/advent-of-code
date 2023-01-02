package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SetAndForget extends AoCDay {

    public static Coord2D UP = new Coord2D(-1,0);
    public static Coord2D DOWN = new Coord2D(1,0);
    public static Coord2D LEFT = new Coord2D(0,-1);
    public static Coord2D RIGHT = new Coord2D(0, 1);
    public static Map<Pair<Coord2D,String>,Coord2D> TURNS = Map.of(
            Pair.of(UP,"L"),LEFT,Pair.of(UP,"R"),RIGHT,
            Pair.of(RIGHT,"L"),UP,Pair.of(RIGHT,"R"),DOWN,
            Pair.of(DOWN,"L"),RIGHT, Pair.of(DOWN,"R"),LEFT,
            Pair.of(LEFT,"L"),DOWN,Pair.of(LEFT,"R"),UP);

    public static class ASCIIRobot extends IntCodeComputer {
        Map<Coord2D, String> map;
        Coord2D robotLocation;
        Coord2D robotDirection;

        Integer x;
        Integer y;
        Integer maxX;
        Integer maxY;
        byte[] program;
        Integer programPointer;
        Long dustCleaned;
        Set<Coord2D> allScaffolding;
        public ASCIIRobot() {
            super();
            map = new HashMap<>();
            x = 0;
            y = 0;
            maxX = Integer.MIN_VALUE;
            maxY = Integer.MIN_VALUE;
            robotDirection = new Coord2D(0,0);
            robotLocation = new Coord2D(0,0);
            program = new byte[]{};
            programPointer = 0;
            dustCleaned = 0L;
            allScaffolding = new HashSet<>();
        }

        public void setFunctions(String main, String a, String b, String c) {
            String total = main + a + b + c + "n\n";
            program = total.getBytes(StandardCharsets.UTF_8);
        }
        public void printMap() {
            System.out.print("   ");
            for (int x = 0; x < maxX; x++) {
                System.out.print(x / 10);
            }
            System.out.println();
            System.out.print("   ");
            for (int x = 0; x < maxX; x++) {
                System.out.print(x % 10);
            }
            System.out.println();
            for (int y = 0; y < maxY-1; y++) {
                System.out.print(String.format("%2d ",y));
                for (int x = 0; x < maxX; x++) {
                    System.out.print(map.getOrDefault(new Coord2D(y,x),"?"));
                }
                System.out.println();
            }
        }

        @Override
        public Pair<Boolean, Long> getInputValue() {
            if (programPointer < program.length) {
                programPointer++;
                return Pair.of(true, Long.valueOf(program[programPointer-1]));
            }
            return Pair.of(false, 0L);
        }

        @Override
        public void addOutputValue(Long outputValue) {
            if (outputValue > 255L) {
                dustCleaned = outputValue;
            }
            else if (outputValue == 35L) {
                map.put(new Coord2D(y,x), "#");
                allScaffolding.add(new Coord2D(y,x));
                x++;
            } else if (outputValue == 46L) {
                map.put(new Coord2D(y,x), ".");
                x++;
            } else if (outputValue == 10L) {
                x = 0;
                y++;
            } else if (outputValue == 94L) { //robot Here; pointing up
                robotLocation = new Coord2D(y,x);
                robotDirection = UP;
                map.put(new Coord2D(y,x),"^");
                x++;
            }
            else if (outputValue == 74L) { //robot Here; pointing left
                robotLocation = new Coord2D(y,x);
                robotDirection = LEFT;
                map.put(new Coord2D(y,x),"<");
                x++;
            }else if (outputValue == 76L) { //robot Here; pointing right
                robotLocation = new Coord2D(y,x);
                robotDirection = RIGHT;
                map.put(new Coord2D(y,x),">");
                x++;
            } else if (outputValue == 118L) { //robot Here; pointing down
                robotLocation = new Coord2D(y,x);
                robotDirection = DOWN;
                map.put(new Coord2D(y,x),"v");
                x++;
            } else if (outputValue == 88L) { //Robot Here; drifted off
                robotLocation = new Coord2D(y,x);
                robotDirection = DOWN;
                map.put(new Coord2D(y,x),"X");
                x++;
            }
            maxX = Integer.max(maxX, x);
            maxY = Integer.max(maxY, y);
        }

        public boolean scaffoldAhead() {
            Coord2D nextStep = robotLocation.add(robotDirection);
            return !map.getOrDefault(nextStep,".").equals(".");
        }

        public boolean scaffoldLeft() {
            Coord2D turnLeft = TURNS.get(Pair.of(robotDirection,"L"));
            Coord2D nextStep = robotLocation.add(turnLeft);
            return !map.getOrDefault(nextStep,".").equals(".");
        }

        public boolean scaffoldRight() {
            Coord2D turnRight = TURNS.get(Pair.of(robotDirection,"R"));
            Coord2D nextStep = robotLocation.add(turnRight);
            return !map.getOrDefault(nextStep,".").equals(".");
        }

        public List<String> findPath() {
            Set<Coord2D> states = new HashSet<>();
            states.add(robotLocation);
            done = false;
            List<String> path = new ArrayList<>();
            while(!done) {
                if (scaffoldAhead()) {
                    path.add("F");
                    map.put(robotLocation,"F");
                    robotLocation = robotLocation.add(robotDirection);
                } else if (scaffoldLeft()) {
                    path.add("L");
                    map.put(robotLocation,"L");
                    robotDirection = TURNS.get(Pair.of(robotDirection,"L"));
                } else if (scaffoldRight()) {
                    path.add("R");
                    map.put(robotLocation,"R");
                    robotDirection = TURNS.get(Pair.of(robotDirection, "R"));
                } else {
                    path.add("F");
                    map.put(robotLocation,"F");
                }
                done = map.entrySet().stream().noneMatch(e -> e.getValue().equals("#"));
            }
            return shortenedPath(path);
        }

        public List<String> shortenedPath(List<String> path) {
            List<String> newPath = new ArrayList<>();
            Integer steps = 0;
            List<String> turns = List.of("R","L");
            for (String p : path) {
                if (turns.contains(p)) {
                    if (!newPath.isEmpty()) {
                        newPath.add(steps.toString());
                        steps = 0;
                    }
                    newPath.add(p);
                } else {
                    steps++;
                }
            }
            newPath.add(steps.toString());
            return newPath;
        }

        public List<Coord2D> neighbors(Coord2D point) {
            List<Coord2D> directions = List.of(UP,DOWN,LEFT,RIGHT);
            return directions.stream().map(d -> point.add(d)).collect(Collectors.toList());
        }

        public boolean isIntersection(Coord2D point) {
            List<Coord2D> neighbors = neighbors(point);
            return map.getOrDefault(point,".").equals("#") && neighbors.stream().allMatch(n -> map.getOrDefault(n,".").equals("#"));
        }

        public Integer calculateAlignment() {
            Integer sum = 0;
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x <= maxX; x++) {
                    sum += isIntersection(new Coord2D(y,x)) ? y * x : 0;
                }
            }
            return sum;
        }

    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day17/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(lines);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Integer solutionPart1(List<String> lines) {
        ASCIIRobot robot = new ASCIIRobot();
        robot.setId(1L);
        robot.initializeIntCode(lines);
        robot.run();
        List<String> path = robot.findPath();
        robot.printMap();
        System.out.println(path);
        return robot.calculateAlignment();
    }

    Long solutionPart2(List<String> lines) {
        ASCIIRobot robot = new ASCIIRobot();
        robot.setId(1L);
        robot.initializeIntCode(lines);
        robot.setIntCodeMemory(0L,2L);


        //Best scenario
        //R,10,L,12,R,6, R,10,L,12,R,6, R,6,R,10,R,12,R,6, R,10,L,12,L,12, R,6,R,10,R,12,R,6, R,10,L,12,L,12, R,6,R,10,R,12,R,6, R,10,L,12,L,12, R,6,R,10,R,12,R,6, R,10,L,12,R,6]
        // A,A,B,C,B,C,B,C,B,A
        //              00000000001111111111
        //              01234567890123456789
        String main =  "A,A,B,C,B,C,B,C,B,A\n";
        String funcA = "R,10,L,12,R,6\n";
        String funcB = "R,6,R,10,R,12,R,6\n";
        String funcC = "R,10,L,12,L,12\n";
        robot.setFunctions(main, funcA, funcB, funcC);
        robot.x = 0;
        robot.y = 0;
        robot.run();
        if (robot.dustCleaned == 0L) {
            robot.printMap();
        }
        return robot.dustCleaned;
    }
}
