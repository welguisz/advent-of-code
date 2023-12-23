package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StepCounter extends AoCDay {
    Coord2D UP = new Coord2D(-1,0);
    Coord2D RIGHT = new Coord2D(0,1);
    Coord2D DOWN = new Coord2D(1,0);
    Coord2D LEFT = new Coord2D(0,-1);

    List<Coord2D> POSSIBLE_NEXT_STEPS = List.of(UP,RIGHT,DOWN,LEFT);

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 21, false, 0);
        char[][] grid = convertToCharGrid(lines);
        Character[][] newGrid = createNewGrid(grid);
        Coord2D startingPosition = findStartingLocation(grid, 'S');
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(newGrid, startingPosition);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(newGrid, startingPosition);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Character[][] createNewGrid(char[][] grid) {
        Character[][] newGrid = new Character[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }
        return newGrid;
    }
    Stream<Coord2D> nextSpots(Coord2D loc, Character[][] grid) {
        return POSSIBLE_NEXT_STEPS.stream().map(n -> loc.add(n))
                .filter(c -> 0 <= c.x && c.x < grid.length && 0 <= c.y && c.y < grid[0].length)
                .filter(c -> grid[c.x][c.y] != '#');

    }

    Long solutionPart1(Character[][] grid, Coord2D startingPosition) {
        Set<Coord2D> states = new HashSet<>();
        states.add(startingPosition);
        for (int i = 0; i < 64; i++) {
            states = states.stream().flatMap(s -> nextSpots(s,grid)).collect(Collectors.toSet());
        }
        return (long) states.size();
    }

    Stream<Coord2D> nextSpotsPart2(Coord2D loc, Character[][] grid) {
        return POSSIBLE_NEXT_STEPS.stream()
                .map(n -> loc.add(n))
                .filter(n -> grid[newX(n.x% grid.length, grid.length)][newX(n.y % grid[0].length, grid[0].length)] != '#');
    }

    Integer newX(Integer x, Integer length) {
        return (x + length) % length;
    }
    Long solutionPart2(Character[][] grid, Coord2D startingPosition) {
        Set<Coord2D> states = new HashSet<>();
        states.add(startingPosition);
        int stepCount;
        int oddOrEven = 1;
        List<Long> oddTotals = new ArrayList<>();
        Map<Boolean, Set<Coord2D>> lastSetOfCoords = new HashMap<>();
        lastSetOfCoords.put(false, new HashSet<>());
        lastSetOfCoords.put(true, new HashSet<>());
        Map<Boolean, Long> runningSum = new HashMap<>();
        runningSum.put(false,0L);
        runningSum.put(true,0L);
        for (stepCount = 0; true; stepCount++) {
            final Integer finalStepCount = stepCount;
            states = states.stream().flatMap(s -> nextSpotsPart2(s, grid))
                    .filter(s -> !lastSetOfCoords.get(finalStepCount%2==oddOrEven).contains(s))
                    .collect(Collectors.toSet());
            Set<Coord2D> finalStates = states;
            runningSum.computeIfPresent(stepCount%2==oddOrEven,(k, v) -> v + finalStates.size());
            lastSetOfCoords.put(stepCount%2==oddOrEven,states);
            if (stepCount % 262 == 65) {
                oddTotals.add(runningSum.get(stepCount%2==(oddOrEven^1)));
            }
            //We need at least 4 points to do a quadratic. 5 will guarantee a zero in createStack
            if (oddTotals.size() == 3) {
                break;
            }
        }
        List<Long> oddCoefficients = findCoefficients(oddTotals.get(0), oddTotals.get(1), oddTotals.get(2));
        Long loopCount = 26501365L/ 262;
        Long jumpValue = function(oddCoefficients.get(0),oddCoefficients.get(1),oddCoefficients.get(2), loopCount+1);
        return jumpValue;
    }

    //Think of this as the following:
    // f(x) = a*x^2+b*x+c
    // By have three points, we can create a set of equations. We are using 1,2,and 3 here
    // f(1) = a+b+c = x
    // f(2) = 4a+2b+c = y
    // f(3) = 9a+3b+c = z;
    // We are going to do manipulate the equations, so we get
    // a = x'
    // b = y'
    // c = z'
    // 1) -4*f(1) + f(2) -> f(2). This causes f(2) to be -2b-3c = y-4x
    // 2) -9*f(1) + f(3) -> f(3). This causes f(3) to be -6b-8c = y-9x
    // 3) -0.5 * f(2) -> f(2). f(2) is now: b+1.5c=-0.5(y-4x)
    // 4) 6*f(2) + f(3) -> f(3). f(3) is now: c= z'
    // 5) -1.5*f(3) + f(2) ->f(2). f(2) is now b=y'
    // 6) -1 * f(3) + f(1) -> f(1).
    // 7) -1 * f(2) + f(2) -> f(1). f(1) is now a=x'
    List<Long> findCoefficients(Long x, Long y, Long z) {
        Double aDouble = Double.valueOf(x);
        Double bDouble = Double.valueOf(y);
        Double cDouble = Double.valueOf(z);
        bDouble += -4*aDouble;
        bDouble *= -0.5;
        cDouble += -9*aDouble;
        cDouble += 6*bDouble;
        bDouble += -1.5*cDouble;
        aDouble += -1 * cDouble;
        aDouble += -1 * bDouble;
        return List.of(aDouble.longValue(), bDouble.longValue(), cDouble.longValue());
    }

    Long function(Long a, Long b, Long c, Long x) {
        return a*x*x+b*x+c;
    }
}
