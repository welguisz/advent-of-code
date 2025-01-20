package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CorporatePolicy extends AoCDay {
    public static List<Character> BAD_CHARS = List.of('i','o','l');

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,11,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(instructions.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1((String) part1Answer);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public String solutionPart1(String oldPassword) {
        String newPassword = incrementPassword(oldPassword);
        while (!passwordValid(newPassword)) {
            newPassword = incrementPassword(newPassword);
        }
        return newPassword;
    }

    public String incrementPassword(String oldPassword) {
        char chars[] = oldPassword.toCharArray();
        char newChars[] = oldPassword.toCharArray();
        char carry = 1;
        for (int i = chars.length-1; i >= 0; i--) {
            char tmp = (char) (chars[i] + carry);
            if (BAD_CHARS.contains(tmp)) {
                tmp = (char) (tmp + carry);
                for (int j = i+1; j < chars.length;j++) {
                    newChars[j] = 'a';
                }
                carry = 0;
            }
            else if (tmp > 'z') {
                carry = 1;
                tmp = 'a';
            } else {
                carry = 0;
            }
            newChars[i] = tmp;
        }
        return String.valueOf(newChars);
    }

    public boolean passwordValid(String currentPassword) {
        char charsT[] = currentPassword.toCharArray();
        Character chars[] = new Character[charsT.length];
        for (int i = 0; i < charsT.length; i++) {
            chars[i] = charsT[i];
        }
        List<Boolean> passwordCriteria = List.of(checkForIncreasingChars(chars), doesContainTwoDoubleLetters(chars));
        return passwordCriteria.stream().allMatch(t -> t);
    }

    boolean checkForIncreasingChars(Character chars[]) {
        List<List<Character>> characterGroups = IntStream.range(0,chars.length-2).boxed().map(i -> List.of(chars[i],chars[i+1],chars[i+2])).collect(Collectors.toList());
        return characterGroups.stream().anyMatch(cg -> ((cg.get(0)+1) == cg.get(1)) && ((cg.get(1)+1) == cg.get(2)));
    }

    boolean doesContainTwoDoubleLetters(Character chars[]) {
        List<List<Character>> characterGroups = IntStream.range(0,chars.length-1).boxed().map(i -> List.of(chars[i],chars[i+1])).collect(Collectors.toList());
        Set<List<Character>> doubleLetters = characterGroups.stream().filter(cg -> cg.get(0) == cg.get(1)).collect(Collectors.toSet());
        return (doubleLetters.size() == 2);
    }
}
