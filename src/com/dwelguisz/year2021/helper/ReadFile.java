package com.dwelguisz.year2021.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ReadFile {
    static public List<String> readFile(String fileName) {
        List<String> instructions = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> instructions.add(line));
        } catch (IOException e) {
            System.out.println("Exception caught\n" + e);
        }
        return instructions;
    }

}
