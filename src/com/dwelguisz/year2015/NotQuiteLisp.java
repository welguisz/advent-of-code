package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.io.EOFException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NotQuiteLisp extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,1,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(instructions.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(instructions.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
        documentation();
    }

    int solutionPart2(String input) {
        int level = 0;
        int position = 0;
        List<Integer> inputChrs = input.chars().boxed().toList();
        while (level != -1) {
            Integer chr = inputChrs.get(position);
            level += (chr == '(') ? 1 : -1;
            position++;
        }
        return position;
    }

    long solutionPart1(String input) {
        long openParentheses = input.chars().filter(s -> s == '(').count();
        return (2*openParentheses) - input.length();
    }

    void documentation() {
        puzzleName = "Not Quite Lisp";
        difficultLevel = "1 out of 10";
        inputDescription = "String containing many `(` and `)`";
        skills = List.of("Streaming", "while loops");
        part1Solution = """
Go through each character in the string. If the character is `(`, add 1 to the count. 
If the character is `)`, then subtract 1. The basic solution would look like:

```java
    Long solutionPart1(List<String> input) {
        Long answer = 0L;
        for(String in : input) {
            if ("(".equals(in)) {
                answer++;
            } else if (")".equals(in)) {
                answer--;
            }
        }
        return answer;
    }
```

### Using Java Stream
The above logic can be translated easily to a stream, like

```java
    Long solutionPart1(String input) {
        Long openParentheses = input.chars().filter(s -> s == '(').count();
        return (2*openParentheses) - input.length();
    }
```
""";
        part2Solution = """
For Part2, there is not a way to use streams to find the answer. A good solution will
use a loop and return the position when the level is -1.

```java
    Integer solutionPart2(String input) {
        Integer level = 0;
        Integer position = 0;
        List<Integer> inputChrs = input.chars().boxed().collect(Collectors.toList());
        while (level != -1) {
            Integer chr = inputChrs.get(position);
            level += (chr == '(') ? 1 : -1;
            position++;
        }
        return position;
    }

```
""";

    }
}
