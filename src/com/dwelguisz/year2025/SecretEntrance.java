package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.floorDiv;

public class SecretEntrance extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 1, false, 0);
        List<Integer> values = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
        documentation();
    }

    List<Integer> parseLines(List<String> lines) {
        List<Integer> values = new ArrayList<>();
        for(String line : lines) {
            int sign = line.substring(0,1).equals("R") ? 1 : -1;
            int value = Integer.parseInt(line.substring(1));
            values.add(sign * value);
        }
        return values;
    }

    public long solutionPart1(List<Integer> values) {
        int current = 50;
        int hit = 0;
        for (int value : values) {
            current += value;
            int remainder = ((current % 100) + 100) % 100;
            if (remainder == 0)
                hit++;
        }
        return hit;
    }

    public long solutionPart2(List<Integer> values) {
        int current = 50;
        int hit = 0;
        for (int value : values) {
            int newValue = current + value;
            if (current <= newValue)
                hit += abs(floorDiv(newValue, 100) - floorDiv(current, 100));
            else
                hit += abs(floorDiv(-newValue, 100) - floorDiv(-current, 100));
            current = newValue;
        }
        return hit;
    }

    void documentation() {
        puzzleName = "Secret Entrance";
        difficultLevel = "1 out of 10";
        inputDescription = "lines of strings with L/R follwed by integer";
        skills = List.of("floorDiv", "floorMod");
        setup = """
## Parsing
Parsing is straightforward with each line being of the format `[DIR][CLICKS]`.
This can be turn into a list of positive and negative numbers. If L, negative. If R, positive.

## General approach to both parts
We can just use a number line. So we start at 50 and add/subtract the list of numbers as we
go along. When we see `00`, we add to our count if we stop on it for part 1 and whenever we see
it in part 2.
""";

        part1Solution = """
For part1, we just add the numbers and do a floorMod. If the value of the floorMod is 0, we add to our count.

```java
        for (int value : values) {
            current += value;
            int remainder = ((current % 100) + 100) % 100;
            if (remainder == 0)
                hit++;
        }
        return hit;
```
""";
        part2Solution = """
For part2, we now have to see how many times it passes a number that ends in `00`.  This took a bit more
thought and had to use `floorDiv` so when we do an integer division, we go to the value closest to negative
infinity. If we use regular division, the quoitent will go to the value closet to 0. So if we do a -10/3,
`floorDiv` will return `-4`, while `regular division` will return `-3`.

If we are turning left (negative), we negate the values so we don't double count.

```java
        for (int value : values) {
            int newValue = current + value;
            if (current <= newValue)
                hit += abs(floorDiv(newValue, 100) - floorDiv(current, 100));
            else
                hit += abs(floorDiv(-newValue, 100) - floorDiv(-current, 100));
            current = newValue;
        }
```
""";
    }

}
