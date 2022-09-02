package com.dwelguisz.year2015;


import com.dwelguisz.base.AoCDay;

public class ElvesLookElvesSay extends AoCDay {
    public void solve() {
        Integer part1 = part1("1113222113", 40);
        Integer part2 = part1("1113222113", 50);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    static Integer part1(String newStr, Integer steps) {
        String tmpStr = newStr;
        for (int i = 0; i < steps; i++) {
            tmpStr = LookAndSay(tmpStr);
        }
        return tmpStr.length();
    }

    static String LookAndSay(String inputStr) {
        String[] digits = inputStr.split("");
        int currentPos = 0;
        StringBuffer newString = new StringBuffer();
        String currentNum = digits[0];
        int currentCount = 0;
        while (currentPos < digits.length) {
            if (digits[currentPos].equals(currentNum)) {
                currentCount++;
            } else if (currentCount > 0){
                newString.append(currentCount);
                newString.append(currentNum);
                currentCount = 1;
                currentNum = digits[currentPos];
            }
            currentPos++;
        }
        if (currentCount > 0) {
            newString.append(currentCount);
            newString.append(currentNum);
        }
        return newString.toString();
    }
}
