package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PointOfIncidence extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 13, false, 0);
        List<char[][]> lavaPits = createLavaPits(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lavaPits);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lavaPits);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<char[][]> createLavaPits(List<String> lines) {
        List<char[][]> lavaPits = new ArrayList<>();
        List<String> currentSet = new ArrayList<>();
        for (String l : lines) {
            if (l.length() == 0) {
                lavaPits.add(convertToCharGrid(currentSet));
                currentSet = new ArrayList<>();
            } else {
                currentSet.add(l);
            }
        }
        lavaPits.add(convertToCharGrid(currentSet));
        return lavaPits;
    }

    Long findReflection(char[][] lavaPit, int badGoal) {
        int r = 0;
        for (int currentRow = 0; currentRow < lavaPit.length-1;currentRow++) {
            int bad = 0;
            for (int deltaRow = 0; deltaRow < lavaPit.length/2+1;deltaRow++) {
                Integer up = currentRow - deltaRow;
                Integer down = currentRow + deltaRow + 1;
                if (up >= 0 && up < lavaPit.length && down >= 0 && down < lavaPit.length && up < down) {
                    for (int column = 0; column < lavaPit[0].length; column++) {
                        if (lavaPit[up][column] != lavaPit[down][column]) {
                            bad++;
                        }
                    }
                }
            }
            if (bad == badGoal) {
                r = currentRow+1;
            }
        }

        int c = 0;
        for (int currentCol = 0; currentCol < lavaPit[0].length-1; currentCol++) {
            int bad = 0;
            for (int deltaCol = 0; deltaCol < lavaPit[0].length;deltaCol++) {
                int left = currentCol - deltaCol;
                int right = currentCol + deltaCol + 1;
                if (left >= 0 && left < lavaPit[0].length && right >= 0 && right < lavaPit[0].length && left < right) {
                    for (int row = 0; row < lavaPit.length; row++) {
                        if (lavaPit[row][left] != lavaPit[row][right]) {
                            bad++;
                        }
                    }
                }
            }
            if (bad == badGoal) {
                c = currentCol+1;
            }
        }

        return (r*100L)+c;
    }

    Long solutionPart1(List<char[][]> lavaPits) {
        return lavaPits.stream().mapToLong(l -> findReflection(l,0)).sum();
    }

    Long solutionPart2(List<char[][]> lavaPits) {
        return lavaPits.stream().mapToLong(l -> findReflection(l,1)).sum();
    }

}
