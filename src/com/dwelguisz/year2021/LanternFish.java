package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day6.School;

import java.util.List;

public class LanternFish extends AoCDay {

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day6/input.txt");
        School school = new School(lines.get(0));
        Long part1 = 0L;
        for(int i = 0; i < 256; i++) {
            if (i == 80) {
                part1 = school.getFishes();
            }
            school.advanceOneDay();
        }
        Long part2 = school.getFishes();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

}
