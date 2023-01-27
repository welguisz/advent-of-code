# Day 10: Elves Look, Elves Say

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy

Input: An Integer


## General approach to both parts
There might be an elegant solution, but I couldn't think of one.  The only thing
that I could think was a while loop that counts the number of consecutive
characters and when it encounters a difference, add to the next string. This
produces the following function:

```java
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
```

For both parts, run the string through the loop the requested time.

|[Previous (Day 9)](../day09/README.md)|[Next (Day 11)](../day11/README.md)|