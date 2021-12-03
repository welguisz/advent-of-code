package com.dwelguisz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class Main {


    public static void main(String[] args) {
//        List<Integer> depths = readFile("/home/dwelguisz/advent_of_coding/src/resources/input1.txt");
//        List<Integer> sums = calculateWindow(depths);
//        int increased = calculateIncreases(sums);
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/input2.txt");
        System.out.println(String.format("Multiplied depth and horizontal: %d", calculateLocation(instructions)));
    }

    static private Integer calculateLocation(List<String> instructions) {
        int horizontal = 0;
        int depth = 0;
        int aim = 0;
        for(int i = 0; i < instructions.size(); i++) {
            String[] parseInstruction = instructions.get(i).split(" ");
            int step = parseInt(parseInstruction[1]);
            if ("forward".equals(parseInstruction[0])) {
                horizontal += step;
                depth = depth + (step*aim);
            } else if ("down".equals(parseInstruction[0])) {
                aim += step;
            } else if ("up".equals(parseInstruction[0])) {
                aim -= step;
            }
        }
        return horizontal*depth;
    }

    static private Integer calculateIncreases(List<Integer> values) {
        int increased = 0;
        boolean skipFirst = true;
        for(int i = 0; i < values.size(); i++) {
            if (skipFirst) {
                skipFirst = false;
                continue;
            }
            if (values.get(i) > values.get(i-1)) {
                increased++;
            }
        }
        return increased;
    }

    static private List<String> readFile(String fileName) {
        List<String> instructions = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> instructions.add(line));
        } catch (IOException e) {
            System.out.println("Exception caught\n" + e);
        }
        return instructions;
    }

    static private List<Integer> calculateWindow(List<Integer> depths) {
        List<Integer> sums = new ArrayList<>();
        for(int i = 0; i < depths.size(); i++) {
            if (i < 2) {
                continue;
            }
            sums.add(depths.get(i) + depths.get(i-1) + depths.get(i-2));
        }
        return sums;
    }
}
