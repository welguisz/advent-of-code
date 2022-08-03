package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class SecurityThroughObscurity extends AoCDay {

    List<Pair<Integer,String>> validRooms;

    public void solve() {
        validRooms = new ArrayList<>();
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2016/day04/input.txt");
        Long part1 = solutionPart1(lines);
        Integer part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long solutionPart1(List<String> lines) {
        Long runningSum = 0L;
        for (String line: lines) {
            String[] firstCut = line.split("\\[");
            String checkSum = firstCut[1].split("\\]")[0];
            String[] secondCut = firstCut[0].split("-");
            Integer sectorId = Integer.parseInt(secondCut[secondCut.length-1]);
            PriorityQueue<Pair<Character, Long>> queue =
                    new PriorityQueue<Pair<Character, Long>>(26,
                            (a,b) -> {
                        int value = (int) (b.getRight() - a.getRight());
                        if (value == 0) {
                            return a.getLeft() - b.getLeft();
                        }
                        return value;
                            });
            StringBuffer sb = new StringBuffer();
            StringBuffer sbCheck = new StringBuffer();
            for (int i = 0; i < secondCut.length - 1; i++) {
                sb.append(secondCut[i]);
                sbCheck.append(secondCut[i]);
                if (i < secondCut.length - 2) {
                    sbCheck.append("-");
                }
            }
            String justChars = sb.toString();
            while(justChars.length() > 0) {
                Character firstChar = justChars.charAt(0);
                Long firstCharCount = justChars.chars().filter(ch -> ch == firstChar).count();
                queue.add(Pair.of(firstChar, firstCharCount));
                justChars = justChars.replaceAll(firstChar.toString(), "");
            }
            //System.out.println("Queue: " + queue);
            StringBuffer sbNew = new StringBuffer();
            for (int i = 0; i < 5; i++) {
                sbNew.append(queue.poll().getLeft());
            }
            if (sbNew.toString().equals(checkSum)) {
                validRooms.add(Pair.of(sectorId, sbCheck.toString()));
                runningSum += sectorId;
            }
        }

        return runningSum;
    }

    public Integer solutionPart2() {
        List<String> easterWords = new ArrayList<>();
        easterWords.add("rabbit");
        easterWords.add("flower");
        easterWords.add("basket");
        easterWords.add("grass");
        easterWords.add("chocolate");
        easterWords.add("dye");
        easterWords.add("egg");
        easterWords.add("candy");
        easterWords.add("bunny");
        for(Pair<Integer, String> validRoom : validRooms) {
            Integer sectorId = validRoom.getLeft();
            String encryptedName = validRoom.getRight();
            Integer shiftNum = sectorId % 26;
            String decryptedName = deCrypt(encryptedName, shiftNum);
            if (doesNotContainEasterWords(decryptedName, easterWords)) {
                System.out.println(sectorId + ": " + decryptedName);
            }
        }
        return validRooms.get(0).getLeft();
    }

    private boolean doesNotContainEasterWords(String targetStr, List<String> easterWords) {
        List<String> words = Arrays.stream(targetStr.split(" ")).collect(Collectors.toList());
        for (String word : words) {
            if (easterWords.contains(word)) {
                return false;
            }
        }
        return true;
    }

    private String deCrypt(String encryptedName, Integer shiftNum) {
        StringBuffer sb = new StringBuffer();
        encryptedName.chars().forEach(chr -> {
            if (chr == '-') {
                sb.append(" ");
            } else {
                int newChar = chr + shiftNum;
                if (newChar > 'z') {
                    newChar -= 26;
                }
                sb.append((char)newChar);
            }
        });
        return sb.toString();
    }
}
