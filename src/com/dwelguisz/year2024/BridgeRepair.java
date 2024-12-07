package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class BridgeRepair extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 7, false, 0);
        List<Pair<Long, List<Long>>> equations = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(equations);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(equations);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Pair<Long, List<Long>>> parseLines(List<String> lines) {
        List<Pair<Long, List<Long>>> equations = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(":\\s+");
            Long total = Long.parseLong(parts[0]);
            List<Long> operands = Arrays.stream(parts[1].split("\\s+")).map(Long::parseLong).collect(Collectors.toList());
            equations.add(Pair.of(total, operands));
        }
        return equations;
    }

    long solutionPart1(List<Pair<Long, List<Long>>> equations) {
        return equations.stream()
                .filter(e -> validEquationPart1(e.getLeft(), e.getRight()))
                .map(Pair::getLeft)
                .mapToLong(l -> l).sum();
    }

    boolean validEquationPart1(Long total, List<Long> operands) {
        List<Long> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(operands.get(0));
        for (int i = 1; i < operands.size(); i++) {
            int currentSize = possibleAnswers.size();
            while (currentSize > 0) {
                long temp = possibleAnswers.remove(0);
                possibleAnswers.add(temp * operands.get(i));
                possibleAnswers.add(temp + operands.get(i));
                currentSize--;
            }
        }
        return possibleAnswers.contains(total);
    }

    long solutionPart2(List<Pair<Long, List<Long>>> equations) {
        return equations.stream()
                .filter(e -> validEquationPart2(e.getLeft(), e.getRight()))
                .map(Pair::getLeft)
                .mapToLong(l -> l).sum();
    }

    boolean validEquationPart2(Long total, List<Long> operands) {
        List<Long> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(operands.get(0));
        for (int i = 1; i < operands.size(); i++) {
            int currentSize = possibleAnswers.size();
            while (currentSize > 0) {
                Long temp = possibleAnswers.remove(0);
                long mulTotal = temp * operands.get(i);
                long addTotal = temp + operands.get(i);
                long concatTotal = Long.parseLong(temp + operands.get(i).toString());;
                if (mulTotal <= total) {
                    possibleAnswers.add(mulTotal);
                }
                if (addTotal <= total) {
                    possibleAnswers.add(addTotal);
                }
                if (concatTotal <= total) {
                    possibleAnswers.add(concatTotal);
                }
                currentSize--;
            }
        }
        return possibleAnswers.contains(total);
    }

}
