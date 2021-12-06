package com.dwelguisz.year2021.helper.day6;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class School {
    private Long[] fishes;

    private static int SPAWN_TIME = 7;

    public School(String counts) {
        fishes = new Long[SPAWN_TIME+3];
        Arrays.fill(fishes, 0L);
        List<Integer> numbers = Arrays.stream(counts.split(",")).map(str -> parseInt(str)).collect(Collectors.toList());
        for (Integer num : numbers) {
            fishes[num]++;
        }
    }

    public void advanceOneDay() {

        Long newPosition8 = fishes[0];
        for(int i = 0; i <= SPAWN_TIME+1; i++) {
            fishes[i] = fishes[i+1];
        }
        fishes[SPAWN_TIME+1] = newPosition8;
        fishes[SPAWN_TIME-1] += newPosition8;
    }

    public Long getFishes() {
        Long sum = 0L;
        for (int i = 0; i <= SPAWN_TIME+1;i++) {
            sum += fishes[i];
        }
        return sum;
    }
}
