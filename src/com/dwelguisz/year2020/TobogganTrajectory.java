package com.dwelguisz.year2020;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class TobogganTrajectory {
    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day03/input.txt");
        Integer[][] sampleMap = createMap(lines);
        Long part1 = solutionPart1(sampleMap);
        Long part2 = solutionPart2(sampleMap);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part1: %d",part2));
    }

    public static Long solutionPart1(Integer[][] map) {
        return checkSlopes(map, 3, 1);
    }

    public static Long solutionPart2(Integer[][] map) {
        Long slope1 = checkSlopes(map, 1,1);
        Long slope2 = checkSlopes(map, 3,1);
        Long slope3 = checkSlopes(map, 5,1);
        Long slope4 = checkSlopes(map, 7,1);
        Long slope5 = checkSlopes(map, 1,2);
        return slope1*slope2*slope3*slope4*slope5;
    }

    public static Long checkSlopes(Integer[][] map, Integer xSlope, Integer ySlope) {
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

    public static Integer[][] createMap(List<String> lines) {
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
