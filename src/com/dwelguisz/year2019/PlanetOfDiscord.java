package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlanetOfDiscord extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,24,false,0);
        Set<Coord2D> initialState = getInitialState(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(initialState);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(getInitialState(lines));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Set<Coord2D> getInitialState(List<String> lines) {
        Integer y = 0;
        Set<Coord2D> state = new HashSet<>();
        for (String l : lines) {
            String t[] = l.split("");
            for (int x = 0; x < t.length; x++) {
                if (t[x].equals("#")) {
                    state.add(new Coord2D(y,x));
                }
            }
            y++;
        }
        return state;
    }
    Long solutionPart1(Set<Coord2D> state) {
        boolean noRepeats = true;
        Set<Long> bioDiversity = new HashSet<>();
        Long currentDiversity = 0L;
        while (noRepeats) {
            currentDiversity = calculateDiversity(state);
            noRepeats = bioDiversity.add(currentDiversity);
            state = nextMinute(state);
        }
        return currentDiversity;
    }

    Long calculateDiversity(Set<Coord2D> state) {
        Long bitMask = 1L;
        Long value = 0L;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (state.contains(new Coord2D(y,x))) {
                    value |= bitMask;
                }
                bitMask <<= 1;
            }
        }
        return value;
    }
    Integer bugsNextToMe(Set<Coord2D> grid, Integer y, Integer x) {
        List<String> tmp = new ArrayList<>();
        tmp.add((y > 0) ? grid.contains(new Coord2D(y-1,x)) ? "#" : "." : ".");
        tmp.add((x > 0) ? grid.contains(new Coord2D(y,x-1)) ? "#" : "." : ".");
        tmp.add((y < 4) ? grid.contains(new Coord2D(y+1,x)) ? "#" : "." : ".");
        tmp.add((x < 4) ? grid.contains(new Coord2D(y,x+1)) ? "#" : "." : ".");
        Long count = tmp.stream().filter(t -> t.equals("#")).count();
        return count.intValue();
    }

    Set<Coord2D> nextMinute(Set<Coord2D> grid) {
        Set<Coord2D> newGrid = new HashSet<>();
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Integer neighbors = bugsNextToMe(grid, y, x);
                if (grid.contains(new Coord2D(y,x)) && neighbors.equals(1)) {
                    newGrid.add(new Coord2D(y,x));
                } else if (!grid.contains(new Coord2D(y,x)) && (neighbors.equals(1) || neighbors.equals(2))) {
                    newGrid.add(new Coord2D(y,x));
                }
            }
        }
        return newGrid;
    }

    Integer solutionPart2(Set<Coord2D> grid) {
        Set<Coord3D> grid3D = new HashSet<>();
        for (Coord2D g : grid) {
            grid3D.add(new Coord3D(g.x, g.y, 0));
        }
        for (int i = 0; i < 200; i++) {
            grid3D = recursiveGeneration(grid3D);
        }
        return grid3D.size();
    }

    Set<Coord3D> recursiveGeneration(Set<Coord3D> grid) {
        Set<Coord3D> newGrid = new HashSet<>();
        Integer minLevel = grid.stream().mapToInt(g -> g.z).min().getAsInt();
        Integer maxLevel = grid.stream().mapToInt(g -> g.z).max().getAsInt();
        for (Integer level = minLevel - 1; level <= maxLevel + 1; level++) {
            for (Integer y = 0; y < 5; y++) {
                for (Integer x = 0; x < 5; x++) {
                    if (x == 2 && y == 2) {
                        continue;
                    }
                    if (bugCreated(grid,y,x,level)) {
                        newGrid.add(new Coord3D(y,x,level));
                    }
                }
            }
        }
        return newGrid;
    }

    Boolean bugCreated(Set<Coord3D> grid, Integer y, Integer x, Integer z) {
        Integer neighbors = countNeighbors(grid, y, x, z);
        if (neighbors == 1) {
            return true;
        } else if (!grid.contains(new Coord3D(y,x,z)) && neighbors == 2) {
            return true;
        }
        return false;
    }

    Integer countNeighbors(Set<Coord3D> grid, Integer y, Integer x, Integer z) {
        // Check Cells Upward
        Set<Coord3D> cellsToCheck = new HashSet<>();
        if (y == 0) {
            cellsToCheck.add(new Coord3D(1,2,z-1));
        } else if (x == 2 && y == 3) {
            for (int i = 0; i < 5; i++) {
                cellsToCheck.add(new Coord3D(4,i,z+1));
            }
        } else {
            cellsToCheck.add(new Coord3D(y-1,x,z));
        }

        // Check Cells Left
        if (x == 0) {
            cellsToCheck.add(new Coord3D(2,1,z-1));
        } else if (x == 3 && y == 2) {
            for (int i = 0; i < 5; i++) {
                cellsToCheck.add(new Coord3D(i,4,z+1));
            }
        } else {
            cellsToCheck.add(new Coord3D(y,x-1,z));
        }

        //Check Cells Downward
        if (y == 4) {
            cellsToCheck.add(new Coord3D(3,2,z-1));
        } else if (x ==2  && y == 1) {
            for (int i = 0; i < 5; i++) {
                cellsToCheck.add(new Coord3D(0,i,z+1));
            }
        } else {
            cellsToCheck.add(new Coord3D(y+1,x,z));
        }

        // Check Cells Right
        if (x == 4) {
            cellsToCheck.add(new Coord3D(2,3,z-1));
        } else if (x == 1 && y == 2) {
            for (int i = 0; i < 5; i++) {
                cellsToCheck.add(new Coord3D(i,0,z+1));
            }
        } else {
            cellsToCheck.add(new Coord3D(y,x+1,z));
        }
        Long value = cellsToCheck.stream().filter(c -> grid.contains(c)).count();
        return value.intValue();
    }

}
