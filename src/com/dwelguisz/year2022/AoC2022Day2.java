package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.util.List;

public class AoC2022Day2 extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day02/input.txt");
        Integer part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines) {
        Integer score = 0;
        for (String line:lines) {
            String c[] = line.split(" ");
            if (c[1].equals("X")) {
                score += 1;
                if (c[0].equals("A")) {
                    score += 3;
                } else if (c[0].equals("C")) {
                    score += 6;
                }
            } else if (c[1].equals("Y")) {
                score += 2;
                if (c[0].equals("B")) {
                    score += 3;
                } else if (c[0].equals("A")) {
                    score += 6;
                }
            } else if (c[1].equals("Z")) {
                score += 3;
                if (c[0].equals("C")) {
                    score += 3;
                } else if (c[0].equals("B")) {
                    score += 6;
                }
            }

        }
        return score;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer score = 0;
        for (String line:lines) {
            String c[] = line.split(" ");
            if (c[0].equals("A")) {
                if (c[1].equals("X")) {
                    score += 3;
                } else if (c[1].equals("Y")) {
                    score += 4;
                } else {
                    score += 8;
                }
            } else if (c[0].equals("B")) {
                if (c[1].equals("X")) {
                    score += 1;
                } else if (c[1].equals("Y")) {
                    score += 5;
                } else {
                    score += 9;
                }
            } else {
                if (c[1].equals("X")) {
                    score += 2;
                } else if (c[1].equals("Y")) {
                    score += 6;
                } else {
                    score += 7;
                }

            }

        }
        return score;
    }
}
