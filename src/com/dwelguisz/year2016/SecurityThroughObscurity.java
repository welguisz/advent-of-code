package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class SecurityThroughObscurity extends AoCDay {

    List<Pair<Integer,String>> validRooms;

    public void solve() {
        validRooms = new ArrayList<>();
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,4,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
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
        for(Pair<Integer, String> validRoom : validRooms) {
            Integer sectorId = validRoom.getLeft();
            String encryptedName = validRoom.getRight();
            Integer shiftNum = sectorId % 26;
            String decryptedName = deCrypt(encryptedName, shiftNum);
            if (decryptedName.equals("northpole object storage")) {
                return sectorId;
            }
        }
        return validRooms.get(0).getLeft();
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
