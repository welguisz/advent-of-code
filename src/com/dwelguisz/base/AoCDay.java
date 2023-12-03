package com.dwelguisz.base;

import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
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

    public Long timeMarkers[] = new Long[]{0L,0L,0L,0L};
    public Object part1Answer;
    public Object part2Answer;
    public void solve() {
        System.out.println("Not yet implemented");
    }

    public void run() {
        solve();
        System.out.println("---------"+getClass().getName()+"------------");
        System.out.println(String.format("%20s|%20s |%20s |%20s |","Elapsed Times:","Parsing Time(ms)","Part 1 Time(ms)","Part 2 Time(ms)"));
        System.out.println(String.format("%20s|%20d |%20d |%20d |","",timeMarkers[1]-timeMarkers[0],timeMarkers[2]-timeMarkers[1],timeMarkers[3]-timeMarkers[2]));
        System.out.println("Part 1 Answer: " + part1Answer);
        System.out.println("Part 2 Answer: " + part2Answer);
    }

    public InputStream getFileFromResourceStream(Integer year, Integer day, boolean test) {
        return getFileFromResourceStream(year, day, test, 0);
    }

    public InputStream getFileFromResourceStream(Integer year, Integer day, boolean test, Integer testSuffix) {
        String separator = FileSystems.getDefault().getSeparator();
        String filename = test ? "testcase" : "input";
        filename += (testSuffix > 0) ? String.format("%d",testSuffix) : "";
        filename += ".txt";
        String directories[] = new String[]{String.format("year%d",year),String.format("day%2d",day).replace(" ","0"),filename};
        String resourceFile = Arrays.stream(directories).collect(Collectors.joining(separator));
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFile);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + resourceFile);
        }
        return inputStream;
    }

    public List<String> readResourceFile(Integer year, Integer day, boolean test) {
        return readResoruceFile(year, day, test, 0);
    }

    public List<String> readResoruceFile(Integer year, Integer day, boolean test, Integer testSuffix) {
        InputStream is = getFileFromResourceStream(year,day,test,testSuffix);
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to read file" + e);
        }
        return lines;
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

    protected char[][] convertToCharGrid(List<String> lines) {
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        Integer i = 0;
        for(String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j);
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
