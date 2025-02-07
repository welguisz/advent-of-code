package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class PasswordPhilosophy extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,2,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = lines.stream().filter(str -> validPassword(str, false)).count();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = lines.stream().filter(str -> validPassword(str, true)).count();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private boolean validPassword(String str, boolean part2) {
        String[] splitLine = str.split(": ");
        String password = splitLine[1];
        char requiredChar = splitLine[0].split(" ")[1].charAt(0);
        Integer minRequiredChar = parseInt(splitLine[0].split(" ")[0].split("-")[0]);
        Integer maxRequiredChar = parseInt(splitLine[0].split(" ")[0].split("-")[1]);
        if (part2) {
            return validPasswordPart2(password, requiredChar, minRequiredChar, maxRequiredChar);
        } else {
            return validPasswordPart1(password, requiredChar, minRequiredChar, maxRequiredChar);
        }
    }

    private boolean validPasswordPart1(String password, char requiredChar, Integer minRequireChar, Integer maxRequiredChar) {
        Long requiredCharCount = password.chars().filter(ch -> ch == requiredChar).count();
        return ((minRequireChar <= requiredCharCount) && (requiredCharCount <= maxRequiredChar));
    }

    private boolean validPasswordPart2(String password, char requiredChar, Integer minRequireChar, Integer maxRequiredChar) {
        char charAtMinLoc = password.charAt(minRequireChar-1);
        char charAtMaxLoc = password.charAt(maxRequiredChar-1);
        Integer value = 0;
        if (requiredChar == charAtMinLoc) {
            value++;
        }
        if (requiredChar == charAtMaxLoc) {
            value++;
        }
        return value == 1;
    }
}
