package com.dwelguisz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class Main {


    public static void main(String[] args) {
//        List<Integer> depths = readFile("/home/dwelguisz/advent_of_coding/src/resources/input1.txt");
//        List<Integer> sums = calculateWindow(depths);
//        int increased = calculateIncreases(sums);
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/input3.txt");
        int oxygen = getRating(instructions, '1', true);
        int carbondioxide = getRating(instructions, '0', false);
        System.out.println(String.format("Oxygen: %d", oxygen));
        System.out.println(String.format("Carbon Dioxide: %d", carbondioxide));
        System.out.println(String.format("Multiplied oxygen and carbon dioxide: %d", oxygen * carbondioxide));
    }

    static private Integer getRating(final List<String> binaryValues, final char midVal, final boolean oxygen) {
        List<String> values = binaryValues;
        StringBuffer sb = new StringBuffer();
        int pos = 0;
        while (values.size() > 1) {
            values = calculateReduceNumbers(values, pos, midVal, oxygen);
            pos++;
        }
        return parseInt(values.get(0), 2);
    }

    static private List<String> calculateReduceNumbers(final List<String> values, int pos, char midVal, boolean oxygen) {
        List<Integer> positionCountis0 = new ArrayList<>();
        List<Integer> positionCountis1 = new ArrayList<>();
        for (int i = 0; i< values.get(0).length(); i++) {
            positionCountis0.add(0);
            positionCountis1.add(0);
        }
        for(int i = 0; i < values.size(); i++) {
            for(int j=0;j < values.get(i).length(); j++) {
                if (values.get(i).charAt(j) == '0') {
                    positionCountis0.set(j, positionCountis0.get(j) + 1);
                } else {
                    positionCountis1.set(j, positionCountis1.get(j) + 1);
                }
            }
        }
        char importVar = ' ';
        if (oxygen && (positionCountis0.get(pos) > positionCountis1.get(pos))) {
            importVar = '0';
        } else if (!oxygen &&(positionCountis0.get(pos) > positionCountis1.get(pos))) {
            importVar = '1';
        } else if (positionCountis0.get(pos) == positionCountis1.get(pos)) {
            importVar = midVal;
        } else if (oxygen && (positionCountis0.get(pos) < positionCountis1.get(pos))){
            importVar = '1';
        } else {
            importVar = '0';
        }
        List<String> reducedValues = new ArrayList<>();
        for(int i = 0; i < values.size(); i++) {
            if(values.get(i).charAt(pos) == importVar) {
                reducedValues.add(values.get(i));
            }
        }
        return reducedValues;
    }

    static private Integer calculateBinary(final List<String> instructions) {
        List<Integer> positionCountis0 = new ArrayList<>();
        List<Integer> positionCountis1 = new ArrayList<>();
        for (int i = 0; i< instructions.get(0).length(); i++) {
            positionCountis0.add(0);
            positionCountis1.add(0);
        }
        for(int i = 0; i < instructions.size(); i++) {
            for(int j=0;j < instructions.get(i).length(); j++) {
                if (instructions.get(i).charAt(j) == '0') {
                    positionCountis0.set(j, positionCountis0.get(j) + 1);
                } else {
                    positionCountis1.set(j, positionCountis1.get(j) + 1);
                }
            }
        }
        StringBuffer gamma = new StringBuffer();
        StringBuffer epsilon = new StringBuffer();
        for(int i = 0; i < instructions.get(0).length(); i++) {
            if (positionCountis0.get(i) < positionCountis1.get(i)) {
                gamma.append("0");
                epsilon.append("1");
            } else {
                gamma.append("1");
                epsilon.append("0");
            }
        }
        int finalGamma = parseInt(gamma.toString(), 2);
        int finalEpsilon = parseInt(epsilon.toString(), 2);
        return finalGamma * finalEpsilon;
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
