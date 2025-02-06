package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManyWorldsInterpretation extends AoCDay {
    public Map<Coord2D,String> keys;
    public Map<Coord2D,String> doors;
    public Set<Coord2D> openSpace;
    Coord2D startingPoint;

    Map<Integer,Set<String>> keysInQuadrant;

    public static class State {
        Coord2D currentLocation;
        Set<String> keys;
        private int hashCode;

        public State(Coord2D currentLocation, Set<String> keys) {
            this.currentLocation = currentLocation;
            this.keys = keys;
            this.hashCode = Objects.hash(currentLocation, keys);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) {
                return false;
            }
            State other = (State) o;
            return currentLocation.equals(other.currentLocation) && keys.containsAll(other.keys) && other.keys.containsAll(keys);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }

    public static class StatePart2 {
        final List<Coord2D> currentLocations;
        final Set<String> keys;
        final Integer robotMoving;
        private int hashCode;

        public StatePart2(List<Coord2D> currentLocations, Set<String> keys, Integer robotMoving) {
            this.currentLocations = currentLocations;
            this.keys = keys;
            this.robotMoving = robotMoving;
            this.hashCode = Objects.hash(currentLocations, keys, robotMoving);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) {
                return false;
            }
            StatePart2 other = (StatePart2) o;
            return currentLocations.equals(other.currentLocations) && keys.equals(other.keys)
                    && robotMoving.equals(robotMoving);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }


    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,18,false,0);
        startingPoint = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(startingPoint);
        timeMarkers[2] = Instant.now().toEpochMilli();
        //part2Answer = solutionPart2(startingPoint);
        part2Answer = "needs work";
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Coord2D parseLines(List<String> lines) {
        keys = new HashMap<>();
        doors = new HashMap<>();
        openSpace = new HashSet<>();
        String[][] grid = convertToGrid(lines);
        Coord2D startingPoint = new Coord2D(0,0);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x].equals("@")) {
                    startingPoint = new Coord2D(y,x);
                    openSpace.add(new Coord2D(y,x));
                }
                else if (grid[y][x].equals(".")) {
                    openSpace.add(new Coord2D(y,x));
                } else if (grid[y][x].equals("#")) {

                } else if ((grid[y][x].charAt(0) - 96) > 0) {
                    keys.put(new Coord2D(y,x),grid[y][x]);
                } else if ((grid[y][x].charAt(0) - 64) > 0) {
                    doors.put(new Coord2D(y, x), grid[y][x]);
                }
            }
        }
        Set<String> openSpaces0 = new HashSet<>();
        Set<String> openSpaces1 = new HashSet<>();
        Set<String> openSpaces2 = new HashSet<>();
        Set<String> openSpaces3 = new HashSet<>();
        for (Map.Entry<Coord2D,String> s : keys.entrySet()) {
            Coord2D upLoc = s.getKey().add(new Coord2D(-1,0));
            Coord2D downLoc = s.getKey().add(new Coord2D(1,0));
            Coord2D leftLoc = s.getKey().add(new Coord2D(0,-1));
            Coord2D rightLoc = s.getKey().add(new Coord2D(0,1));
            if (s.getKey().x < startingPoint.x) {
                if (s.getKey().y < startingPoint.y) {
                    openSpaces0.add(s.getValue());
                } else if (s.getKey().y > startingPoint.y) {
                    openSpaces1.add(s.getValue());
                } else {
                    if (openSpace.contains(leftLoc) || doors.containsKey(leftLoc) || keys.containsKey(leftLoc)) {
                        openSpaces0.add(s.getValue());
                    } else if (openSpace.contains(rightLoc) || doors.containsKey(rightLoc) || keys.containsKey(rightLoc)) {
                        openSpaces2.add(s.getValue());
                    }
                }
            } else if (s.getKey().x > startingPoint.x){
                if (s.getKey().y < startingPoint.y) {
                    openSpaces2.add(s.getValue());
                } else if (s.getKey().y > startingPoint.y){
                    openSpaces3.add(s.getValue());
                } else {
                    if (openSpace.contains(leftLoc) || doors.containsKey(leftLoc) || keys.containsKey(leftLoc)) {
                        openSpaces1.add(s.getValue());
                    } else if (openSpace.contains(rightLoc) || doors.containsKey(rightLoc) || keys.containsKey(rightLoc)) {
                        openSpaces3.add(s.getValue());
                    }
                }
            } else {
                if (s.getKey().y < startingPoint.y) {
                    if (openSpace.contains(upLoc) || doors.containsKey(upLoc) || keys.containsKey(upLoc)) {
                        openSpaces0.add(s.getValue());
                    } else if (openSpace.contains(downLoc) || doors.containsKey(downLoc) || keys.containsKey(downLoc)) {
                        openSpaces2.add(s.getValue());
                    }
                } else if (s.getKey().y > startingPoint.y) {
                    if (openSpace.contains(upLoc) || doors.containsKey(upLoc) || keys.containsKey(upLoc)) {
                        openSpaces1.add(s.getValue());
                    } else if (openSpace.contains(downLoc) || doors.containsKey(downLoc) || keys.containsKey(downLoc)) {
                        openSpaces3.add(s.getValue());
                    }
                }
            }
        }
        keysInQuadrant = new HashMap<>();
        keysInQuadrant.put(0,openSpaces0);
        keysInQuadrant.put(1,openSpaces1);
        keysInQuadrant.put(2,openSpaces2);
        keysInQuadrant.put(3,openSpaces3);
        return startingPoint;
    }

    public Stream<State> nextStates(State currentState, Set<State> seen) {
        Coord2D loc = currentState.currentLocation;
        List<Coord2D> possibleLocs = List.of(
                loc.add(new Coord2D(-1,0)),
                loc.add(new Coord2D(0,1)),
                loc.add(new Coord2D(1,0)),
                loc.add(new Coord2D(0,-1)));
        List<Coord2D> keyFound = possibleLocs.stream().filter(n -> keys.containsKey(n)).collect(Collectors.toList());
        List<Coord2D> doorFound = possibleLocs.stream().filter(n -> doors.containsKey(n)).collect(Collectors.toList());
        List<Coord2D> openSpaces = possibleLocs.stream().filter(n -> openSpace.contains(n)).collect(Collectors.toList());
        List<State> nextStates = new ArrayList<>();
        if (!keyFound.isEmpty()) {
            for (Coord2D keyLoc : keyFound) {
                Set<String> nextKeys = new HashSet<>(currentState.keys);
                nextKeys.add(keys.get(keyLoc));
                nextStates.add(new State(keyLoc,nextKeys));
            }
        }
        if (!doorFound.isEmpty()) {
            for (Coord2D doorLoc : doorFound) {
                String keyNeeded = doors.get(doorLoc).toLowerCase();
                if (currentState.keys.contains(keyNeeded)) {
                    nextStates.add(new State(doorLoc, new HashSet<>(currentState.keys)));
                }
            }
        }
        for (Coord2D openSpace : openSpaces) {
            nextStates.add(new State(openSpace, new HashSet<>(currentState.keys)));
        }
        return nextStates.stream().filter(n -> !seen.contains(n));
    }

    public Integer solutionPart1(Coord2D startingPoint) {
        State initialState = new State(startingPoint, new HashSet<>());
        Set<State> states = new HashSet<>();
        states.add(initialState);
        Set<State> seen = new HashSet<>();
        for (int i = 1; true; i ++) {
            seen.addAll(states);
            states = states.stream().flatMap(s -> nextStates(s, seen)).collect(Collectors.toSet());
            for (State state : states) {
                if (keys.entrySet().stream().allMatch(n -> state.keys.contains(n.getValue()))) {
                    return i;
                }
            }
        }
    }

    public Stream<StatePart2> nextStatesPart2(StatePart2 state, Set<StatePart2> seen, int depth) {
        if (depth > 3) {
            return Stream.of();
        }
        if (keysInQuadrant.get(state.robotMoving).stream().allMatch(k -> state.keys.contains(k))) {
            StatePart2 nextStep = new StatePart2(state.currentLocations, state.keys, (state.robotMoving+1)%4);
            return nextStatesPart2(nextStep,seen, depth+1);
        }
        List<Coord2D> possibleMoves = List.of (new Coord2D(-1,0),
                new Coord2D(1,0), new Coord2D(0,-1), new Coord2D(0,1));
        List<Coord2D> cL = new ArrayList<>(state.currentLocations);
        Coord2D currentLocation = cL.remove(state.robotMoving.intValue());
        List<Coord2D> possibleLocs = possibleMoves.stream().map(c -> currentLocation.add(c)).collect(Collectors.toList());
        List<Coord2D> keyFound = possibleLocs.stream().filter(n -> keys.containsKey(n)).collect(Collectors.toList());
        List<Coord2D> doorFound = possibleLocs.stream().filter(n -> doors.containsKey(n)).collect(Collectors.toList());
        List<Coord2D> openSpaces = possibleLocs.stream().filter(n -> openSpace.contains(n)).collect(Collectors.toList());
        Set<StatePart2> nextStates = new HashSet<>();
        if (!keyFound.isEmpty()) {
            for (Coord2D keyLoc : keyFound) {
                Set<String> nextKeys = new HashSet<>(state.keys);
                nextKeys.add(keys.get(keyLoc));
                List<Coord2D> nextLocs = new ArrayList<>(cL);
                nextLocs.add(state.robotMoving, keyLoc);
                nextStates.add(new StatePart2(nextLocs,nextKeys,state.robotMoving));
            }
        }
        if (!doorFound.isEmpty()) {
            for (Coord2D doorLoc : doorFound) {
                String keyNeeded = doors.get(doorLoc).toLowerCase();
                if (state.keys.contains(keyNeeded)) {
                    List<Coord2D> nextLocs = new ArrayList<>(cL);
                    nextLocs.add(state.robotMoving, doorLoc);
                    nextStates.add(new StatePart2(nextLocs,new HashSet<>(state.keys),state.robotMoving));
                }
            }
        }
        for (Coord2D openSpace : openSpaces) {
            List<Coord2D> nextLocs = new ArrayList<>(cL);
            nextLocs.add(state.robotMoving, openSpace);
            nextStates.add(new StatePart2(nextLocs, new HashSet<>(state.keys),state.robotMoving));
        }
        nextStates = nextStates.stream().filter(n -> !seen.contains(n)).collect(Collectors.toSet());
        if (nextStates.isEmpty()) {
            StatePart2 nextStep = new StatePart2(state.currentLocations, state.keys, (state.robotMoving+1)%4);
            return nextStatesPart2(nextStep,seen,depth+1);
        }
        return nextStates.stream();
    }

    public Integer solutionPart2(Coord2D startingPoint) {
        openSpace.remove(new Coord2D(startingPoint.x-1,startingPoint.y));
        openSpace.remove(new Coord2D(startingPoint.x+1,startingPoint.y));
        openSpace.remove(new Coord2D(startingPoint.x,startingPoint.y-1));
        openSpace.remove(new Coord2D(startingPoint.x,startingPoint.y+1));
        openSpace.remove(startingPoint);
        Coord2D robot1 = new Coord2D(startingPoint.x-1,startingPoint.y-1);
        Coord2D robot2 = new Coord2D(startingPoint.x-1,startingPoint.y+1);
        Coord2D robot3 = new Coord2D(startingPoint.x+1,startingPoint.y-1);
        Coord2D robot4 = new Coord2D(startingPoint.x+1,startingPoint.y+1);
        Set<StatePart2> states = new HashSet<>();
        states.add(new StatePart2(List.of(robot1,robot2,robot3,robot4), new HashSet<>(), 0));
        Set<StatePart2> seen = new HashSet<>();
        for (int i = 1; true; i ++) {
            if (i % 50 == 0) {
                System.out.println("Part 2, Step # " + i);
            }
            seen.addAll(states);
            states = states.stream().flatMap(s -> nextStatesPart2(s, seen, 0)).collect(Collectors.toSet());
            for (StatePart2 state : states) {
                if (keys.entrySet().stream().allMatch(n -> state.keys.contains(n.getValue()))) {
                    return i;
                }
            }
        }
    }

}
