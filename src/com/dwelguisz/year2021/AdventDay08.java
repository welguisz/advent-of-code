package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.SevenWireDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class AdventDay08 {

    static List<List<String>> input;
    static List<List<String>> output;

    static Map<String, Integer> numberSetup;

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_coding/src/resources/year2021/day8/input.txt");
        parseString(instructions);
        int part1 = countEasyNumbers(output);
        int part2 = SumNumbers(input, output);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static int SumNumbers(List<List<String>> inputStrings, List<List<String>> outputStrings) {
        int total = 0;
        int lineNumber = 0;
        for (List<String> inputStr : inputStrings) {
            SevenWireDisplay sevenWireDisplay = new SevenWireDisplay(inputStr);
            int temp = 0;
            for(String outputStr : outputStrings.get(lineNumber)) {
                temp *= 10;
                int value = sevenWireDisplay.decodeString(outputStr);
                temp += value;
            }
            lineNumber++;
            total += temp;
        }
        return total;
    }

    public static int decodeString(String val) {
        if (val.length() == 2) {
            return 1;
        } else if (val.length() == 3) {
            return 7;
        } else if (val.length() == 4) {
            return 4;
        } else if (val.length() == 7) {
            return 8;
        } else if (val.length() == 6) { // could be 0, 6, 9
            List<String> vals = Arrays.stream(val.split("")).collect(Collectors.toList());
            if (!vals.contains("f")) {
                return 0;
            } else if (vals.contains("g")) {
                return 9;
            } else if (vals.contains("a")) {
                return 6;
            }
        } else if (val.length() == 5) { // could be 5, 2, 3
            List<String> vals = Arrays.stream(val.split("")).collect(Collectors.toList());
            if (vals.contains("g")) {
                return 5;
            } else if (vals.contains("e")) {
                return 3;
            } else if (vals.contains("b")) {
                return 5;
            }
        }
        return -134;
    }

    public static int countEasyNumbers(List<List<String>> strings) {
        int total = 0;
        List<Integer> easySizes = new ArrayList<>();
        easySizes.add(2);
        easySizes.add(3);
        easySizes.add(4);
        easySizes.add(7);
        for(List<String> str : strings) {
            for (String sp : str) {
                if(easySizes.contains(sp.length())) {
                    total++;
                }
            }
        }
        return total;
    }

    public static void parseString(List<String> instructions) {
        input = new ArrayList<>();
        output = new ArrayList<>();
        int pos =0;
        for (String instruction : instructions) {
            String[] splitStr = instruction.split(" ");
            boolean useInput = true;
            List<String> inputList = new ArrayList<>();
            List<String> outputList = new ArrayList<>();
            for (String sp: splitStr) {
                if (sp.equals("|")) {
                    useInput = false;
                }
                else if (useInput)
                    inputList.add(sp);
                else {
                    outputList.add(sp);
                }
            }
            input.add(inputList);
            output.add(outputList);
            pos++;
        }
    }

}
