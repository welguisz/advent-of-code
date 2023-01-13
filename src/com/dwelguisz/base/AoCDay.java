package com.dwelguisz.base;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class AoCDay {

    public void solve() {
        System.out.println("Not yet implemented");
    }

    public List<String> readFile(String fileName) {
        List<String> instructions = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> instructions.add(line));
        } catch (IOException e) {
            System.out.println("Exception caught\n" + e);
        }
        return instructions;
    }

    public List<Integer> convertStringsToInts(List<String> lines) {
        return lines.stream().map(str -> parseInt(str)).collect(Collectors.toList());
    }

    protected String[][] convertToGrid(List<String> lines) {
        String[][] grid = new String[lines.size()][lines.get(0).length()];
        Integer i = 0;
        for(String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.substring(j,j+1);
            }
            i++;
        }
        return grid;
    }

    protected String[][] copy2DArray(String [][] source) {
        String[][] newGrid = Arrays.stream(source)
                .map((String[] row) -> row.clone())
                .toArray((int length) -> new String[length][]);
        return newGrid;
    }


    protected static void printGrid(String[][] grid) {
        StringBuffer sb = new StringBuffer("\n");
        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length;j++) {
                sb.append(grid[i][j]);
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb);
    }

    protected void printGrid(Character[][] grid) {
        StringBuffer sb = new StringBuffer("\n");
        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length;j++) {
                sb.append(grid[i][j]);
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb);
    }


    public List<String> convertStringToList(String input) {
        return Arrays.stream(input.split("")).collect(Collectors.toList());
    }

}
