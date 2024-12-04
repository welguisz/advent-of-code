package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CeresSearch extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 4, false, 0);
        char[][] grid = convertToCharGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    long findString(char[][] grid, String wordSearch) {
        char[] word = wordSearch.toCharArray();
        long foundWord = 0l;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == word[0]) {
                    List<String> aroundTheX = getStuffAround(grid, i, j, 3);
                    foundWord += aroundTheX.stream().filter(str -> str.equals(wordSearch)).count();
                }
            }
        }
        return foundWord;
    }

    List<String> getStuffAround(char[][] grid, int x, int y, int count) {
        char[][] smallerGrid = createSmallerGrid(grid, x, y, count);
        List<String> result = new ArrayList<>();
        List<Coord2D> deltas = List.of(
                new Coord2D(-1,-1), new Coord2D(0, -1), new Coord2D(1, -1),
                new Coord2D(-1, 0), new Coord2D(0, 0), new Coord2D(1, 0),
                new Coord2D(-1, 1), new Coord2D(0, 1), new Coord2D(1, 1));
        for(Coord2D delta : deltas) {
            StringBuilder currStr = new StringBuilder();
            Coord2D currPos = new Coord2D(count, count);
            for (int i = 0; i <= count; i++) {
                currStr.append(smallerGrid[currPos.x][currPos.y]);
                currPos = currPos.add(delta);
            }
            result.add(currStr.toString());
        }
        return result;
    }

    long solutionPart1(char[][] grid) {
        return findString(grid, "XMAS");
    }

    long solutionPart2(char[][] grid) {
        long foundCross = 0l;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'A') {
                    if (getStuffAroundCross(grid, i, j, 1)) {
                        foundCross++;
                    }
                }
            }
        }
        return foundCross;
    }

    boolean getStuffAroundCross(char[][] grid, int x, int y, int count) {
        char[][] smallerGrid = createSmallerGrid(grid, x, y, count);
        List<String> result = new ArrayList<>();
        List<List<Coord2D>> deltas = List.of(
                List.of(new Coord2D(-1,-1), new Coord2D(1, 1)),
                List.of(new Coord2D(-1, 1), new Coord2D(1, -1)));
        Coord2D center = new Coord2D(1,1);
        for(List<Coord2D> delta : deltas) {
            StringBuilder currStr = new StringBuilder();
            for (Coord2D delta1 : delta) {
                Coord2D tmp = center.add(delta1);
                currStr.append(smallerGrid[tmp.x][tmp.y]);
            }
            result.add(currStr.toString());
        }
        List<String> allowedStrings = List.of("MS", "SM");
        return allowedStrings.containsAll(result);
    }

    char[][] createSmallerGrid(char[][] grid, int x, int y, int count) {
        char[][] smallerGrid = new char[2*count+1][2*count+1];
        for(int i = 0; i < smallerGrid.length; i++) {
            for(int j = 0; j < smallerGrid[i].length; j++) {
                smallerGrid[i][j] = 'Q';
            }
        }
        for (int i = -count; i < count+1; i++) {
            for (int j = -count; j < count+1; j++) {
                int tempX = x + i;
                int tempY = y + j;
                if (tempX >= 0  && tempX < grid.length && tempY >= 0 && tempY < grid[0].length) {
                    smallerGrid[i+count][j+count] = grid[tempX][tempY];
                }
            }
        }
        return smallerGrid;
    }


}
