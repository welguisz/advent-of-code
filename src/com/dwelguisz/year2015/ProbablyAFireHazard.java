package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class ProbablyAFireHazard extends AoCDay {
    static int MAX_ROW = 1000;
    static Boolean[][] grid = new Boolean[MAX_ROW][MAX_ROW];
    static Integer[][] betterGrid = new Integer[MAX_ROW][MAX_ROW];
    static {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_ROW; j++) {
                grid[i][j] = false;
                betterGrid[i][j] = 0;
            }
        }
    }

    public void solve() {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2015/day06/input.txt");
        Long part1 = solutionPart1(instructions);
        Long part2 = solutionPart2(instructions);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    static Long solutionPart1(List<String> strings) {
        for (String str : strings) {
            List<String> instruction = Arrays.asList(str.split(" "));
            if (instruction.get(0).equals("turn")) {
                String point1 = instruction.get(2);
                String point2 = instruction.get(4);
                List<Integer> point1I = Arrays.stream(point1.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                List<Integer> point2I = Arrays.stream(point2.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                for (int x = point1I.get(0); x <= point2I.get(0); x++) {
                    for (int y = point1I.get(1); y <= point2I.get(1); y++) {
                        if (instruction.get(1).equals("off")) {
                            grid[x][y] = false;
                        } else if (instruction.get(1).equals("on")) {
                            grid[x][y] = true;
                        }
                    }
                }
            } else if (instruction.get(0).equals("toggle")) {
                String point1 = instruction.get(1);
                String point2 = instruction.get(3);
                List<Integer> point1I = Arrays.stream(point1.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                List<Integer> point2I = Arrays.stream(point2.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                for (int x = point1I.get(0); x <= point2I.get(0); x++) {
                    for (int y = point1I.get(1); y <= point2I.get(1); y++) {
                        grid[x][y] ^= true;
                    }
                }
            }
        }
        Long count = 0L;
        for (int x = 0; x < MAX_ROW; x++) {
            for (int y = 0; y < MAX_ROW; y++) {
                if (grid[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }

    static Long solutionPart2(List<String> strings) {
        for (String str : strings) {
            List<String> instruction = Arrays.asList(str.split(" "));
            if (instruction.get(0).equals("turn")) {
                String point1 = instruction.get(2);
                String point2 = instruction.get(4);
                List<Integer> point1I = Arrays.stream(point1.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                List<Integer> point2I = Arrays.stream(point2.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                for (int x = point1I.get(0); x <= point2I.get(0); x++) {
                    for (int y = point1I.get(1); y <= point2I.get(1); y++) {
                        if (instruction.get(1).equals("off")) {
                            betterGrid[x][y] -= 1;
                            if (betterGrid[x][y] < 0) {
                                betterGrid[x][y] = 0;
                            }
                        } else if (instruction.get(1).equals("on")) {
                            betterGrid[x][y] += 1;
                        }
                    }
                }
            } else if (instruction.get(0).equals("toggle")) {
                String point1 = instruction.get(1);
                String point2 = instruction.get(3);
                List<Integer> point1I = Arrays.stream(point1.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                List<Integer> point2I = Arrays.stream(point2.split(",")).map(str1 -> parseInt(str1)).collect(Collectors.toList());
                for (int x = point1I.get(0); x <= point2I.get(0); x++) {
                    for (int y = point1I.get(1); y <= point2I.get(1); y++) {
                        betterGrid[x][y] += 2;
                    }
                }
            }
        }
        Long count = 0L;
        for (int x = 0; x < MAX_ROW; x++) {
            for (int y = 0; y < MAX_ROW; y++) {
                count += betterGrid[x][y];
            }
        }
        return count;
    }
}
