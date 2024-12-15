package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.dwelguisz.utilities.Grid.createCharGridMap;
import static com.dwelguisz.utilities.Grid.getStartingPoint;

public class WarehouseWoes extends AoCDay {

    Map<Coord2D, Character> grid = new HashMap<>();
    List<Character> moves = new ArrayList<>();
    Map<Character, Coord2D> MOVESMAP = Map.of('^', new Coord2D(-1, 0),
            '>', new Coord2D(0, 1), 'v', new Coord2D(1, 0),
            '<', new Coord2D(0, -1));

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 15, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        parseLines(lines);
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        parseLinesPart2(lines);
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void parseLines(List<String> lines) {
        boolean map = true;
        List<String> gridLines = new ArrayList<>();
        for (String line : lines) {
            if (map) {
                gridLines.add(line);
            } else {
                moves.addAll(Arrays.stream(line.split("")).map(l -> l.toCharArray()[0]).toList());
            }
            if (line.length() == 0) {
                map = false;
            }
        }
        grid = createCharGridMap(gridLines);
    }

    void parseLinesPart2(List<String> lines) {
        boolean map = true;
        List<String> gridLines = new ArrayList<>();
        for (String line : lines) {
            if (map) {
                StringBuffer buffer = new StringBuffer();
                for(char c : line.toCharArray()) {
                    if (c == 'O') {
                        buffer.append("[]");
                    } else if (c == '.') {
                        buffer.append("..");
                    } else if (c == '#') {
                        buffer.append("##");
                    } else if (c == '@') {
                        buffer.append("@.");
                    }
                }
                gridLines.add(buffer.toString());
            }
            if (line.length() == 0) {
                map = false;
            }
        }
        grid = createCharGridMap(gridLines);
    }

    List<Character> MOVABLE = List.of('O', '[', ']');

    long solutionPart1() {
        Coord2D location = getStartingPoint(grid, '@');
        for (Character move : moves) {
            Coord2D next = location.add(MOVESMAP.get(move));
            if (grid.get(next) == '.') {
                grid.put(next, '@');
                grid.put(location, '.');
                location = next;
            } else if (MOVABLE.contains(grid.get(next))) {
                Stack<Coord2D> warehouseMoves = new Stack<>();
                Coord2D curr = next.add(new Coord2D(0, 0));
                while (MOVABLE.contains(grid.get(curr))) {
                    warehouseMoves.push(curr);
                    curr = curr.add(MOVESMAP.get(move));
                }
                if (grid.get(curr) == '.') {
                    while (!warehouseMoves.empty()) {
                        grid.put(curr, 'O');
                        curr = warehouseMoves.pop();
                    }
                    grid.put(next, '@');
                    grid.put(location, '.');
                    location = next;
                }
            }
        }
        return grid.entrySet().stream()
                .filter(e -> e.getValue() == 'O')
                .map(e -> GPSValue(e.getKey()))
                .reduce(0L, Long::sum);
    }

    long GPSValue(Coord2D location) {
        return 100*location.x + location.y;
    }

    Coord2D movePart2(Coord2D location, Coord2D delta) {
        ArrayDeque<Coord2D> possibleMoves = new ArrayDeque<>();
        possibleMoves.add(location.add(delta));
        Map<Coord2D, Character> touchedCells = new HashMap<>();
        touchedCells.put(location, '@');
        while (!possibleMoves.isEmpty()) {
            Coord2D curr = possibleMoves.pop();
            if (touchedCells.containsKey(curr)) {
                continue;
            }
            touchedCells.put(curr, grid.get(curr));
            if (grid.get(curr) == '#') {
                return location;
            } else if (grid.get(curr) == '[') {
                possibleMoves.add(curr.add(delta));
                possibleMoves.add(curr.add(new Coord2D(0,1)));
            } else if (grid.get(curr) == ']') {
                possibleMoves.add(curr.add(delta));
                possibleMoves.add(curr.add(new Coord2D(0,-1)));
            }
        }
        touchedCells.keySet().stream().forEach(k -> grid.put(k, '.'));
        touchedCells.entrySet().stream()
                .filter(e -> e.getValue() != '.')
                .forEach(e -> grid.put(e.getKey().add(delta), e.getValue()));
        return location.add(delta);
    }

    long solutionPart2() {
        Coord2D location = getStartingPoint(grid, '@');
        System.out.println("--------------------");
        for (Character move : moves) {
            location = movePart2(location, MOVESMAP.get(move));
        }
        return grid.entrySet().stream()
                .filter(e -> e.getValue() == '[')
                .map(e -> GPSValue(e.getKey()))
                .reduce(0L, Long::sum);
    }

}
