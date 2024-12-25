package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dwelguisz.utilities.Grid.createCharGridMap;

public class CodeChronicle extends AoCDay {

    List<Map<Coord2D, Character>> keys;
    List<Map<Coord2D, Character>> locks;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 25, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    boolean isLock(Map<Coord2D, Character> vals) {
        return vals.entrySet().stream()
                .filter(e -> e.getKey().x == 0)
                .allMatch(e -> e.getValue() == '#');
    }

    void addToLists(List<String> temp) {
        Map<Coord2D, Character> vals = createCharGridMap(temp);
        if (isLock(vals)) {
            locks.add(vals);
        } else {
            keys.add(vals);
        }
    }

    void parseLines(List<String> lines) {
        keys = new ArrayList<>();
        locks = new ArrayList<>();

        List<String> temp = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                addToLists(temp);
                temp.clear();
            } else {
                temp.add(line);
            }
        }
        addToLists(temp);
    }

    boolean keyFits(Map<Coord2D, Character> key, Map<Coord2D, Character> lock) {
        int maxX = key.keySet().stream().mapToInt(c -> c.x).max().getAsInt();
        int maxY = key.keySet().stream().mapToInt(c -> c.y).max().getAsInt();
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                if ((key.get(new Coord2D(x, y)) == '#') && (lock.get(new Coord2D(x, y)) == '#')) {
                    return false;
                }
            }
        }
        return true;
    }

    long solutionPart1() {
        long answer = 0;
        for (Map<Coord2D,Character> key : keys) {
            for(Map<Coord2D,Character> lock : locks) {
                answer += keyFits(key, lock) ? 1 : 0;
            }
        }
        return answer;
    }

    String solutionPart2() {
        return "Press link to complete.";
    }
}
