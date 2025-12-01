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
        printExplanation = true;
        documentation();
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

    void documentation() {
        puzzleName = "Elves Look Elves Say";
        difficultLevel = "3 out of 10";
        inputDescription = "A 10 digit number that has numbers repeating";
        skills = List.of("positional, regex lookahead");
        setup = """
## Parsing
Parsing is straightforward and is just a number that is given. The biggest thing is to count
consecutive numbers.  So, if you have the number 112222111, it should be read as Two 1s (21), Four twos (42), Three 1s (31) 

## General approach to both parts
There might be a way to do this with regex where you match the current position of the string and count the number of identical
numbers. There might be a way to do with regex but the approach of just counting the numbers for each step works.
This approach might fail if the number of steps gets too high. Right now, at 50 steps, it takes 125 ms. For 40 steps,
it took 27 ms.
""";

        part1Solution = """
For part1, it asked for the length of the string after 40 steps.

```java
        for (int i = 0; i < steps; i++) {
            tmpStr = LookAndSay(tmpStr);
        }
```
""";
        part2Solution = """
Same as part1, but with 50 steps.
""";
    }
}
