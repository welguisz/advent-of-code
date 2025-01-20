package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LetItSnow extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,25,false,0);
        List<Integer> numbers = parseLine(instructions.get(0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(numbers.get(0), numbers.get(1));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Integer> parseLine(String line) {
        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("row (?<row>\\d+), column (?<column>\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            numbers.add(Integer.parseInt(matcher.group("row")));
            numbers.add(Integer.parseInt(matcher.group("column")));
        }
        return numbers;
    }

    public Long solutionPart1(Integer targetRow, Integer targetCol) {
        Long currentNumber = 20151125L;
        Long multiplier = 252533L;
        Long divisor = 33554393L;
        Integer currentRow = 1;
        Integer currentCol = 1;
        Integer maxRow = currentRow;
        while (!(currentRow.equals(targetRow) && currentCol.equals(targetCol))) {
            Integer nextRow = currentRow - 1;
            Integer nextCol = currentCol + 1;
            if (nextRow == 0) {
                nextRow = maxRow + 1;
                maxRow = nextRow;
                nextCol = 1;
            }
            currentNumber = (currentNumber * multiplier) % divisor;
            currentRow = nextRow;
            currentCol = nextCol;
        }
        return currentNumber;
    }

    String solutionPart2() {
        return "Press link to complete.";
    }

}
