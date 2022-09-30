package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class InternetProtocolVersion7 extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day07/input.txt");
        System.out.println(String.format("aba[bab]xyz returns %b",supportsSSL("aba[bab]xyz")));
        System.out.println(String.format("xyx[xyx]xyx returns %b",supportsSSL("xyx[xyx]xyx")));
        System.out.println(String.format("aaa[kek]eke returns %b",supportsSSL("aaa[kek]eke")));
        System.out.println(String.format("zazbz[bzb]cdb returns %b",supportsSSL("zazbz[bzb]cdb")));
        Integer part1 = solutionPart1(lines);
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines) {
        Integer count = 0;
        for (String line : lines) {
            if (supportsTLS(line)) {
                count++;
            }
        }
        return count;
    }

    public Integer solutionPart2(List<String> lines) {
        Integer count = 0;
        for (String line : lines) {
            if (supportsSSL(line)) {
                count++;
            }
        }
        return count;
    }


    public Boolean supportsSSL(String line) {
        List<String> abaOutsideBrackets = new ArrayList<>();
        List<String> babInsideBrackets = new ArrayList<>();
        Boolean insideBracket = false;
        char charArray[] = line.toCharArray();
        for (int i = 0; i < line.length() - 2; i++) {
            char firstLetter = charArray[i];
            char secondLetter = charArray[i+1];
            char thirdLetter = charArray[i+2];
            if (firstLetter == '[') {
                insideBracket = true;
            } else if (firstLetter == ']') {
                insideBracket = false;
            } else if (firstLetter == thirdLetter && firstLetter != secondLetter) {
                String sb = String.valueOf(new StringBuffer().append(firstLetter).append(secondLetter).append(thirdLetter));
                if (insideBracket) {
                    babInsideBrackets.add(sb);
                } else {
                    abaOutsideBrackets.add(sb);
                }
            }
        }
        for (String out : abaOutsideBrackets) {
            char tmp[] = out.toCharArray();
            String findThis = String.valueOf(new StringBuffer().append(tmp[1]).append(tmp[0]).append(tmp[1]));
            if (babInsideBrackets.contains(findThis)) {
                return true;
            }
        }
        return false;
    }
    public Boolean supportsTLS(String line) {
        Boolean possible = false;
        Boolean insideBracket = false;
        char charArray[] = line.toCharArray();
        for (int i = 0; i < line.length() - 3; i++) {
            char firstLetter = charArray[i];
            char secondLetter = charArray[i+1];
            char thirdLetter = charArray[i+2];
            char fourthLetter = charArray[i+3];
            if (firstLetter == '[') {
                insideBracket = true;
            } else if (firstLetter == ']') {
                insideBracket = false;
            } else if (firstLetter == fourthLetter && secondLetter == thirdLetter && firstLetter != secondLetter) {
                if (insideBracket) {
                    return false;
                } else {
                    possible = true;
                }
            }

        }
        return possible;
    }
}
