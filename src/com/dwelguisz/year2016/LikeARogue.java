package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LikeARogue extends AoCDay {
    public static String MY_INPUT_ROW = ".^^^.^.^^^.^.......^^.^^^^.^^^^..^^^^^.^.^^^..^^.^.^^..^.^..^^...^.^^.^^^...^^.^.^^^..^^^^.....^....";
    public static Integer PART1_ROW = 40;
    public static String SAFE_TILE = ".";
    public static String TRAP_TILE = "^";
    public static List<String> TRAP_TILES = List.of("^^.",".^^","^..","..^");

    public void solve() {
        Integer part1 = solutionPart1(MY_INPUT_ROW, PART1_ROW);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart1(MY_INPUT_ROW, PART1_ROW * 10000);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }
    public Integer solutionPart1(String startRow, Integer numberOfRows) {
        String currentRow = startRow;
        Integer count = 0;
        for (int i = 0; i < numberOfRows; i++) {
            count += Arrays.stream(currentRow.split("")).filter(s -> s.equals(SAFE_TILE)).collect(Collectors.toList()).size();
            currentRow = calculateNextRow(currentRow);
        }
        return count;
    }
    public String calculateNextRow(String currentRow) {
        String inputStr = SAFE_TILE + currentRow + SAFE_TILE;
        String split[] = inputStr.split("");
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < split.length-1; i++) {
            String checkStr = Arrays.stream(new String[]{split[i-1],split[i],split[i+1]}).collect(Collectors.joining(""));
            String tmp = (TRAP_TILES.contains(checkStr)) ? TRAP_TILE : SAFE_TILE;
            sb.append(tmp);
        }
        return sb.toString();
    }
}
