package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dwelguisz.utilities.Grid.createCharGridMap;

public class GardenGroups extends AoCDay {

    static final List<Coord2D> NEIGHBORS = List.of(new Coord2D(-1,0), new Coord2D(0,-1), new Coord2D(0,1), new Coord2D(1,0));

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 12, false, 0);
        Map<Coord2D, Character> plots = createCharGridMap(lines);
        int maxY = lines.get(0).length();
        int maxX = lines.size();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = bothParts(plots, maxX, maxY, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = bothParts(plots, maxX, maxY, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    long bothParts(Map<Coord2D, Character> plots, int maxX, int maxY, boolean part2) {
        Set<Coord2D> seen = new HashSet<>();
        long price = 0;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                if (seen.contains(new Coord2D(x, y))) {
                    continue;
                }
                ArrayDeque<Coord2D> queue = new ArrayDeque<>();
                queue.add(new Coord2D(x, y));
                long area = 0;
                long perimeter = 0;
                Map<Coord2D, Set<Coord2D>> perimeters = new HashMap<>();
                while (!queue.isEmpty()) {
                    Coord2D curr = queue.pollFirst();
                    if (seen.contains(curr)) {
                        continue;
                    }
                    seen.add(curr);
                    area++;
                    for (Coord2D dir : NEIGHBORS) {
                        Coord2D next = curr.add(dir);
                        if (plots.containsKey(next) && (plots.get(curr) == plots.get(next))) {
                            queue.add(next);
                        } else {
                            perimeter++;
                            perimeters.computeIfAbsent(dir, k -> new HashSet<>()).add(curr);
                        }
                    }
                }
                long sides = 0;
                if (part2) {
                    for (Map.Entry<Coord2D, Set<Coord2D>> entry : perimeters.entrySet()) {
                        Set<Coord2D> seenPerimeters = new HashSet<>();
                        for (Coord2D curr : entry.getValue()) {
                            if (seenPerimeters.contains(curr)) {
                                continue;
                            }
                            sides++;
                            ArrayDeque<Coord2D> perimQueue = new ArrayDeque<>();
                            perimQueue.add(curr);
                            while (!perimQueue.isEmpty()) {
                                Coord2D test = perimQueue.pollFirst();
                                if (seenPerimeters.contains(test)) {
                                    continue;
                                }
                                seenPerimeters.add(test);
                                for (Coord2D dir : NEIGHBORS) {
                                    Coord2D next = test.add(dir);
                                    if (entry.getValue().contains(next)) {
                                        perimQueue.add(next);
                                    }
                                }
                            }
                        }
                    }
                }
                price += part2 ? area * sides : area * perimeter;
            }
        }
        return price;
    }
}
