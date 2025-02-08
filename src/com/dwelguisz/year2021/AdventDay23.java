package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class AdventDay23 extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2021,23,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = "To work on";
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "To work on";
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

}
