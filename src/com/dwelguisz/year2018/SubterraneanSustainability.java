package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SubterraneanSustainability extends AoCDay {
    Map<String, Character> nextGenPlant = new HashMap<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,12,false,0);
        String initialState = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(initialState, 20);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(initialState);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart2(String initialState) {
        Stack<Long> scores = new Stack<>();
        for (int i = 0; i <= 200; i++) {
            scores.push(solutionPart1(initialState, i));
        }
        Long difference = 0L;
        Integer scorePointer = 199;
        Long lastScore = scores.pop();
        Long lastScoreOnStack = lastScore;
        while(!scores.isEmpty()) {
            Long currentScore = scores.pop();
            if (scorePointer > 100) {
                if (lastScore - currentScore != difference) {
                    difference = lastScore - currentScore;
                    System.out.println("Difference at location " + scorePointer + " changed to " + difference);
                }
            }
            lastScore = currentScore;
            scorePointer--;
        }
        return ((50000000000L - 200) * difference) + lastScoreOnStack;
    }

    public Long solutionPart1(String initialState, Integer iterations) {
        ArrayDeque<Character> deque = new ArrayDeque<>();
        for (int i = 0; i < initialState.length(); i++) {
            deque.addLast(initialState.charAt(i));
        }
        Long currentZeroPot = 0L;
        for (Integer i = 0; i < iterations; i++) {
            Pair<Integer, ArrayDeque<Character>> result = processPots(deque);
            deque = result.getRight();
            currentZeroPot += result.getLeft();
        }
        Long score = 0L;
        Long pos = 0L;
        while (!deque.isEmpty()) {
            Character c = deque.pop();
            Long potNumber = pos - currentZeroPot;
            score += (c == '#') ?  potNumber : 0;
            pos++;
        }
        return score;
    }

    public Pair<Integer, ArrayDeque<Character>> processPots(ArrayDeque<Character> plants) {
        ArrayDeque<Character> newDeque = new ArrayDeque<>();
        Integer numberToZeroPot = 3;
        for (int i = 0; i < 5; i++) {
            plants.addFirst('.');
            plants.addLast('.');
        }
        String curStr = "";
        while (!plants.isEmpty()) {
            if (curStr.length() == 5) {
                newDeque.addLast(nextGenPlant.getOrDefault(curStr,'.'));
                curStr = curStr.substring(1);
                curStr += plants.removeFirst();
            } else {
                curStr += plants.removeFirst();
            }
        }
        boolean run = true;
        while (run) {
            if (newDeque.removeFirst() == '#') {
                run = false;
                newDeque.addFirst('#');
                numberToZeroPot++;
            }
            numberToZeroPot--;
        }
        run = true;
        while (run) {
            if (newDeque.removeLast() == '#') {
                run = false;
                newDeque.addLast('#');
            }
        }
        return Pair.of(numberToZeroPot,newDeque);
    }

    public String parseLines(List<String> lines) {
        String initialState = "";
        nextGenPlant = new HashMap<>();
        for (String l : lines) {
            if (l.contains("initial")) {
                initialState = l.substring(l.indexOf(':')+2);
            } else if (l.contains("=>")) {
                String sp[] = l.split(" => ");
                nextGenPlant.put(sp[0],sp[1].charAt(0));
            }
        }
        return initialState;
    }
}
