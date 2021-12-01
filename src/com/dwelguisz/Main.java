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
        List<Integer> depths = readFile("/home/dwelguisz/advent_of_coding/src/resources/input1.txt");
        List<Integer> sums = calculateWindow(depths);
        int increased = calculateIncreases(sums);
        System.out.println(String.format("number of increases: %d", increased));
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

    static private List<Integer> readFile(String fileName) {
        List<Integer> numbers = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> numbers.add(parseInt(line)));
        } catch (IOException e) {
            System.out.println("Exception caught\n" + e);
        }
        return numbers;
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
