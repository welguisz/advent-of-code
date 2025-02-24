package com.dwelguisz.base;

import com.dwelguisz.utilities.Coord2D;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class AoCDay {
    private static String SESSION_ID = "53616c7465645f5fc2d415e9fd5facbee2c017f6e6a962da68d9bad559c6ec79f4a4e63217c0a6ee8eeb7d2cfd7699f0b3b399f756ef412cf6c5a916596b135c";

    private static String DOCS_DIRECTORY = "/Users/davidwelguisz/coding/advent-of-code/docs/";

    private static Coord2D MIN_PUZZLE = new Coord2D(2015, 1);
    private static Coord2D MAX_PUZZLE = new Coord2D (2024, 25);

    @Setter
    private AoCClient aoCClient;

    public Long timeMarkers[] = new Long[]{0L,0L,0L,0L};
    public Object part1Answer;
    public Object part2Answer;
    public String puzzleName = "Not yet implemented";
    public String setup = null;
    public String difficultLevel;
    public String inputDescription;
    public String part1Solution;
    public String part2Solution;
    public List<String> skills = new ArrayList<>();
    public String notesAboutPuzzle = null;
    public boolean printExplanation = false;

    public void solve() {
        System.out.println("Not yet implemented");
    }

    public String previousPuzzle(Integer year, Integer day) {
        int previousDay = (day == 1) ? 25 : day - 1;
        int previousYear = (day == 1) ? year - 1 : year;
        return String.format("[Previous (Year %d, Day %d)](../../year%d/day%2d/README.md)",previousYear,previousDay,previousYear,previousDay).replaceAll("day ", "day0");
    }

    public String nextPuzzle(Integer year, Integer day) {
        int nextDay = (day == 25) ? 1 : day + 1;
        int nextYear = (day == 25) ? year + 1 : year;
        return String.format("[Next (Year %d, Day %d)](../../year%d/day%2d/README.md)",nextYear,nextDay,nextYear,nextDay).replaceAll("day ", "day0");
    }


    public void run() {
        run(true, MIN_PUZZLE.x, MIN_PUZZLE.y);
    }

    public void run(boolean printStatements, int year, int day) {
        solve();
        if (printStatements) {
            System.out.println("---------" + getClass().getName() + "------------");
            System.out.println(String.format("%20s|%20s |%20s |%20s |", "Elapsed Times:", "Parsing Time(ms)", "Part 1 Time(ms)", "Part 2 Time(ms)"));
            System.out.println(String.format("%20s|%20d |%20d |%20d |", "", timeMarkers[1] - timeMarkers[0], timeMarkers[2] - timeMarkers[1], timeMarkers[3] - timeMarkers[2]));
            System.out.println("Part 1 Answer: " + part1Answer);
            System.out.println("Part 2 Answer: " + part2Answer);
        }
        if (printExplanation) {
            StringBuffer markdown = new StringBuffer("# Day " + day + " " + puzzleName + "\n\n");
            markdown.append("[Back to Top README file](../../../README.md)\n\n");
            markdown.append("## Overview\n\n* [Puzzle Prompt](")
                    .append(aocPuzzlePrompt(year,day))
                    .append(")\n* Difficult Level: ").append(difficultLevel)
                    .append("\n* [Input](").append(aocPuzzlePrompt(year,day)).append("/input): ").append(inputDescription)
                    .append("\n* Skills/Knowledge: ").append(String.join(", ", skills))
                    .append("\n\n");
            if (setup != null) {
                markdown.append("## Setup\n\n").append(setup).append("\n\n");
            }
            markdown.append("## Part 1 Solution:\n\n").append(part1Solution).append("\n\n");
            markdown.append("## Part 2 Solution:\n\n").append(part2Solution).append("\n\n");
            if (notesAboutPuzzle != null) {
                markdown.append("## Notes about this puzzle\n\n").append(notesAboutPuzzle).append("\n\n");
            }
            markdown.append("## Times\n\n");
            markdown.append("* Parsing: ").append(timeMarkers[1] - timeMarkers[0]).append(" ms\n");
            markdown.append("* Part 1 Solve time: ").append(timeMarkers[2] - timeMarkers[1]).append(" ms\n");
            markdown.append("* Part 2 Solve time: ").append(timeMarkers[3] - timeMarkers[2]).append(" ms\n\n");
            markdown.append("## Solutions: \n\n");
            markdown.append("* Part 1: ").append(part1Answer).append("\n");
            markdown.append("* Part 2: ").append(part2Answer).append("\n\n");
            if (MIN_PUZZLE.equals(new Coord2D(year, day))) {
                markdown.append("| |\n|:---|\n").append(nextPuzzle(year, day));
            } else if (MAX_PUZZLE.equals(new Coord2D(year, day))) {
                markdown.append("| |\n|---:|\n").append(previousPuzzle(year, day));
            } else {
                markdown.append("| | |\n|:---|---:|\n|").append(previousPuzzle(year, day)).append("|").append(nextPuzzle(year, day)).append("|\n");
            }
            try {
                writeToDocsDirectory(year, day, markdown.toString());
            } catch (IOException ignored) {}
        }

    }

    String aocPuzzlePrompt(int year, int day) {
        return "https://adventofcode.com/" + year + "/day/" + day;
    }

    public void printSummary(int i) {
        String className = getClass().getName();
        String[] splitNames = className.split("\\.");
        String name = splitNames[splitNames.length-1];
        System.out.println(String.format("|%4d |%25s |%20d |%20d |%20d |%20s |%20s |",i,name,timeMarkers[1]-timeMarkers[0],timeMarkers[2]-timeMarkers[1],timeMarkers[3]-timeMarkers[2], part1Answer.toString(), part2Answer.toString()));
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
            try {
                this.aoCClient = new AoCClient(year, day, classLoader);
                aoCClient.fetchData();
                inputStream = classLoader.getResourceAsStream(resourceFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private void writeToDocsDirectory(int year, int day, String body) throws IOException {
        String separator = FileSystems.getDefault().getSeparator();
        String directories[] = new String[]{DOCS_DIRECTORY, String.format("year%d",year),String.format("day%2d",day).replace(" ","0")};
        Files.createDirectories(Path.of(String.join(separator, directories)));
        File newFile = new File(String.join(separator, directories) + "/README.md");
        try (FileWriter writer = new FileWriter(newFile)){
            writer.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    protected char[][] rotateCharGridClockwise(char[][] grid) {
        char[][] newGrid = new char[grid[0].length][grid.length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[c][r] = grid[r][c];
            }
        }
        return newGrid;
    }

    protected char[][] rotateCharGridCounterClockwise(char[][] grid) {
        int maxR = grid.length;
        int maxC = grid[0].length;
        char[][] newGrid = new char[maxR][maxC];
        for (int r = 0; r < maxR; r++) {
            for (int c = 0; c < maxC; c++) {
                newGrid[c][maxR-1-r] = grid[r][c];
            }
        }
        return newGrid;
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

    public Coord2D findStartingLocation(char[][] grid, char startingChar) {
        return IntStream.range(0, grid.length).boxed()
                .flatMap(row -> IntStream.range(0,grid[0].length).boxed()
                        .map(col -> new Coord2D(row,col)))
                .filter(pos -> grid[pos.x][pos.y] == startingChar)
                .findFirst().get();
    }
}
