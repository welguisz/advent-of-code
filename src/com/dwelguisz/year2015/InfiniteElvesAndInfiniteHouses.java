package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class InfiniteElvesAndInfiniteHouses extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,20,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        int num = Integer.parseInt(instructions.get(0));
        part1Answer = solutionPart1(num);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(num);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(Integer minimumPresent) {
        final int MAX = 1000000;
        int[] houses = new int [MAX];
        for (int elf = 1; elf < MAX; elf++) {
            for (int visited = elf; visited < MAX; visited += elf) {
                houses[visited] += elf * 10;
            }
        }
        for (int i = 0; i < MAX; i++) {
            if (houses[i] >= minimumPresent) {
                return i;
            }
        }
        return 0;
    }

    public Integer solutionPart2(Integer minimumPresent) {
        final int MAX = 1000000;
        int[] houses = new int [MAX];
        for (int elf = 1; elf < MAX; elf++) {
            for (int visited = elf; (visited <= elf*50 && visited < MAX); visited += elf) {
                houses[visited] += elf * 11;
            }
        }
        for (int i = 0; i < MAX; i++) {
            if (houses[i] >= minimumPresent) {
                return i;
            }
        }
        return 0;
    }

}
