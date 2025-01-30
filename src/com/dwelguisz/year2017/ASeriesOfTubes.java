package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ASeriesOfTubes extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,19,false,0);
        Character[][] grid = createGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        Pair<String, Integer> answers = solutionPart1(grid, lines.get(0).indexOf('|'));
        part1Answer = answers.getLeft();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = answers.getRight();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Character[][] createGrid(List<String> lines) {
        Integer xDimension = lines.stream().map(s -> s.length()).max(Integer::compareTo).get();
        Integer yDimension = lines.size();
        Character grid[][] = new Character[yDimension+1][xDimension+1];
        int y = 0;
        for (String line : lines) {
            int x = 0;
            for (; x < line.length();x++) {
                grid[y][x] = line.toCharArray()[x];
            }
            for (;x<=xDimension;x++) {
                grid[y][x] = ' ';
            }
            y++;
        }
        for (int x = 0; x <= xDimension; x++) {
            grid[y][x] = ' ';
        }
        return grid;
    }
    public Pair<String,Integer> solutionPart1(Character grid[][], Integer startX) {
        StringBuffer answer = new StringBuffer();
        boolean done = false;
        Character currentItem = '|';
        int xPosition = startX;
        int yPosition = 1;
        Integer currentYDirection = 1;
        Integer currentXDirection = 0;
        List<Character> doNothingChars = List.of('-','|');
        List<Character> pathTaken = new ArrayList<>();
        try {
            while (!done) {
                if (currentItem != ' ') {
                    pathTaken.add(currentItem);
                }
                Character val = grid[yPosition][xPosition];
                if (!doNothingChars.contains(currentItem)) {
                    if (currentItem == '+') {
                        if ((val == '|' && currentYDirection != 0) || (val == '-' && currentXDirection != 0)) {
                            //Do nothing, continue onward
                        } else {
                            if (currentYDirection != 0) {
                                yPosition -= currentYDirection; //move back one direction
                                Integer tmp = currentYDirection;
                                currentYDirection = 0;
                                //Check edges
                                if (xPosition == 0) {
                                    currentXDirection = 1;
                                } else if (xPosition == grid[yPosition].length - 1) {
                                    currentXDirection = -1;
                                } else if (grid[yPosition][xPosition - 1] != ' ') {
                                    currentXDirection = -1;
                                } else if (grid[yPosition][xPosition + 1] != ' ') {
                                    currentXDirection = 1;
                                } else {
                                    currentYDirection = tmp;
                                }
                            } else if (currentXDirection != 0) {
                                xPosition -= currentXDirection;
                                Integer tmp = currentXDirection;
                                currentXDirection = 0;
                                if (yPosition == 0) {
                                    currentYDirection = 1;
                                } else if (yPosition == grid.length - 1) {
                                    currentYDirection = -1;
                                } else if (grid[yPosition - 1][xPosition] != ' ') {
                                    currentYDirection = -1;
                                } else if (grid[yPosition + 1][xPosition] != ' ') {
                                    currentYDirection = 1;
                                } else {
                                    currentYDirection = tmp;
                                }
                            }
                            yPosition += currentYDirection;
                            xPosition += currentXDirection;
                            val = grid[yPosition][xPosition];
                        }
                    } else if (currentItem == ' ') {
                        done = true;
                    } else {
                        answer.append(currentItem);
                    }
                }
                xPosition += currentXDirection;
                yPosition += currentYDirection;
                currentItem = val;

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //Do nothing
        }
        return Pair.of(answer.toString(),pathTaken.size());

    }
}
