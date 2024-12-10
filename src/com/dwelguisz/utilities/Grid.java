package com.dwelguisz.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {

    public static List<Coord2D> DIRECTIONS = List.of(
            new Coord2D(-1,-0),
            new Coord2D(0, 1),
            new Coord2D(1, 0),
            new Coord2D(0, -1))
            ;


    public static char[][] createCharGrid(List<String> lines) {
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        int i = 0;
        for(String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j);
            }
            i++;
        }
        return grid;
    }

    public static Map<Coord2D, Character> createCharGridMap(List<String> lines) {
        Map<Coord2D, Character> grid = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                grid.put(new Coord2D(i, j), lines.get(i).charAt(j));
            }
        }
        return grid;
    }

    public static Map<Coord2D, Integer> createIntegerGridMap(List<String> lines) {
        Map<Coord2D, Integer> grid = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                grid.put(new Coord2D(i, j), Integer.parseInt(""+lines.get(i).charAt(j)));
            }
        }
        return grid;
    }


    public static Coord2D getStartingPoint(Map<Coord2D, Character> grid, char startingSymbol) {
        return grid.entrySet().stream()
                .filter(entry -> entry.getValue() == startingSymbol)
                .findFirst().get().getKey();
    }

    public static List<Coord2D> getStartingPoints (Map<Coord2D, Integer> grid, Integer startingValue) {
        return grid.entrySet().stream()
                .filter(entry -> entry.getValue() == startingValue)
                .map(entry -> entry.getKey())
                .toList();
    }

}
