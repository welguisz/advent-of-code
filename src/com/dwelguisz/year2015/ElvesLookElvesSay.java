package com.dwelguisz.year2015;


import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class ElvesLookElvesSay extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,10,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = part1(instructions.get(0), 40);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = part1(instructions.get(0), 50);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Integer part1(String newStr, Integer steps) {
        String tmpStr = newStr;
        for (int i = 0; i < steps; i++) {
            tmpStr = LookAndSay(tmpStr);
        }
        return tmpStr.length();
    }

    String LookAndSay(String inputStr) {
        char[] digits = inputStr.toCharArray();
        int currentPos = 0;
        StringBuffer newString = new StringBuffer();
        Character currentNum = digits[0];
        int currentCount = 0;
        while (currentPos < digits.length) {
            if (digits[currentPos] == currentNum) {
                currentCount++;
            } else if (currentCount > 0){
                newString.append(currentCount).append(currentNum);
                currentCount = 1;
                currentNum = digits[currentPos];
            }
            currentPos++;
        }
        if (currentCount > 0) {
            newString.append(currentCount).append(currentNum);
        }
        return newString.toString();
    }
}
