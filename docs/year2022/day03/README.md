# Day 3: RuckSack Reorganization

[Back to Top README file](../../../README.md)

## Overview
Difficult Level: Easy

Input: A text file that has random characters on multiple lines

## Solution

### Priority Scoring
In the question's prompt, it says that each letter has a priority.
```text
- Lowercase item types a through z has priorities 1 through 26.
- Uppercase item types A through Z has priorities 27 through 52.
```
You can write up a hashmap that says "a" -> 1, "b" -> 2, ... "Y" -> 51, "Z" -> 52. This is filled
with lots of typing and issues. If you want to do this in a simpler manner, you can just use the
ASCII table.  The character 'A' is value 65 and the character 'a' is value 97.  Knowing this, the
priority score can be found by:

```java
public Integer priorityScore(String str) {
    Integer val = Integer.valueOf(str.toCharArray()[0]);
    return (val < 96) ? val - 38 : val - 96;
}
```

### Part 1
For each line, split the line, find the common character in each half of the line. After finding the
common character, get the priorityScore for that character and add it to a running total.

```java
public Integer solutionPart1(List<String> lines) {
    Integer sum = 0;
    for (String line : lines) {
        Integer length = line.length()/2;
        List<String> firstHalf = convertStringToList(line.substring(0,length));
        List<String> secondHalf = convertStringToList(line.substring(length));
        String appearsInBoth = firstHalf.stream().filter(l -> secondHalf.contains(l)).collect(Collectors.toList()).get(0);
        sum += priorityScore(appearsInBoth);
    }
    return sum;
}
```

### Part 2
Now instead of looking at each line, we will look at lines in groups of 3 and find the common
character in all 3 lines.  Similar to Part 1.

```java
public Integer solutionPart2(List<String> lines) {
    Integer sum = 0;
    for (int i = 0; i < lines.size(); i+=3) {
        List<String> line0 = convertStringToList(lines.get(i));
        List<String> line1 = convertStringToList(lines.get(i+1));
        List<String> line2 = convertStringToList(lines.get(i+2));
        String inAllThree = line0.stream().filter(l -> line1.contains(l)).filter(l -> line2.contains(l)).collect(Collectors.toList()).get(0);
            sum += priorityScore(inAllThree);
        }
        return sum;
    }

```