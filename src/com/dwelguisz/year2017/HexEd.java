package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HexEd extends AoCDay {
    Map<String, Coord2D> HEX_DIR = Map.of(
            "n", new Coord2D(0,-1),
            "ne", new Coord2D(1, -1),
            "se", new Coord2D(1,0),
            "s", new Coord2D(0,1),
            "sw", new Coord2D(-1, 1),
            "nw", new Coord2D(-1,0));

    public int hexDistance(int x, int y) {
        return ((Math.abs(x) + Math.abs(y) + Math.abs(x+y))/2);
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,11,false,0);
        List<String> steps = Arrays.stream(lines.get(0).split(",")).toList();
        timeMarkers[1] = Instant.now().toEpochMilli();
        Pair<Integer, Integer> pathInfo = tranversePath(steps);
        part1Answer = pathInfo.getLeft();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = pathInfo.getRight();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Pair<Integer, Integer> tranversePath(List<String> steps) {
        Coord2D currentLoc = new Coord2D(0,0);
        int furthest = 0;
        int dist = 0;
        for (String step : steps) {
            Coord2D value = HEX_DIR.get(step);
            currentLoc = currentLoc.add(value);
            dist = hexDistance(currentLoc.x, currentLoc.y);
            if (dist > furthest) {
                furthest = dist;
            }
        }
        return Pair.of(dist, furthest);
    }
}
