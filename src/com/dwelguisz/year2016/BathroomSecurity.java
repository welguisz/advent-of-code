package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BathroomSecurity extends AoCDay {
    Map<String, String> allowedNextDown;
    Map<String, String> allowedNextUp;
    Map<String, String> allowedNextRight;
    Map<String, String> allowedNextLeft;
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2016/day02/input.txt");
        allowedNextDown = new HashMap<>();
        allowedNextUp = new HashMap<>();
        allowedNextRight = new HashMap<>();
        allowedNextLeft = new HashMap<>();
        allowedNextDown.put("1","3");
        allowedNextDown.put("2","6");
        allowedNextDown.put("3","7");
        allowedNextDown.put("4","8");
        allowedNextDown.put("6","A");
        allowedNextDown.put("7","B");
        allowedNextDown.put("8","C");
        allowedNextDown.put("B","D");
        allowedNextUp.put("D","B");
        allowedNextUp.put("C","8");
        allowedNextUp.put("B","7");
        allowedNextUp.put("A","6");
        allowedNextUp.put("8","4");
        allowedNextUp.put("7","3");
        allowedNextUp.put("6","2");
        allowedNextUp.put("3","1");
        allowedNextRight.put("2","3");
        allowedNextRight.put("3","4");
        allowedNextRight.put("5","6");
        allowedNextRight.put("6","7");
        allowedNextRight.put("7", "8");
        allowedNextRight.put("8", "9");
        allowedNextRight.put("A", "B");
        allowedNextRight.put("B", "C");
        allowedNextLeft.put("C","B");
        allowedNextLeft.put("B","A");
        allowedNextLeft.put("9","8");
        allowedNextLeft.put("8","7");
        allowedNextLeft.put("7","6");
        allowedNextLeft.put("6","5");
        allowedNextLeft.put("4","3");
        allowedNextLeft.put("3","2");
        String part1 = solutionPart1(lines);
        String part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        System.out.println(String.format("Part 1 Answer: %s",part2));
    }

    public String solutionPart1(List<String> lines) {
        Integer currentButton = 5;
        StringBuffer sb = new StringBuffer();
        for (String line : lines) {
            String[] directions = line.split("");
            for (String direction : directions) {
                currentButton = calculateNextButton(currentButton, direction);
            }
            sb.append(currentButton);
        }
        return sb.toString();
    }

    public String solutionPart2(List<String> lines) {
        String currentButton = "5";
        StringBuffer sb = new StringBuffer();
        for (String line : lines) {
            String[] directions = line.split("");
            for (String direction : directions) {
                currentButton = calculateAdvancedNextButton(currentButton, direction);
            }
            sb.append(currentButton);
        }
        return sb.toString();
    }

    public String calculateAdvancedNextButton(String currentButton, String direction) {
        if ("D".equals(direction)) {
            return allowedNextDown.getOrDefault(currentButton, currentButton);
        } else if ("U".equals(direction)) {
            return allowedNextUp.getOrDefault(currentButton, currentButton);
        } else if ("L".equals(direction)) {
            return allowedNextLeft.getOrDefault(currentButton, currentButton);
        } else {
            return allowedNextRight.getOrDefault(currentButton, currentButton);
        }
    }

    public Integer calculateNextButton(Integer currentButton, String direction) {
        if ("D".equals(direction)) {
            Integer nextButton = currentButton + 3;
            if (nextButton > 9) {
                nextButton -= 3;
            }
            return nextButton;
        } else if ("U".equals(direction)) {
            Integer nextButton = currentButton - 3;
            if (nextButton < 1) {
                nextButton += 3;
            }
            return nextButton;
        } else if ("L".equals(direction)) {
            List<Integer> doNothing = new ArrayList<>();
            doNothing.add(1);
            doNothing.add(4);
            doNothing.add(7);
            if (doNothing.contains(currentButton)) {
                return currentButton;
            }
            return currentButton -1;
        } else {
            List<Integer> doNothing = new ArrayList<>();
            doNothing.add(3);
            doNothing.add(6);
            doNothing.add(9);
            if (doNothing.contains(currentButton)) {
                return currentButton;
            }
            return currentButton + 1;
        }
    }
}
