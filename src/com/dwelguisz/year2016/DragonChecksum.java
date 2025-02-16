package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class DragonChecksum extends AoCDay {
    public static Integer MY_LENGTH = 272;
    public static Integer SECOND_LENGTH = 35651584;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,16,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0), MY_LENGTH);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines.get(0), SECOND_LENGTH);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public String solutionPart1(String input, Integer length) {
        String disk = input;
        while (disk.length() < length) {
            disk = createNewInformation(disk);
        }
        disk = disk.substring(0,length);
        return createChecksum(disk);
    }

    public String createNewInformation(String initialString) {
        StringBuffer partB = new StringBuffer();
        for (int i = initialString.length()-1; i >= 0; i--) {
            if (initialString.charAt(i) == '0') {
                partB.append('1');
            } else {
                partB.append('0');
            }
        }
        return initialString + "0" + partB;
    }

    public String createChecksum(String disk) {
        String checksum = disk;
        while (checksum.length() %2 == 0) {
            StringBuffer newChecksum = new StringBuffer();
            for (int i = 0; i < checksum.length() / 2; i++) {
                if (checksum.charAt(i*2) == checksum.charAt(i*2+1)) {
                    newChecksum.append('1');
                } else {
                    newChecksum.append('0');
                }
            }
            checksum = newChecksum.toString();
        }
        return checksum;
    }

    public String solutionPart2(String input, Integer length) {
        // Step 0: 10111011
        // Step 1: 10111011 0 00100010
        // Step 2: 10111011 0 00100010 0 10111011 1 00100010
        // Step 3: 10111011 0 00100100 0 10111011 1 00100010 0 10111011 0 00100010 1 10110111 1 00100010
        // This can be reduced to the following (a is input and b is the revese string)
        // Step 0: a
        // Step 1: a0b
        // Step 2: a0b0a1b
        // Step 3: a0b0a1b0a0b1a1b
        // Step 4: a0b0a1b0a0b1a1b0a0b0a0b1a1b
        // Step 5: a0b0a1b0a0b1a1b0a0b0a0b1a1b0a0b0a
        // This can be reduced to the following
        // Step 0: -
        // Step 1: 0
        // Step 2: 001
        // Step 3: 0010011
        // Step 4: 001001100011011
        // So the expansion just has to be on the character 0
        int currentLength = input.length() * 2  + 1;
        String newStr = "0";
        while (currentLength < length) {
            newStr = createNewInformation(newStr);
            currentLength *= 2;
            currentLength += 1;
        }
        String reverseString = createNewInformation(input).substring(18);
        // Now we want to create a map that takes a string of length 8 and returns 1 character
        Map<String, String> checkSumMap = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            String tmpStr = String.format("%2s", Integer.toBinaryString(i)).replace(' ', '0');
            checkSumMap.put(tmpStr, createChecksum(tmpStr));
        }
        for (int i = 0; i < 16; i++) {
            String tmpStr = String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0');
            checkSumMap.put(tmpStr, createChecksum(tmpStr));
        }
        for (int i = 0; i < 256; i++) {
            String tmpStr = String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
            checkSumMap.put(tmpStr, createChecksum(tmpStr));
        }
        for (int i = 0; i < 256*256; i++) {
            String tmpStr = String.format("%16s", Integer.toBinaryString(i)).replace(' ', '0');
            checkSumMap.put(tmpStr, createChecksum(tmpStr));
        }
        // Now with the input String, reverseString, newStr, we can use checkSumMap to take substrings of
        // 8 and reduce it down several times and when the length is not divisible by 8, go to checkSum of 4.
        return checkSumUsingAdvanceStep(input, reverseString, newStr, checkSumMap, length);
    }

    public String checkSumUsingAdvanceStep(String a, String b, String newStr, Map<String, String> map, Integer length) {
        int divisor = 16;
        String composedString = composeStr(a,b,newStr,map,length);
        while (composedString.length() % divisor != 0) {
            divisor >>= 1;
        }
        if (divisor == 1) {
            return composedString;
        }
        while (composedString.length() % divisor == 0) {
            composedString = easierMap(composedString, map, divisor);
            while (composedString.length() % divisor != 0) {
                divisor >>= 1;
            }
            if (divisor == 1) {
                return composedString;
            }
        }
        return composedString;
    }

    public String easierMap(String str, Map<String, String> map, Integer step) {
        StringBuffer newStr = new StringBuffer();
        for (int i = 0; i < str.length(); i += step) {
            newStr.append(map.get(str.substring(i,i+step)));
        }
        return newStr.toString();
    }
    public String composeStr(String a, String b, String newStr, Map<String, String> map, Integer length) {
        int currentPos = 0;
        int newStrLoc = 0;
        StringBuffer tmp = new StringBuffer();
        while (currentPos < length) {
            String splice = "";
            String possibleChar = newStr.substring(newStrLoc, newStrLoc+1);
            switch ((currentPos/16) % 9) {
                case 0: {
                    splice = a.substring(0,16);
                    break;
                }
                case 1: {
                    splice = a.substring(16) + possibleChar + b.substring(0,14);
                    newStrLoc++;
                    break;
                }
                case 2: {
                    splice = b.substring(14) + possibleChar + a.substring(0,12);
                    newStrLoc++;
                    break;
                }
                case 3: {
                    splice = a.substring(12) + possibleChar + b.substring(0,10);
                    newStrLoc++;
                    break;
                }
                case 4: {
                    splice = b.substring(10) + possibleChar + a.substring(0,8);
                    newStrLoc++;
                    break;
                }
                case 5: {
                    splice = a.substring(8) + possibleChar + b.substring(0,6);
                    newStrLoc++;
                    break;
                }
                case 6: {
                    splice = b.substring(6) + possibleChar + a.substring(0,4);
                    newStrLoc++;
                    break;
                }
                case 7: {
                    splice = a.substring(4) + possibleChar + b.substring(0,2);
                    newStrLoc++;
                    break;
                }
                case 8: {
                    splice = b.substring(2) + possibleChar;
                    newStrLoc++;
                    break;
                }
            }
            tmp.append(map.get(splice));
            currentPos += 16;
        }
        return tmp.toString();
    }
}
