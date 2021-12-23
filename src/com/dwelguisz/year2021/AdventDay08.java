package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day8.SevenWireDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class AdventDay08 {

    static List<List<String>> input;
    static List<List<String>> output;

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2021/day8/input.txt");
        parseString(instructions);
        int part1 = countEasyNumbers(output);
        int part2 = SumNumbers(input, output);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static int SumNumbers(List<List<String>> inputStrings, List<List<String>> outputStrings) {
        int total = 0;
        for (int i = 0; i < inputStrings.size(); i++) {
            int temp = getValue(inputStrings.get(i), outputStrings.get(i));
            total += temp;
        }
        return total;
    }

    public static int getValue(List<String> inputStr, List<String> outputStrings) {
        SevenWireDisplay sevenWireDisplay = new SevenWireDisplay(inputStr);
        int temp = 0;
        for(String outputStr : outputStrings) {
            temp *= 10;
            int value = sevenWireDisplay.decodeString(outputStr);
            temp += value;
        }
        return temp;
    }

    public static int countEasyNumbers(List<List<String>> strings) {
        int total = 0;
        List<Integer> easySizes = Arrays.asList(2, 3, 4, 7);
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
