package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.microsoft.z3.*;

public class Factory extends AoCDay {
    public class FactoryMachines {
        String finalState;
        List<List<Integer>> buttons;
        List<Integer> joltageRequirements;

        public FactoryMachines(
                String finalState,
               List<List<Integer>> buttons,
               List<Integer> joltageRequirements) {
            this.finalState = finalState;
            this.buttons = buttons;
            this.joltageRequirements = joltageRequirements;
        }

        public Set<Pair<String,Integer>> nextState(String currentState, int count, Set<String> seen) {
            Set<Pair<String,Integer>> nextStates = new HashSet<>();
            Integer nextCount = count+1;
            for(List<Integer> button : buttons) {
                String nextState = currentState;
                char[] chars = nextState.toCharArray();
                for(Integer circuit : button) {
                    chars[circuit] = (chars[circuit] == '.') ? '#' : '.';
                }
                Pair<String,Integer> pair = Pair.of(new String(chars), nextCount);
                if (!seen.contains(pair.getLeft())) {
                    nextStates.add(pair);
                }
            }
            return nextStates;
        }

        public int solve() {
            String initialState = ".".repeat(finalState.length());
            PriorityQueue<Pair<String,Integer>> queue = new PriorityQueue<>(100, Comparator.comparingInt(Pair::getRight));
            queue.add(Pair.of(initialState,0));
            Set<String> seen = new HashSet<>();
            seen.add(initialState);
            while(!queue.isEmpty()) {
                Pair<String,Integer> pair = queue.poll();
                seen.add(pair.getLeft());
                if (pair.getLeft().equals(finalState)) {
                    return pair.getRight();
                }
                Set<Pair<String,Integer>> nextStates = nextState(pair.getLeft(), pair.getRight(), seen);
                queue.addAll(nextStates);
            }
            return -10000000;
        }
        
        public int solvePart2() {
            Context ctx = new Context();
            Optimize opt = ctx.mkOptimize();
            IntExpr presses = ctx.mkIntConst("presses");

            IntExpr[] buttonVars = IntStream.range(0, buttons.size())
                    .mapToObj(i -> ctx.mkIntConst("button" + i))
                    .toArray(IntExpr[]::new);

            Map<Integer, List<IntExpr>> countersToButtons = new HashMap<>();

            for (int i = 0; i < buttons.size(); i++) {
                IntExpr buttonVar = buttonVars[i];
                for (int flip : buttons.get(i)) {
                    countersToButtons.computeIfAbsent(flip, k -> new ArrayList<>()).add(buttonVar);
                }
            }

            for (Map.Entry<Integer, List<IntExpr>> entry : countersToButtons.entrySet()) {
                int counterIndex = entry.getKey();
                List<IntExpr> counterButtons = entry.getValue();

                IntExpr targetValue = ctx.mkInt(joltageRequirements.get(counterIndex));

                IntExpr[] buttonPressesArray = counterButtons.toArray(new IntExpr[0]);

                IntExpr sumOfButtonPresses = (IntExpr) ctx.mkAdd(buttonPressesArray);

                BoolExpr equation = ctx.mkEq(targetValue, sumOfButtonPresses);
                opt.Add(equation);
            }

            IntExpr zero = ctx.mkInt(0);
            for (IntExpr buttonVar : buttonVars) {
                BoolExpr nonNegative = ctx.mkGe(buttonVar, zero);
                opt.Add(nonNegative);
            }

            IntExpr sumOfAllButtonVars = (IntExpr) ctx.mkAdd(buttonVars);
            BoolExpr totalPressesEq = ctx.mkEq(presses, sumOfAllButtonVars);
            opt.Add(totalPressesEq);

            opt.MkMinimize(presses);

            Status status = opt.Check();

            if (status == Status.SATISFIABLE) {
                Model model = opt.getModel();
                IntNum outputValue = (IntNum) model.evaluate(presses, false);
                return outputValue.getInt();
            } else if (status == Status.UNSATISFIABLE) {
                System.out.println("Problem is UNSATISFIABLE (no solution exists).");
            } else {
                System.out.println("Optimization could not be determined (" + status + ").");
            }
            return -1000000;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 10, false, 0);
        List<FactoryMachines> factories = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(factories);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(factories);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    public List<FactoryMachines> parseLines(List<String> lines) {
        List<FactoryMachines> factories = new ArrayList<>();
        for (String line : lines) {
            int closeBracketLoc = line.indexOf(']');
            int openCurlyLoc = line.indexOf('{');
            String finalState = line.substring(1, closeBracketLoc).trim();
            String joltageLevel = line.substring(openCurlyLoc+1,line.length()-1).trim();
            String button = line.substring(closeBracketLoc + 3, openCurlyLoc-2).trim();
            List<Integer> finalLevel = Arrays.stream(joltageLevel.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<String> buttonList = Arrays.stream(button.split("\\) \\(")).toList();
            List<List<Integer>> buttons = new ArrayList<>();
            for (String buttonLine : buttonList) {
                List <Integer> buttonInts = Arrays.stream(buttonLine.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                buttons.add(buttonInts);
            }
            factories.add(new FactoryMachines(finalState, buttons, finalLevel));
        }
        return factories;
    }

    public int solutionPart1(List<FactoryMachines> factories) {
        return factories.stream().map(FactoryMachines::solve).reduce(0, Integer::sum);
    }

    public long solutionPart2(List<FactoryMachines> factories) {
        return factories.stream().map(FactoryMachines::solvePart2).reduce(0, Integer::sum);
    }

}
