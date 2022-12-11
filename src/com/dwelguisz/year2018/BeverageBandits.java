package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class BeverageBandits extends AoCDay {

    public static class Fighter {
        Integer x;
        Integer y;
        Integer hitPoints;
        Boolean type;

        public Fighter(Integer x, Integer y, Boolean type) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.hitPoints = 200;
        }

        public Integer calculateScore() {
            return this.y * 10000 + x;
        }

        public List<Fighter> findClosest() {
            return new ArrayList<>();
        }

    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day14/input.txt");

    }
}
