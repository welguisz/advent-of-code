package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day12.Cave;
import com.dwelguisz.year2021.helper.day12.CavePath;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassagePathing extends AoCDay {

    private HashMap<String, Cave> caveMap;

    public PassagePathing() {
        super();
        caveMap = new HashMap<>();
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021,12,false,0);
        createMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solution(false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solution(true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void createMap(List<String> lines) {
        for (String line:lines) {
            String[] points = line.split("-");
            Cave point1 = caveMap.computeIfAbsent(points[0], k -> new Cave(k));
            Cave point2 = caveMap.computeIfAbsent(points[1], k -> new Cave(k));
            point1.insertCave(point2);
            point2.insertCave(point1);
        }
    }

    private Integer solution(boolean part2) {
        Cave startCave = caveMap.get("start");
        CavePath initialPath = new CavePath(new ArrayList<>(), new HashMap<>());
        initialPath.pathList.add(startCave);
        initialPath.visited.put(startCave, 1);
        List<CavePath> paths = createPaths(startCave, initialPath, part2);
        int smallPathCount = 0;
        for (CavePath path : paths) {
            for (Cave cave : path.visited.keySet()) {
                if (cave.isSmallCove) {
                    smallPathCount += 1;
                    break;
                }
            }
        }
        return smallPathCount;
    }

    private List<CavePath> createPaths(Cave currentCave, CavePath path, boolean part2) {
        List<CavePath> addPaths = new ArrayList<>();
        for (final Cave cave : currentCave.getOtherCaves()) {
            boolean containsSmallCaveTwice = false;
            if (part2) {
                for (Cave visitedCave : path.visited.keySet()) {
                    if ((visitedCave.isSmallCove) && (path.visited.get(visitedCave) > 1)) {
                        containsSmallCaveTwice = true;
                        break;
                    }
                }
            }
            if ((!part2 && (!cave.isSmallCove || !path.visited.containsKey(cave))) ||
                    (part2 && (!cave.isSmallCove || !path.visited.containsKey(cave) || !containsSmallCaveTwice) && (!cave.getCaveName().equals("start")))){
                final CavePath newCavePath = path.duplicate();
                newCavePath.pathList.add(cave);
                newCavePath.visited.put(cave, newCavePath.visited.getOrDefault(cave, 0) + 1);
                if (!cave.getCaveName().equals("end")) {
                    addPaths.addAll(createPaths(cave, newCavePath, part2));
                } else {
                    addPaths.add(newCavePath);
                }
            }
        }
        return addPaths;
    }
}
