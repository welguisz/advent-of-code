package com.dwelguisz.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

}
