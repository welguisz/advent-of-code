package com.dwelguisz.year2021;


import com.dwelguisz.year2021.helper.day5.Board;

import java.util.List;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;


public class AdventDay05 {

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2021/day5/input.txt");
        Board board1 = new Board(lines, false);
        Board board2 = new Board(lines, true);
        int part1 = board1.avoidSpaces();
        int part2 = board2.avoidSpaces();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

}
