package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PointOfIncidence extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 13, false, 0);
        List<char[][]> lavaPits = createLavaPits(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lavaPits,0);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lavaPits,1);
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

    Integer findReflectionRow(char[][] lavaPit, int badGoal) {
        for (int currentRow =0; currentRow < lavaPit.length-1; currentRow++) {
            int sum = 0;
            for (int deltaRow = 0; deltaRow < lavaPit.length / 2; deltaRow++) {
                int up = currentRow - deltaRow;
                int down = currentRow + deltaRow + 1;
                if (up >= 0 && down < lavaPit.length) {
                    for (int column = 0; column < lavaPit[0].length; column++) {
                        if (lavaPit[up][column] != lavaPit[down][column]) {
                            sum++;
                        }
                    }
                }
            }
            if (sum == badGoal) {
                return currentRow+1;
            }
        }
        return 0;
    }
    Integer findReflectionRowStream(char[][] lavaPit, int badGoal) {
        Map<Integer, List<Pair<Integer,Integer>>> t1 = IntStream.range(0, lavaPit.length-1).boxed()
                .flatMap(currentRow -> IntStream.range(0,lavaPit.length).boxed()
                        .map(deltaRow -> Pair.of(currentRow, new Coord2D(currentRow - deltaRow, currentRow + deltaRow + 1)))
                        .filter(p -> p.getRight().x >= 0 && p.getRight().y < lavaPit.length))  //Pair(currentRow, Coord2D (down, up))
                .flatMap(rows -> IntStream.range(0,lavaPit[0].length).boxed()
                        .map(col -> Pair.of(rows.getLeft(),lavaPit[rows.getRight().x][col] == lavaPit[rows.getRight().y][col] ? 0 : 1))
                ).collect(Collectors.groupingBy(p -> p.getLeft(), Collectors.toList()));
        return t1.entrySet().stream()
                .filter(e -> e.getValue().stream().mapToLong(v -> v.getRight()).sum() == badGoal)
                .map(e -> e.getKey()+1)
                .findFirst().orElse(0);
    }

    Long solutionPart1(List<char[][]> lavaPits, int badGoal) {
        return lavaPits.stream()
                .mapToLong(l -> findReflectionRow(l, badGoal) * 100 + findReflectionRow(rotateCharGridClockwise(l),badGoal))
                .sum();
    }
}
