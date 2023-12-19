package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Dive extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2021,2,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = calculateLocation(instructions);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = calculateLocationWithAim(instructions);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Integer calculateLocation(List<String> instructions) {
        int horizontal = 0;
        int depth = 0;
        for(int i = 0; i < instructions.size(); i++) {
            String[] parseInstruction = instructions.get(i).split(" ");
            int step = parseInt(parseInstruction[1]);
            if ("forward".equals(parseInstruction[0])) {
                horizontal += step;
            } else if ("down".equals(parseInstruction[0])) {
                depth += step;
            } else if ("up".equals(parseInstruction[0])) {
                depth -= step;
            }
        }
        return horizontal*depth;
    }


    private Integer calculateLocationWithAim(List<String> instructions) {
        int horizontal = 0;
        int depth = 0;
        int aim = 0;
        for(int i = 0; i < instructions.size(); i++) {
            String[] parseInstruction = instructions.get(i).split(" ");
            int step = parseInt(parseInstruction[1]);
            if ("forward".equals(parseInstruction[0])) {
                horizontal += step;
                depth = depth + (step*aim);
            } else if ("down".equals(parseInstruction[0])) {
                aim += step;
            } else if ("up".equals(parseInstruction[0])) {
                aim -= step;
            }
        }
        return horizontal*depth;
    }

}
