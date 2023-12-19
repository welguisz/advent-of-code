package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day6.School;

import java.time.Instant;
import java.util.List;

public class LanternFish extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021,6,false,0);
        School school = new School(lines.get(0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        for(int i = 0; i < 256; i++) {
            if (i == 80) {
                part1Answer = school.getFishes();
                timeMarkers[2] = Instant.now().toEpochMilli();
            }
            school.advanceOneDay();
        }
        part2Answer = school.getFishes();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

}
