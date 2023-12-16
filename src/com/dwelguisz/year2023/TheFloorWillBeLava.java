package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TheFloorWillBeLava extends AoCDay {

    Coord2D UP = new Coord2D(-1,0);
    Coord2D RIGHT = new Coord2D(0,1);
    Coord2D DOWN = new Coord2D(1,0);
    Coord2D LEFT = new Coord2D(0,-1);

    List<Character> MIRRORS = List.of('\\', '/');
    List<Character> SPLITTER = List.of('|', '-');
    List<Coord2D> SPLIT_VERTICAL = List.of(RIGHT, LEFT);
    List<Coord2D> SPLIT_HORIZONTAL = List.of(UP, DOWN);
    Map<Pair<Coord2D,Character>,Coord2D> MIRROR_TRANSFORM = Map.of(
            Pair.of(RIGHT,'\\'),DOWN,
            Pair.of(RIGHT,'/'),UP,
            Pair.of(UP,'\\'),LEFT,
            Pair.of(UP,'/'),RIGHT,
            Pair.of(LEFT,'\\'),UP,
            Pair.of(LEFT,'/'),DOWN,
            Pair.of(DOWN,'\\'),RIGHT,
            Pair.of(DOWN,'/'),LEFT
    );


    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 16, false, 0);
        char[][] grid = convertToCharGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(char[][] grid) {
        return energizeGrid(grid, new Coord2D(0,0), RIGHT);
    }


    Stream<Pair<Coord2D,Coord2D>> nextSplitter(Coord2D loc, Coord2D dir, char val) {
        if (val == '-' && SPLIT_HORIZONTAL.contains(dir)) {
            return SPLIT_VERTICAL.stream().map(d -> Pair.of(loc.add(d),d));
        } else if (val == '|' && SPLIT_VERTICAL.contains(dir)) {
            return SPLIT_HORIZONTAL.stream().map(d -> Pair.of(loc.add(d),d));
        }
        return Stream.of(Pair.of(loc.add(dir),dir));
    }

    boolean filterBadLocs(char[][] grid, Coord2D loc) {
        Integer r = loc.x;
        Integer c = loc.y;
        return (0 <= r) && (r < grid.length) && (0 <= c) && (c < grid[0].length);
    }
    public Stream<Pair<Coord2D,Coord2D>> nextSpots(char[][] grid, Pair<Coord2D, Coord2D> state, Set<Pair<Coord2D,Coord2D>> seen) {
        Integer r = state.getLeft().x;
        Integer c = state.getLeft().y;
        if (filterBadLocs(grid, state.getLeft())) {
            char val = grid[r][c];
            if (SPLITTER.contains(val)) {
                return nextSplitter(state.getLeft(), state.getRight(), val).filter(s -> !seen.contains(s));
            } else {
                Pair<Coord2D, Coord2D> next = Pair.of(state.getLeft(), state.getRight());
                if (val == '.') {
                    next = Pair.of(state.getLeft().add(state.getRight()),state.getRight());
                } else if (MIRRORS.contains(val)) {
                    Coord2D newDir = MIRROR_TRANSFORM.get(Pair.of(state.getRight(), val));
                    next = Pair.of(state.getLeft().add(newDir), newDir);
                }
                if (!seen.contains(next)) {
                    return Stream.of(next);
                }
            }
        }
        return Stream.of();
    }
    public Long energizeGrid(
            char[][] grid, Coord2D current, Coord2D direction
    ) {
        Set<Pair<Coord2D,Coord2D>> states = new HashSet<>();
        states.add(Pair.of(current, direction));
        Set<Pair<Coord2D,Coord2D>> touched = new HashSet<>();
        while (!states.isEmpty()) {
            touched.addAll(states);
            states = states.stream().flatMap(s -> nextSpots(grid, s, touched))
                    .filter(p -> filterBadLocs(grid, p.getLeft()))
                    .collect(Collectors.toSet());
        }
        return touched.stream().map(s -> s.getLeft()).distinct().count();
    }


    public Long solutionPart2(char[][] grid) {
        Long maxValue = Long.MIN_VALUE;
        for (int r = 0; r < grid.length; r++) {
            maxValue = Long.max(maxValue, energizeGrid(grid, new Coord2D(r,0),RIGHT));
            maxValue = Long.max(maxValue, energizeGrid(grid, new Coord2D(r,grid[0].length-1), LEFT));
        }
        for (int c = 0; c < grid[0].length; c++) {
            maxValue = Long.max(maxValue, energizeGrid(grid, new Coord2D(0,c),DOWN));
            maxValue = Long.max(maxValue, energizeGrid(grid, new Coord2D(grid.length-1,c),UP));
        }
        return maxValue;
    }
}
