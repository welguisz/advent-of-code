package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
        Map<Character, List<Coord2D>> gardenGroups = createGardenGroup(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = calculatePrice(gardenGroups, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = calculatePrice(gardenGroups, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Map<Character, List<Coord2D>> createGardenGroup(List<String> lines) {
        Map<Character, List<Coord2D>> gardenGroups = new HashMap<>();
        int x = 0;
        for (String line : lines) {
            for (int y = 0; y < line.length(); y++) {
                gardenGroups.computeIfAbsent(line.charAt(y), c -> new ArrayList<>()).add(new Coord2D(x, y));
            }
            x++;
        }
        return gardenGroups;
    }

    long calculatePrice(Map<Character, List<Coord2D>> groups, boolean part2) {
        return groups.entrySet().stream().map(e -> calculatePricePerGroup(e.getKey(), e.getValue(), part2))
                .mapToLong(l -> l).sum();
    }

    long calculatePricePerGroup(Character c, List<Coord2D> group, boolean part2) {
        long total = 0L;
        Set<Coord2D> seen = new HashSet<>();
        for (Coord2D currentLoc : group) {
            if (seen.contains(currentLoc)) {
                continue;
            }
            ArrayDeque<Coord2D> queue = new ArrayDeque<>();
            queue.add(currentLoc);
            long perimeter = 0;
            long area = 0;
            Map<Coord2D, Set<Coord2D>> perimeters = new HashMap<>();
            while (!queue.isEmpty()) {
                Coord2D current = queue.pollFirst();
                if (seen.contains(current)) {
                    continue;
                }
                seen.add(current);
                area++;
                for (Coord2D dir : NEIGHBORS) {
                    Coord2D next = current.add(dir);
                    if (group.contains(next)) {
                        queue.add(next);
                    } else {
                        perimeter++;
                        perimeters.computeIfAbsent(dir, k -> new HashSet<>()).add(current);
                    }
                }
            }
            long sides = findSides(perimeters);
            long multiplier = part2 ? sides : perimeter;
            total += area * multiplier;
        }
        return total;
    }

    long findSides(Map<Coord2D, Set<Coord2D>> perimeter) {
        long sides = 0;
        for (Map.Entry<Coord2D, Set<Coord2D>> entry : perimeter.entrySet()) {
            Set<Coord2D> seen = new HashSet<>();
            for (Coord2D current : entry.getValue()) {
                if (seen.contains(current)) {
                    continue;
                }
                sides++;
                ArrayDeque<Coord2D> queue = new ArrayDeque<>();
                queue.add(current);
                while (!queue.isEmpty()) {
                    Coord2D test = queue.pollFirst();
                    if (seen.contains(test)) {
                        continue;
                    }
                    seen.add(test);
                    for (Coord2D dir : NEIGHBORS) {
                        Coord2D next = test.add(dir);
                        if (entry.getValue().contains(next)) {
                            queue.add(next);
                        }
                    }
                }
            }
        }
        return sides;
    }
}
