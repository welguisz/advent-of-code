package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrintQueue extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 5, false, 0);
        List<Coord2D> pageOrder = parsePart1(lines);
        List<List<Integer>> safetyManuals = parsePart2(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(pageOrder, safetyManuals);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(pageOrder, safetyManuals);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Coord2D> parsePart1(List<String> lines) {
        List<Coord2D> pageOrder = new ArrayList<>();
        boolean inPart1 = true;
        int lineNum = 0;
        while (inPart1) {
            if (lines.get(lineNum).equals("")) {
                inPart1 = false;
                continue;
            }
            String[] split = lines.get(lineNum).split("\\|");
            pageOrder.add(new Coord2D(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            lineNum++;
        }
        return pageOrder;
    }

    List<List<Integer>> parsePart2(List<String> lines) {
        List<List<Integer>> safetyManuals = new ArrayList<>();
        boolean inPart1 = true;
        int lineNum = 0;
        while (inPart1) {
            if (lines.get(lineNum).equals("")) {
                inPart1 = false;
                lineNum++;
                continue;
            }
            lineNum++;
        }
        for (int i = lineNum; i < lines.size(); i++) {
            safetyManuals.add(Arrays.stream(lines.get(i).split(",")).map(Integer::parseInt).toList());
        }
        return safetyManuals;
    }

    long solutionPart1(List<Coord2D> pageOrder, List<List<Integer>> safetyManuals) {
        long sum = 0l;
        for(List<Integer> safety : safetyManuals) {
            if (validManual(pageOrder, safety)) {
                int middlepage = safety.size()/2;
                sum += safety.get(middlepage);
            }
        }

        return sum;
    }

    boolean validManual(List<Coord2D> pageOrder, List<Integer> manual) {
        for(Coord2D coord : pageOrder) {
            if (new HashSet<>(manual).containsAll(List.of(coord.x, coord.y))) {
                int leftPos = manual.indexOf(coord.x);
                int rightPos = manual.indexOf(coord.y);
                if (leftPos > rightPos) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean validManual(List<Coord2D> pageOrder, Map<Integer, Integer> manual) {
        for(Coord2D coord : pageOrder) {
            if (manual.containsKey(coord.x) && manual.containsKey(coord.y)) {
                if (manual.get(coord.x) > manual.get(coord.y)) {
                    return false;
                }
            }
        }
        return true;
    }


    long solutionPart2(List<Coord2D> pageOrder, List<List<Integer>> safetyManuals) {
        long sum = 0l;
        for(List<Integer> safety : safetyManuals) {
            if (!validManual(pageOrder, safety)) {
                List<Integer> newSafetyManual = fixSafetyManual(pageOrder, safety);
                int middlepage = newSafetyManual.size()/2;
                sum += newSafetyManual.get(middlepage);
            }
        }
        return sum;
    }

    List<Integer> fixSafetyManual(List<Coord2D> pageOrder, List<Integer> badManual) {
        Map<Integer,Integer> placements = badManual.stream().collect(Collectors.toMap(k -> k, badManual::indexOf));
        while (!validManual(pageOrder, placements)) {
            for (Coord2D coord : pageOrder) {
                if (placements.containsKey(coord.x) && placements.containsKey(coord.y)) {
                    int leftPos = placements.get(coord.x);
                    int rightPos = placements.get(coord.y);
                    if (leftPos > rightPos) {
                        placements.put(coord.x, rightPos);
                        placements.put(coord.y, leftPos);
                    }
                }
            }
        }
        Integer[] fixedManual = new Integer[placements.size()];
        placements.forEach((key, value) -> fixedManual[value] = key);
        return Arrays.stream(fixedManual).toList();
    }

}
