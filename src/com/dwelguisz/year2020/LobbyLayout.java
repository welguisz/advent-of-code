package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyLayout extends AoCDay {
    Map<String, Coord2D> hexDir;
    Map<Coord2D, Integer> tiles;

    public void solve() {
        hexDir = new HashMap<>();
        hexDir.put("e",new Coord2D(0,1));
        hexDir.put("w", new Coord2D(0,-1));
        hexDir.put("ne", new Coord2D(-1,1));
        hexDir.put("nw", new Coord2D(-1,0));
        hexDir.put("se", new Coord2D(1,0));
        hexDir.put("sw", new Coord2D(1,-1));
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2020/day24/input.txt");
        List<List<String>> movements = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(movements);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    List<List<String>> parseLines(List<String> lines) {
        List<List<String>> movements = new ArrayList<>();
        for (String l : lines) {
            List<String> directions = new ArrayList<>();
            Integer pos = 0;
            List<String> oneChar = List.of("w","e");
            while (pos < l.length()) {
                if (oneChar.contains(l.substring(pos,pos+1))) {
                    directions.add(l.substring(pos,pos+1));
                    pos+=1;
                } else {
                    directions.add(l.substring(pos,pos+2));
                    pos+=2;
                }
            }
            movements.add(directions);
        }
        return movements;
    }

    Coord2D findFinalTile (List<String> directions) {
        Coord2D loc = new Coord2D(0,0);
        for (String dir : directions) {
            loc = loc.add(hexDir.get(dir));
        }
        return loc;
    }

    Integer solutionPart1(List<List<String>> movements) {
        tiles = new HashMap<>();
        for (int i = -100; i < 100; i++) {
            for (int j = -100; j < 100; j++) {
                tiles.put(new Coord2D(i,j),0);
            }
        }
        for (List<String> directions : movements) {
            Coord2D loc = findFinalTile(directions);
            Integer flipNumber = tiles.getOrDefault(loc, 0);
            flipNumber++;
            tiles.put(loc, flipNumber);
        }
        Long value = tiles.entrySet().stream().filter(e -> e.getValue() % 2 == 1).count();
        return value.intValue();
    }

    Integer solutionPart2() {
        for(int i = 0; i < 100; i++) {
            tiles = updateTiles(tiles);
        }
        Long value = tiles.entrySet().stream().filter(e -> e.getValue() % 2 == 1).count();
        return value.intValue();
    }

    Map<Coord2D, Integer> updateTiles(Map<Coord2D, Integer> tiles) {
        Map<Coord2D, Integer> newTiles = new HashMap<>();
        for(Map.Entry<Coord2D, Integer> tile : tiles.entrySet()) {
            Integer blackCount = 0;
            for(Map.Entry<String, Coord2D> dir : hexDir.entrySet()) {
                Integer flipCount = tiles.getOrDefault(tile.getKey().add(dir.getValue()),0);
                if (flipCount %2 == 1) {
                    blackCount++;
                }
            }
            Boolean currentBlack = (tile.getValue() % 2) == 1;
            Integer flipCount = tile.getValue();
            if (currentBlack && ((blackCount == 0) || (blackCount > 2))) {
                flipCount++;
            } else if (!currentBlack && (blackCount == 2)) {
                flipCount++;
            }
            newTiles.put(tile.getKey(), flipCount);
        }
        return newTiles;
    }

}
