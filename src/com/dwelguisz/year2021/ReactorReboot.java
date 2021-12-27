package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day19.Coordinate;
import com.dwelguisz.year2021.helper.day22.Cubiod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReactorReboot extends AoCDay {

    public static Integer MAX_RANGE_PART1 = 50;

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day22/input.txt");
        List<Cubiod> data = parseInput(lines);
        Long part1 = elegantSolution(data, true);
        Long part2 = elegantSolution(data, false);
        System.out.println("--------- Day 22: Reactor Reboot------------");
        System.out.println(String.format("Solution Part1 using Part2: %d",part1));
        System.out.println(String.format("Solution Part2: %d",part2));
    }

    private List<Cubiod> parseInput(List<String> lines){
        List<Cubiod> data = new ArrayList<>();
        for (String line : lines) {
            data.add(new Cubiod(line));
        }
        return data;
    }

    private Long bruteForce(List<Cubiod> data) {
        Map<Coordinate,Boolean> reactor = new HashMap<>();
        for (Cubiod datum : data) {
            if (!((-MAX_RANGE_PART1 <= datum.xMin) && (datum.xMin <= MAX_RANGE_PART1))) {
                break;
            }
            for (Long x = datum.xMin; x < datum.xMax; x++) {
                for (Long y = datum.yMin; y < datum.yMax; y++) {
                    for (Long z = datum.zMin; z < datum.zMax; z++) {
                        reactor.put(new Coordinate(Math.toIntExact(x),Math.toIntExact(y),Math.toIntExact(z)), datum.action);
                    }
                }
            }
        }
        return reactor.entrySet().stream().filter(entry -> entry.getValue()).count();
    }

    private List<Cubiod> mergeTwoArrayLists(List<Cubiod> cubiods1, List<Cubiod> cubiods2) {
        List<Cubiod> newList = new ArrayList<>();
        newList.addAll(cubiods1);
        newList.addAll(cubiods2);
        return newList;
    }

    private Long elegantSolution(List<Cubiod> data, boolean part1) {
        TreeMap<Integer, List<Cubiod>> levels = new TreeMap<>(Collections.reverseOrder());
        for (Cubiod datum : data) {
            if (part1) {
                if (!((-MAX_RANGE_PART1 <= datum.xMin) && (datum.xMin <= MAX_RANGE_PART1))) {
                    break;
                }
            }
            TreeMap<Integer, List<Cubiod>> temp = new TreeMap<>(levels);
            for(Map.Entry<Integer, List<Cubiod>> level : levels.entrySet()) {
                for (Cubiod levelCube : level.getValue()) {
                    if (datum.doesIntersect(levelCube)) {
                        temp.merge(level.getKey()+1,
                                List.of(datum.intersection(levelCube)),
                                (oldValue, newValue) -> mergeTwoArrayLists(oldValue, newValue));
                    }
                }
            }
            levels = temp;
            if(datum.action) {
                levels.merge(0,
                        List.of(datum),
                        (oldValue, newValue) -> mergeTwoArrayLists(oldValue, newValue));
            }
        }

        Long reactorsOn = 0L;
        for (Map.Entry<Integer, List<Cubiod>> level : levels.entrySet()) {
            Long multiplier = (level.getKey() % 2 == 0) ? 1L : -1L;
            reactorsOn += multiplier * level.getValue().stream().map(cubiod -> cubiod.volume()).reduce(0L, Long::sum);
        }
        return reactorsOn;
    }

}
