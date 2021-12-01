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
        int increased = 0;
        boolean skipFirst = true;
        for(int i = 0; i < depths.size(); i++) {
            if (skipFirst) {
                skipFirst = false;
                continue;
            }
            if (depths.get(i) > depths.get(i-1)) {
                increased++;
            }
        }
        System.out.println(String.format("number of increases: %d", increased));
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
}
