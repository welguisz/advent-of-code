package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class HandheldHalting extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day08/input.txt");
        Long part1 = solutionPart1(lines);
        Long part2 = solutionPart2(lines);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part2: %d",part2));
    }

    private Long solutionPart1(List<String> lines) {
        Long accumulator = 0L;
        final Boolean[] instructionRan = new Boolean[lines.size()];
        Integer programCounter = 0;
        for (int i = 0; i < lines.size(); i++) {
            instructionRan[i] = false;
        }
        while (!(instructionRan[programCounter])) {
            instructionRan[programCounter] = true;
            String subStr = lines.get(programCounter).substring(0,3);
            if (subStr.equals("nop")) {
                programCounter += 1;
            } else if (subStr.equals("acc")) {
                accumulator += parseValue(lines.get(programCounter).split(" ")[1]);
                programCounter += 1;
            } else {
                programCounter += parseValue(lines.get(programCounter).split(" ")[1]).intValue();
            }
        }
        return accumulator;
    }

    private Long solutionPart2(List<String> lines) {
        Integer lineNumber = 0;
        Boolean found = false;
        Long accumulator = 0L;
        while(lineNumber < lines.size() && !found) {
            if (lines.get(lineNumber).startsWith("acc")) {
                lineNumber++;
                continue;
            }
            String[] tmpLines = new String[lines.size()];
            tmpLines = lines.toArray(tmpLines);
            String second = tmpLines[lineNumber].split(" ")[1];
            if (tmpLines[lineNumber].startsWith("nop")) {
                tmpLines[lineNumber] = "jmp " + second;
            } else {
                tmpLines[lineNumber] = "nop " + second;
            }
            Pair<Boolean, Long> value = doesItRunToEnd(tmpLines);
            found = value.getLeft();
            accumulator = value.getRight();
            lineNumber++;
        }
        System.out.println(String.format("Line number changed: %d", lineNumber - 1));
        return accumulator;
    }

    private Pair<Boolean,Long> doesItRunToEnd(String[] lines) {
        final Boolean[] instructionRan = new Boolean[lines.length+1];
        Long accumulator = 0L;
        Integer programCounter = 0;
        for (int i = 0; i <= lines.length; i++) {
            instructionRan[i] = false;
        }
        while ((programCounter != lines.length) && !(instructionRan[programCounter])) {
            instructionRan[programCounter] = true;
            String subStr = lines[programCounter].substring(0,3);
            if (subStr.equals("nop")) {
                programCounter += 1;
            } else if (subStr.equals("acc")) {
                accumulator += parseValue(lines[programCounter].split(" ")[1]);
                programCounter += 1;
            } else {
                programCounter += parseValue(lines[programCounter].split(" ")[1]).intValue();
            }
        }
        Boolean finished = (programCounter == lines.length);
        return Pair.of(finished, accumulator);
    }

    private Long parseValue(String val) {
        if (val.substring(0,1).equals("+")) {
            return Long.parseLong(val.substring(1));
        } else {
            return Long.parseLong(val);
        }
    }
}
