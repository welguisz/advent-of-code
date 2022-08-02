package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2020.helper.ConwayCubeBoard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConwayCubes extends AoCDay {

    ConwayCubeBoard board = new ConwayCubeBoard();

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2020/day17/input.txt");
        parseLines(lines);
        Long part1 = solutionPart1();
        board = new ConwayCubeBoard();
        parseLinesPart2(lines);
        Long part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    private void parseLines(List<String> lines) {
        int y = 0;
        for (String line : lines) {
            int x = 0;
            for (String val : line.split("")) {
                if ("#".equals(val)) {
                    Integer ints[] = {x,y,0};
                    board.add(Arrays.stream(ints).collect(Collectors.toList()));
                }
                x++;
            }
            y++;
        }
    }

    private void parseLinesPart2(List<String> lines) {
        int y = 0;
        for (String line : lines) {
            int x = 0;
            for (String val : line.split("")) {
                if ("#".equals(val)) {
                    Integer ints[] = {x,y,0, 0};
                    board.add(Arrays.stream(ints).collect(Collectors.toList()));
                }
                x++;
            }
            y++;
        }
    }


    private Long solutionPart1() {
        for (int i = 0; i < 6; i++) {
            board = board.step();
        }
        return 0L + board.size();
    }

    private Long solutionPart2() {
        for (int i = 0; i < 6; i++) {
            board = board.stepPart2();
        }
        return 0L + board.size();
    }

}
