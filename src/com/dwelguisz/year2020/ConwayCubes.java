package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2020.helper.conwaycube.Board;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConwayCubes extends AoCDay {

    Board board = new Board();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,17,false,0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        board = new Board();
        parseLinesPart2(lines);
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
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
