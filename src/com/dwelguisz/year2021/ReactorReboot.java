package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day19.Coordinate;
import com.dwelguisz.year2021.helper.day22.Cubiod;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReactorReboot extends AoCDay {

    public static Integer MAX_RANGE_PART1 = 50;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021,22,false,0);
        List<Cubiod> data = parseInput(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = elegantSolution(data, true);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = elegantSolution(data, false);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
