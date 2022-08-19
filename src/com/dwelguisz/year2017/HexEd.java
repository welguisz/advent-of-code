package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HexEd extends AoCDay {
    Map<String, Pair<Integer, Integer>> hexDir;

    public int hexDistance(int x, int y) {
        return ((Math.abs(x) + Math.abs(y) + Math.abs(x+y))/2);
    }

    public void solve() {
        hexDir = new HashMap<>();
        hexDir.put("n",Pair.of(0,-1));
        hexDir.put("ne", Pair.of(1,-1));
        hexDir.put("se", Pair.of(1,0));
        hexDir.put("s", Pair.of(0,1));
        hexDir.put("sw", Pair.of(-1,1));
        hexDir.put("nw", Pair.of(-1,0));
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day11/input.txt");
        List<String> steps = Arrays.stream(lines.get(0).split(",")).collect(Collectors.toList());
        int x = 0;
        int y = 0;
        int furthest = 0;
        int dist = 0;
        for (String step : steps) {
            Pair<Integer, Integer> value = hexDir.get(step);
            x += value.getLeft();
            y += value.getRight();
            dist = hexDistance(x,y);
            if (dist > furthest) {
                furthest = dist;
            }
        }
        System.out.println(String.format("Part 1 Answer: %d",dist));
        System.out.println(String.format("Part 2 Answer: %d",furthest));


    }
}
