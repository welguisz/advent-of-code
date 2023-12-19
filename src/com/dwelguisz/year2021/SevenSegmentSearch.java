package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day8.SevenWireDisplay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SevenSegmentSearch extends AoCDay {

    private List<List<String>> input;
    private List<List<String>> output;

    public SevenSegmentSearch() {
        super();
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2021,8,false,0);
        parseString(instructions);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = countEasyNumbers(output);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = SumNumbers(input, output);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int SumNumbers(List<List<String>> inputStrings, List<List<String>> outputStrings) {
        int total = 0;
        for (int i = 0; i < inputStrings.size(); i++) {
            int temp = getValue(inputStrings.get(i), outputStrings.get(i));
            total += temp;
        }
        return total;
    }

    public int getValue(List<String> inputStr, List<String> outputStrings) {
        SevenWireDisplay sevenWireDisplay = new SevenWireDisplay(inputStr);
        int temp = 0;
        for(String outputStr : outputStrings) {
            temp *= 10;
            int value = sevenWireDisplay.decodeString(outputStr);
            temp += value;
        }
        return temp;
    }

    public int countEasyNumbers(List<List<String>> strings) {
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

    public void parseString(List<String> instructions) {
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
