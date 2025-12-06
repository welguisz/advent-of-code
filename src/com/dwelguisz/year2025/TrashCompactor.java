package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrashCompactor extends AoCDay {

    public class MathWork {
        public List<String> values;
        public Integer operationPositions;
        public List<Integer> valuePositions;
        public String operation;
        public boolean isLast;

        public MathWork() {
            values = new ArrayList<>();
            operationPositions = 0;
            valuePositions = new ArrayList<>();
            operation = "+";
            isLast = false;
        }

        public void addValue(String a) {
            values.add(a);
        }

        public void setOperationPosition(int a) {
            operationPositions = a;
        }
        public void addValuePosition(int a) {
            valuePositions.add(a);
        }

        public Long solve() {
            Long tmp = 0l;
            if (operation.equals("+")) {
                tmp = values.stream().map(String::trim).map(Long::parseLong).reduce(0L, Long::sum);
            } else if (operation.equals("*")) {
                tmp = values.stream().map(String::trim).map(Long::parseLong).reduce(1L, (a,b) -> a*b);
            }
            return tmp;
        }

        public Long solve2() {
            int maxLength = values.stream().map(String::length).max(Integer::compare).get();
            List<StringBuilder> builders = new ArrayList<>();
            int temp = isLast ? 0 : 1;
            for (int i = 0; i < maxLength - temp; i++) {
                builders.add(new StringBuilder());
            }
            for (int i = 0; i < maxLength - temp; i++) {
                for (String value : values) {
                    if (!value.substring(i, i+1).isBlank()) {
                        builders.get(i).append(value.substring(i, i+1));
                    }
                }
            }
            List<String> newValues = builders.stream().map(StringBuilder::toString).collect(Collectors.toList());
            long tmp = 0l;
            if (operation.equals("+")) {
                tmp = newValues.stream().map(String::trim).map(Long::parseLong).reduce(0L, Long::sum);
            } else if (operation.equals("*")) {
                tmp = newValues.stream().map(String::trim).map(Long::parseLong).reduce(1L, (a,b) -> a*b);
            }
            return tmp;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 6, false, 0);
        List<MathWork> problems = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(problems);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(problems);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    List<MathWork> parseLines(List<String> lines) {
        List<MathWork> values = new ArrayList<>();
        String[] splits = lines.get(lines.size()-1).split("\\s+");
        for (int i = 0; i < splits.length; i++) {
            values.add(new MathWork());
        }
        Integer maxLengthSize = lines.stream().map(String::length).max(Integer::compare).get();
        values.get(values.size()-1).isLast = true;
        String last_line = lines.get(lines.size()-1);
        int position = 0;
        int pointer = 0;
        while (position < last_line.length()) {
            if (last_line.charAt(position) != ' ') {
                values.get(pointer).setOperationPosition(position);
                values.get(pointer).operation = "" + last_line.charAt(position);
                pointer++;
            }
            position++;
        }
        int line_number = 0;
        int last_line_number = lines.size() - 1;
        for (String line : lines) {
            if (line_number == last_line_number) {
                continue;
            }
            int count = 0;
            while (count < values.size()) {
                int current_operation_loc = values.get(count).operationPositions;
                int next_operation_loc = line.length();
                String more_spaces = "";
                if (count < values.size() - 1) {
                    next_operation_loc = values.get(count + 1).operationPositions;
                } else {
                    more_spaces = " ".repeat(maxLengthSize - line.length());
                }
                values.get(count).addValue(line.substring(current_operation_loc, next_operation_loc) + more_spaces);
                count++;
            }
            line_number++;
        }
        return values;
    }

    long solutionPart1(List<MathWork> problems) {
        return problems.stream().map(MathWork::solve).reduce(0L, Long::sum);
    }

    long solutionPart2(List<MathWork> problems) {
        return problems.stream().map(MathWork::solve2).reduce(0L, Long::sum);
    }
}
