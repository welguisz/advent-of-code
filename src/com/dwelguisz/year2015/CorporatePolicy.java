package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class CorporatePolicy extends AoCDay {
    public void solve() {
        String oldPassword = "cqjxjnds";
        String part1 = solutionPart1(oldPassword);
        String part2 = solutionPart1(part1);
        System.out.println(String.format("Part 1 Answer: %s", part1));
        System.out.println(String.format("Part 2 Answer: %s", part2));
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
            if (tmp > 'z') {
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
        char chars[] = currentPassword.toCharArray();
        boolean threeIncreasingChars = checkForIncreasingChars(chars);
        boolean noBadLetters = doesNotContainBadLetters(chars);
        boolean containsDoubleLetters = doesContainTwoDoubleLetters(chars);
        return threeIncreasingChars && noBadLetters && containsDoubleLetters;
    }

    boolean checkForIncreasingChars(char chars[]) {
        char threeChars[][] = {{chars[0], chars[1], chars[2]},{chars[2], chars[3], chars[4]},
                {chars[3], chars[4], chars[5]},{chars[4], chars[5], chars[6]},{chars[5], chars[6], chars[7]}};
        for (int i = 0; i < threeChars.length; i++) {
            if ((threeChars[i][0] + 1 == threeChars[i][1]) && (threeChars[i][0] + 2 == threeChars[i][2])) {
                return true;
            }
        }
        return false;
    }

    boolean doesNotContainBadLetters(char chars[]) {
        List<Character> badChars = new ArrayList<>();
        badChars.add('i');
        badChars.add('o');
        badChars.add('l');
        for (int i = 0; i < chars.length; i++) {
            if (badChars.contains(chars[i])) {
                return false;
            }
        }
        return true;
    }

    boolean doesContainTwoDoubleLetters(char chars[]) {
        char firstByte[][] = {{chars[0], chars[1]}, {chars[1], chars[2]}, {chars[2], chars[3]},
                {chars[3], chars[4]}, {chars[4], chars[5]}, {chars[5], chars[6]}, {chars[6],chars[7]}};
        int count = 0;
        char firstDoubleLetter[] = new char[2];
        for (int i = 0; i < firstByte.length; i++) {
            if (count == 0) {
                if (firstByte[i][0] == firstByte[i][1]) {
                    firstDoubleLetter = firstByte[i];
                    count++;
                }
            } else {
                if (firstByte[i][0] == firstByte[i][1]) {
                    if (firstDoubleLetter[0] != firstByte[i][0]) {
                        count++;
                    }
                }
            }
        }
        if (count == 2) {
            return true;
        }
        return false;
    }

}
