package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SporificaVirus extends AoCDay {

    public static Coord2D UP = new Coord2D(-1,0);
    public static Coord2D DOWN = new Coord2D(1,0);
    public static Coord2D LEFT = new Coord2D(0,-1);
    public static Coord2D RIGHT = new Coord2D(0, 1);

    public static Map<Pair<Coord2D,String>,Coord2D> TURNS = Map.of(
            Pair.of(UP,"L"),LEFT,Pair.of(UP,"R"),RIGHT,
            Pair.of(RIGHT,"L"),UP,Pair.of(RIGHT,"R"),DOWN,
            Pair.of(DOWN,"L"),RIGHT, Pair.of(DOWN,"R"),LEFT,
            Pair.of(LEFT,"L"),DOWN,Pair.of(LEFT,"R"),UP);
    public static Map<Coord2D, Coord2D> REVERSE = Map.of (
            UP, DOWN,
            DOWN, UP,
            LEFT, RIGHT,
            RIGHT, LEFT
    );


    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2017/day22/input.txt");
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(parseLines(lines));
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(parseLines(lines));
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));

    }

    public Map<Coord2D, String> parseLines(List<String> lines) {
        Integer y = 0;
        Map<Coord2D, String> infection = new HashMap<>();
        for (String l : lines) {
            String[] s = l.split("");
            for (int x = 0; x < s.length; x++) {
                infection.put(new Coord2D(y,x), s[x]);
            }
            y++;
        }
        return infection;
    }

    Integer solutionPart1(Map<Coord2D, String> infection) {
        Integer becomesInfected = 0;
        Integer maxY = infection.keySet().stream().mapToInt(c -> c.x).max().getAsInt();
        Integer maxX = infection.keySet().stream().mapToInt(c -> c.y).max().getAsInt();
        Coord2D currentPos = new Coord2D(maxY/2, maxX/2);
        Coord2D currentDir = UP;
        for (int i = 0; i < 10000; i++) {
            String currentValue = infection.getOrDefault(currentPos, ".");
            if (currentValue.equals("#")) {
                currentDir = TURNS.get(Pair.of(currentDir,"R"));
                infection.put(currentPos,".");
                currentPos = currentPos.add(currentDir);
            } else {
                currentDir = TURNS.get(Pair.of(currentDir, "L"));
                infection.put(currentPos, "#");
                currentPos = currentPos.add(currentDir);
                becomesInfected++;
            }
        }
        return becomesInfected;
    }

    Integer solutionPart2(Map<Coord2D, String> infection) {
        Integer becomesInfected = 0;
        Integer maxY = infection.keySet().stream().mapToInt(c -> c.x).max().getAsInt();
        Integer maxX = infection.keySet().stream().mapToInt(c -> c.y).max().getAsInt();
        Coord2D currentPos = new Coord2D(maxY/2, maxX/2);
        Coord2D currentDir = UP;
        for (int i = 0; i < 10000000; i++) {
            String currentValue = infection.getOrDefault(currentPos, ".");
            if (currentValue.equals("#")) {
                currentDir = TURNS.get(Pair.of(currentDir, "R"));
                infection.put(currentPos, "F");
                currentPos = currentPos.add(currentDir);
            } else if (currentValue.equals("W")) {
                infection.put(currentPos, "#");
                currentPos = currentPos.add(currentDir);
                becomesInfected++;
            } else if (currentValue.equals("F")) {
                currentDir = REVERSE.get(currentDir);
                infection.put(currentPos,".");
                currentPos = currentPos.add(currentDir);
            } else {
                currentDir = TURNS.get(Pair.of(currentDir, "L"));
                infection.put(currentPos, "W");
                currentPos = currentPos.add(currentDir);
            }
        }
        return becomesInfected;
    }

}
