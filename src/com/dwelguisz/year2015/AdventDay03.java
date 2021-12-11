package com.dwelguisz.year2015;

import java.util.Arrays;
import java.util.List;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class AdventDay03 {
    static Integer MAX_ROW = 400;
    static Boolean[][] visited = new Boolean[MAX_ROW][MAX_ROW];
    static Boolean[][] visitedPart2 = new Boolean[MAX_ROW][MAX_ROW];
    static {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_ROW; j++) {
                visited[i][j] = false;
                visitedPart2[i][j] = false;
            }
        }
    }

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2015/day03/input.txt");
        Long part1 = solutionPart1(Arrays.asList(instructions.get(0).split("")));
        Long part2 = solutionPart2(Arrays.asList(instructions.get(0).split("")));
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    static Long solutionPart1(List<String> instructions) {
        Integer santaX = 200;
        Integer santaY = 200;
        Long total = 0L;
        for (String in : instructions) {
            if (!visited[santaX][santaY]) {
                total += 1;
                visited[santaX][santaY] = true;
            }
            if ("^".equals(in)) {
                santaY--;
            } else if ("v".equals(in)) {
                santaY++;
            } else if ("<".equals(in)) {
                santaX--;
            } else if (">".equals(in)) {
                santaX++;
            }
        }
        if (!visited[santaX][santaY]) {
            total += 1;
            visited[santaX][santaY] = true;
        }
        return total;
    }

    static Long solutionPart2(List<String> instructions) {
        Integer santaX = 200;
        Integer santaY = 200;
        Integer roboX = 200;
        Integer roboY = 200;
        boolean  step = false; //false means Santa info; true means Robo Santa info
        Long total = 0L;
        for (String in : instructions) {
            int tempX = step ? roboX : santaX;
            int tempY = step ? roboY : santaY;
            if (!visitedPart2[tempX][tempY])  {
                total += 1;
                visitedPart2[tempX][tempY] = true;
            }
            if ("^".equals(in)) {
                tempY--;
            } else if ("v".equals(in)) {
                tempY++;
            } else if ("<".equals(in)) {
                tempX--;
            } else if (">".equals(in)) {
                tempX++;
            }
            if (!visitedPart2[tempX][tempY]) {
                total += 1;
                visitedPart2[tempX][tempY] = true;
            }
            if (!step) {
                santaX = tempX;
                santaY = tempY;
            } else {
                roboX = tempX;
                roboY = tempY;
            }
            step ^= true;
        }
        int tempX = step ? roboX : santaX;
        int tempY = step ? roboY : santaY;
        if (!visitedPart2[tempX][tempY]) {
            total += 1;
            visitedPart2[santaX][santaY] = true;
        }
        return total;
    }


}
