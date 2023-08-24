# Day 2: Rock Paper Scissors

[Back to Top README file](../../../README.md)

## Overview
Difficult Level: Easy

Input: A text file that has 2 characters per line separated by a space.

## Overall Solution
There are nine possible values for each line.  For both parts, the first character action stays 
the same. So 'A' means 'Rock', 'B' for 'Paper' and 'C' for 'Scissors'.  The second character will
change for each part. For Part 1, 'X' means 'Rock', 'Y' means 'Paper', and 'Z' means 'Scissors'.
For Part 2, 'X' means 'Lose', 'Y' means 'Draw', and 'Z' means 'Win'.

This can make an easy table to see what the scores will be.

|Strategy Action| Part 1 | Part 2 |
|:--:|:------:|:------:|
|A X |   4    |   3    |
|A Y |   8    |   4    |
|A Z |   3    |   8    |
|B X |   1    |   1    |
|B Y |   5    |   5    |
|B Z |   9    |   9    |
|C X |   7    |   2    |
|C Y |   2    |   6    |
|C Z |   6    |   7    |

We can make a Map that has the key of "Strategy Action" pointing to the point value.

```java
Map<String, Integer> scoreMap = Map.of(
        "A X", part2? 3 : 4,
        "A Y", part2? 4 : 8,
        "A Z", part2? 8 : 3,
        "B X", 1,
        "B Y", 5,
        "B Z", 9,
        "C X", part2? 2 : 7,
        "C Y", part2? 6 : 2 ,
        "C Z", part2? 7 : 6);
```
From this map, we can just do the following for both parts.

```java
lines.stream().mapToInt(s -> scoreMap.get(s)).sum()
```