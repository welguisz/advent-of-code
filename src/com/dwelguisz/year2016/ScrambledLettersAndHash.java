package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.google.common.collect.Collections2;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScrambledLettersAndHash extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day21/input.txt");
        String part1 = scrambledLetters("abcdefgh", lines);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        String part2 = findUnscrambledPassword("fbgdceah", lines);
        System.out.println(String.format("Part 2 Answer: %s",part2));
    }

    public String findUnscrambledPassword(String goalString, List<String> lines) {
        Set<String> letters = Arrays.stream(goalString.split("")).collect(Collectors.toSet());
        for (List<String> letterArray : Collections2.permutations(letters)) {
            String tryThisString = letterArray.stream().collect(Collectors.joining());
            String outputString = scrambledLetters(tryThisString, lines);
            if (outputString.equals(goalString)) {
                return tryThisString;
            }
        }
        return "Bad thing happened here";
    }
    public String scrambledLetters(String inputStr, List<String> lines) {
        String currentStr = inputStr;
        for (String line : lines) {
            String cmdSplit[] = line.split(" ");
            if (cmdSplit[0].equals("swap")) {
                Integer firstPos = 0;
                Integer secondPos = 0;
                if (cmdSplit[1].equals("position")) {
                    firstPos = Integer.parseInt(cmdSplit[2]);
                    secondPos = Integer.parseInt(cmdSplit[5]);
                } else {
                    firstPos = currentStr.indexOf(cmdSplit[2]);
                    secondPos = currentStr.indexOf(cmdSplit[5]);
                }
                String currentStrSplit[] = currentStr.split("");
                String tmp = currentStrSplit[firstPos];
                currentStrSplit[firstPos] = currentStrSplit[secondPos];
                currentStrSplit[secondPos] = tmp;
                currentStr = Arrays.stream(currentStrSplit).collect(Collectors.joining());
            } else if (cmdSplit[0].equals("rotate")) {
                if (cmdSplit[1].equals("based")) {
                    Integer steps = currentStr.indexOf(cmdSplit[6]);
                    steps += (steps >= 4) ? 2 : 1;
                    while (steps >= currentStr.length()) {
                        steps -= currentStr.length();
                    }
                    String rightPart = currentStr.substring(currentStr.length()-steps);
                    String leftPart = currentStr.substring(0,currentStr.length()-steps);
                    currentStr = rightPart + leftPart;
                } else if (cmdSplit[1].equals("right")) {
                    Integer steps = Integer.parseInt(cmdSplit[2]);
                    String rightPart = currentStr.substring(currentStr.length()-steps);
                    String leftPart = currentStr.substring(0,currentStr.length()-steps);
                    currentStr = rightPart + leftPart;
                } else {
                    Integer steps = Integer.parseInt(cmdSplit[2]);
                    String rightPart = currentStr.substring(0, steps);
                    String leftPart = currentStr.substring(steps);
                    currentStr = leftPart + rightPart;
                }
            } else if (cmdSplit[0].equals("reverse")) {
                Integer pos1 = Integer.parseInt(cmdSplit[2]);
                Integer pos2 = Integer.parseInt(cmdSplit[4]);
                Integer small = Integer.min(pos1, pos2);
                Integer big = Integer.max(pos1, pos2);
                String beforeReverse = "";
                String afterReverse = "";
                if (small != 0) {
                    beforeReverse = currentStr.substring(0,small);
                }
                if (big != currentStr.length()-1) {
                    afterReverse = currentStr.substring(big+1);
                }
                String reverseStr = "";
                for(int tmp = big; tmp >= small; tmp--) {
                    reverseStr += currentStr.charAt(tmp);
                }
                currentStr = beforeReverse + reverseStr + afterReverse;
            } else if (cmdSplit[0].equals("move")) {
                Integer pos1 = Integer.parseInt(cmdSplit[2]);
                Integer pos2 = Integer.parseInt(cmdSplit[5]);
                if (pos1 < pos2) {
                    String firstPart = currentStr.substring(0,pos1);
                    String character = currentStr.substring(pos1, pos1+1);
                    String secondPart = currentStr.substring(pos1+1,pos2+1);
                    String lastPart = "";
                    if (pos2+1 < currentStr.length()) {
                        lastPart = currentStr.substring(pos2+1);
                    }
                    currentStr = firstPart + secondPart + character + lastPart;
                } else {
                    String firstPart = "";
                    if (pos2 > 0) {
                        firstPart = currentStr.substring(0, pos2);
                    }
                    String secondPart = currentStr.substring(pos2, pos1);
                    String character = currentStr.substring(pos1,pos1+1);
                    String lastPart = "";
                    if (pos1+1 < currentStr.length()) {
                        lastPart = currentStr.substring(pos1 + 1);
                    }
                    currentStr = firstPart + character + secondPart + lastPart;
                }
            }
        }
        return currentStr;
    }
}
