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
        part2Answer = solutionPart2(equations) + (Long) part1Answer;
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
                .filter(e -> validEquationPart1_R2l(e.getLeft(), e.getRight(), false))
                .map(Pair::getLeft)
                .mapToLong(l -> l).sum();
    }

    boolean validEquationPart1_l2R(Long total, List<Long> operands, boolean part2) {
        Queue<Long> possibleAnswers = new LinkedList<>();
        possibleAnswers.add(operands.get(0));
        for (int i = 1; i < operands.size(); i++) {
            int currentSize = possibleAnswers.size();
            while (currentSize > 0) {
                long temp = possibleAnswers.poll();
                long mulTotal = temp * operands.get(i);
                long addTotal = temp + operands.get(i);
                if (mulTotal <= total) {
                    possibleAnswers.add(mulTotal);
                }
                if (addTotal <= total) {
                    possibleAnswers.add(addTotal);
                }
                if (part2) {
                    Long concatTotal = Long.parseLong(temp + operands.get(i).toString());;
                    if (concatTotal <= total) {
                        possibleAnswers.add(concatTotal);
                    }
                }
                currentSize--;
            }
        }
        return possibleAnswers.contains(total);
    }

    boolean validEquationPart1_R2l(Long total, List<Long> operands, boolean part2) {
        Queue<Long> possibleAnswers = new LinkedList<>();
        Stack<Long> stackOperands = new Stack<>();
        operands.forEach(operand -> stackOperands.push(operand));
        possibleAnswers.add(total);
        while (stackOperands.size() > 1 && !possibleAnswers.isEmpty()) {
            int currentSize = possibleAnswers.size();
            long operand = stackOperands.pop();
            while (currentSize > 0) {
                long temp = possibleAnswers.poll();
                if (temp - operand >= 0) {
                    possibleAnswers.add(temp - operand);
                }
                if (temp % operand == 0) {
                    possibleAnswers.add(temp / operand);
                }
                if (part2) {
                    int operandLength = String.valueOf(operand).length();
                    String tempStr = String.valueOf(temp);
                    if (tempStr.length() >= operandLength) {
                        String firstPart = tempStr.substring(0, tempStr.length() - operandLength);
                        String secondPart = tempStr.substring(tempStr.length() - operandLength);
                        if (secondPart.equals(String.valueOf(operand)) && !firstPart.isEmpty()) {
                            possibleAnswers.add(Long.parseLong(firstPart));
                        }
                    }
                }
                currentSize--;
            }
        }
        return possibleAnswers.contains(stackOperands.pop());
    }


    long solutionPart2(List<Pair<Long, List<Long>>> equations) {
        return equations.stream()
                .filter(e -> !validEquationPart1_R2l(e.getLeft(), e.getRight(), false))
                .filter(e -> validEquationPart1_R2l(e.getLeft(), e.getRight(), true))
                .map(Pair::getLeft)
                .mapToLong(l -> l).sum();
    }
}
