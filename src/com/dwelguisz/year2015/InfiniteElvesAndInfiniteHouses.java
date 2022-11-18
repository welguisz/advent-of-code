package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

public class InfiniteElvesAndInfiniteHouses extends AoCDay {
    public void solve() {
        Integer part1 = solutionPart1(29000000);
        Integer part2 = solutionPart2(29000000);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
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
