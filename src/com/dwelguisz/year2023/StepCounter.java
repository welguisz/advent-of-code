package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        Set<String> positions = new HashSet<>();
        Set<Coord2D> states = new HashSet<>();
        states.add(startingPosition);
        positions.add(startingPosition.toString());
        for (int i = 0; i < 64; i++) {
            states = states.stream().flatMap(s -> nextSpots(s,grid)).collect(Collectors.toSet());
            positions.addAll(states.stream().map(s -> s.toString()).collect(Collectors.toList()));
        }
        return (long) states.size();
    }

    Stream<Coord2D> nextSpotsPart2(Coord2D loc, Character[][] grid) {

        return POSSIBLE_NEXT_STEPS.stream()
                .map(n -> loc.add(n))
                .filter(n -> grid[newX(n.x% grid.length, grid.length)][newX(n.y % grid[0].length, grid[0].length)] != '#');
    }

    Integer newX(Integer x, Integer length) {
        return ((x % length) + length) % length;
    }
    Double solutionPart2(Character[][] grid, Coord2D startingPosition) {
        Set<Coord2D> states = new HashSet<>();
        states.add(startingPosition);
        int stepCount;
        Map<Coord2D, Integer> distances = new HashMap<>();
        distances.put(startingPosition, 0);
        List<Long> oddTotals = new ArrayList<>();
        Long totalReached = 0L;
        for (stepCount = 0; true; stepCount++) {
            states = states.stream().flatMap(s -> nextSpotsPart2(s, grid)).collect(Collectors.toSet());
            if (stepCount % 2 == 1) {
                totalReached += states.size();
                if (stepCount % 131 == 65) {
                    oddTotals.add(totalReached);
                }
                //We need at least 4 points to do a quadratic. 5 will guarantee a zero in createStack
                if (oddTotals.size() == 3) {
                    break;
                }
            }
        }
        // We are solving for coefficients in a quadratic
        // f(1) = a + b + c = oddTotals.get(0)
        // f(2) = 4a + 2b + c = oddTotals.get(1)
        // f(3) = 9a + 3b + c = oddTotals.get(2)
        // -4*f(1)+f(2) -> f(2)  = -2b -3c = y-4x
        // -9*f(1)+f(3) -> f(3) = -6b -8c = z - 9x
        // -0.5*f(2) -> f(2) = b + 3/2c = = -0.5(y-4x)
        // -6*f(2) + f(3) -> f(3) = c = z-9x+
        // -1.5*f(3) + f(2) -> f(2)
        // -1*f(3) + f(1) -> f(1)
        // -1*f(2) + f(2) -> f(2)
        Double a = Double.valueOf(oddTotals.get(0));
        Double b = Double.valueOf(oddTotals.get(1));
        Double c = Double.valueOf(oddTotals.get(2));
        b += -4*a;
        c += -9*a;
        b *= -0.5;
        c += -6*b;
        b += -1.5*c;
        a += -1 * c;
        a += -1 * b;
        Long loopCount = 26501365L / 131;
        return a*loopCount*loopCount+b*loopCount+c;

        //Belief that this is a quadratic, let's do
//        Long loopCount = 26501365L / 262 - 1;
//        Long currentLoopCount = i / 262L - 1;
//        Long deltaLoopCount = loopCount - currentLoopCount;
//        long deltaLoopCountTriangular = (loopCount * (loopCount+1))/2 - (currentLoopCount * (currentLoopCount+1))/2;
//        Long deltaDelta = deltaDeltas.get(deltaDeltas.size()-1);
//        Long initialDelta = deltas.get(0);
//        return (deltaDelta * deltaLoopCountTriangular + initialDelta * deltaLoopCount + totalReached);
    }

    List<List<Long>> createStack(List<Long> values) {
        List<List<Long>> stack = new ArrayList<>();
        stack.add(values);
        List<Long> currentRow = new ArrayList<>(values);
        for(int ix = 0; ix < 2; ix++) {
            final List<Long> t = currentRow;
            List<Long> diffRow = IntStream.range(0, currentRow.size() - 1).boxed()
                    .map(i -> t.get(i + 1) - t.get(i))
                    .collect(Collectors.toList());
            stack.add(diffRow);
            currentRow = new ArrayList<>(diffRow);
        }
        Collections.reverse(stack);
        return stack;
    }
}
