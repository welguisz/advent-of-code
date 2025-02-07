package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class TobogganTrajectory extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,3,false,0);
        Integer[][] sampleMap = createMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(sampleMap);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(sampleMap);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Long solutionPart1(Integer[][] map) {
        return checkSlopes(map, 3, 1);
    }

    private Long solutionPart2(Integer[][] map) {
        Long slope1 = checkSlopes(map, 1,1);
        Long slope2 = checkSlopes(map, 3,1);
        Long slope3 = checkSlopes(map, 5,1);
        Long slope4 = checkSlopes(map, 7,1);
        Long slope5 = checkSlopes(map, 1,2);
        return slope1*slope2*slope3*slope4*slope5;
    }

    private Long checkSlopes(Integer[][] map, Integer xSlope, Integer ySlope) {
        Long treesHit = 0L;
        Integer x = 0;
        Integer y = 0;
        while (y < map.length-1) {
            x += xSlope;
            y += ySlope;
            if (x >= map[0].length) {
                x -= map[0].length;
            }
            treesHit += map[y][x];
        }
        return treesHit;
    }

    private Integer[][] createMap(List<String> lines) {
        Map<Character, Integer> treeValues = Map.of('.',0,'#',1);
        Integer[][] map = new Integer[lines.size()][];
        for (int y = 0; y < lines.size(); y++) {
             List<Integer> tmp = lines.get(y).chars()
                    .mapToObj(c -> (char) c)
                    .map(treeValues::get)
                    .collect(Collectors.toList());
             Object[] tmpAr = tmp.toArray();
             Integer[] tmpInt = new Integer[tmpAr.length];
             for (int i = 0; i < tmpAr.length; i++) {
                 tmpInt[i] = parseInt(tmpAr[i].toString());
             }
             map[y] = tmpInt;
        }
        return map;
    }
}
