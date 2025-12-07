package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dwelguisz.utilities.Grid.createCharGrid;

public class TrashCompactor extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 6, false, 0);
        char[][] grid = createCharGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    List<Character> OPERANDS = List.of('+', '*');

    long solutionPart1(char[][] grid) {
        Map<Integer, List<Long>> values = new HashMap<>();
        List<Character> operations = new ArrayList<>();
        for (int y = 0; y < grid.length; y++) {
            int counter = 0;
            boolean isOperand = OPERANDS.contains(grid[y][0]);
            int value = 0;
            for(int x = 0; x < grid[0].length; x++) {
                if (isOperand) {
                    if (OPERANDS.contains(grid[y][x])) {
                        operations.add(grid[y][x]);
                    }
                } else {
                    if (grid[y][x] == ' ') {
                        if (value != 0) {
                            values.computeIfAbsent(counter, k -> new ArrayList<>()).add((long) value);
                            value = 0;
                            counter++;
                        }
                    } else if (x == grid[0].length - 1) {
                        value *= 10;
                        value += grid[y][x] - 48;
                        values.computeIfAbsent(counter, k -> new ArrayList<>()).add((long) value);
                        value = 0;
                        counter++;
                    } else {
                        value *= 10;
                        value += grid[y][x] - 48;
                    }
                }
            }
        }
        long answer = 0l;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i) == '+') {
                answer += values.get(i).stream().reduce(0L, Long::sum);
            } else if (operations.get(i) == '*') {
                answer += values.get(i).stream().reduce(1L, (a,b) -> a*b);
            }
        }
        return answer;
    }

    List<Character> BREAK_CHARS = List.of('-', '+', ' ');
    long solutionPart2(char[][] grid) {
        Map<Integer, List<Long>> values = new HashMap<>();
        List<Character> operations = new ArrayList<>();
        int decOperationSize = 0;
        for (int x = 0; x < grid[0].length; x++) {
            int value = 0;
            int operationsSize = operations.size();
            for (int y = 0; y < grid.length; y++) {
                boolean incCounter = true;
                for(int y1 = 0; y1 < grid.length; y1++) {
                    if (!BREAK_CHARS.contains(grid[y1][x])) {
                        incCounter = false;
                        break;
                    }
                }
                if (incCounter) {
                    decOperationSize = 0;
                }
                boolean isOperand = OPERANDS.contains(grid[y][0]);
                if (isOperand) {
                    if (value != 0) {
                        values.computeIfAbsent(operationsSize - decOperationSize, k -> new ArrayList<>()).add((long) value);
                        value = 0;
                    }
                    if (OPERANDS.contains(grid[y][x])) {
                        operations.add(grid[y][x]);
                        decOperationSize = 1;
                    }
                } else {
                    if (grid[y][x] == ' ') {
                        if (value != 0) {
                            values.computeIfAbsent(operationsSize - decOperationSize, k -> new ArrayList<>()).add((long) value);
                            value = 0;
                        }
                    } else if (BREAK_CHARS.contains(grid[y][x])) {
                        values.computeIfAbsent(operationsSize - decOperationSize , k -> new ArrayList<>()).add((long) value);
                        value = 0;
                    } else {
                        value *= 10;
                        value += grid[y][x] - 48;
                    }
                }
            }
        }
        long answer = 0l;
        for (int i = 0; i < operations.size(); i++) {
            if (i == 1000) {
                System.out.println("Break here");
            }
            List<Long> tmp = values.get(i);
            if (operations.get(i) == '+') {
                answer += values.get(i).stream().reduce(0L, Long::sum);
            } else if (operations.get(i) == '*') {
                answer += values.get(i).stream().reduce(1L, (a,b) -> a*b);
            }
        }
        return answer;
    }
}
