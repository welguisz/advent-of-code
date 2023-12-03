package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GearRatios extends AoCDay {

    Map<Coord2D, List<Integer>> gears;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 3, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Long solutionPart1(List<String> lines) {
        char[][] grid = convertToCharGrid(lines);
        Long total = 0L;
        gears = new HashMap<>();
        for (int row = 0; row < grid.length; row++) {
            Set<Coord2D> nums = new HashSet<>();
            Integer num = 0;
            boolean hasPart = false;
            for(int col = 0; col < grid[row].length+1; col++) {
                if (row < grid.length && col < grid[row].length && Character.isDigit(grid[row][col])) {
                    num = num * 10 + Integer.parseInt(""+grid[row][col]);
                    for (int rr = -1; rr <= 1; rr++) {
                        for (int cc = -1; cc <= 1; cc++) {
                            if (0<=row+rr && row+rr<lines.size() && 0<=col+cc && col+cc<lines.get(row).length()) {
                                char ch = grid[row+rr][col+cc];
                                if ((!Character.isDigit(ch)) && (ch != '.')) {
                                    hasPart = true;
                                }
                                if (ch == '*') {
                                    nums.add(new Coord2D(row+rr,col+cc));
                                }
                            }
                        }
                    }
                } else if (num > 0) {
                    for (Coord2D n : nums) {
                        List<Integer> tmp = gears.getOrDefault(n, new ArrayList<>());
                        tmp.add(num);
                        gears.put(n, tmp);
                    }
                    if (hasPart) {
                        total += num;
                    }
                    num = 0;
                    hasPart = false;
                    nums = new HashSet<>();
                }
            }
        }
        return total;
    }

    public Long solutionPart2() {
        return gears.values().stream()
                .filter(v -> v.size()==2)
                .mapToLong(v -> v.get(0) * v.get(1))
                .sum();
    }

}
