package com.dwelguisz.year2021;


import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day5.Board;

import java.time.Instant;
import java.util.List;


public class HydrothermalVenture extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021,5,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        Board board1 = new Board(lines, false);
        part1Answer = board1.avoidSpaces();
        timeMarkers[2] = Instant.now().toEpochMilli();
        Board board2 = new Board(lines, true);
        part2Answer = board2.avoidSpaces();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

}
