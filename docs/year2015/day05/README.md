# Day 5 Doesn't He Have Intern-Elves For This?

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/5)
* Difficult Level: 2 out of 10
* [Input](https://adventofcode.com/2015/day/5/input): List of random strings
* Skills/Knowledge: Lambda functions, Streaming, Filtering

## Part 1 Solution:

For a String to be nice, it has to meet the following requirements:
* Contains at least 3 vowels
* Contains at least one letter that appears twice in a row
* Does not contain the strings `ab`, `cd`, `pq`, or `xy`.

### Strategy
Create methods that return true if the above condition is met

```java

    public boolean vowels(String str) {
        return Arrays.stream(str.split("")).filter(chr -> VOWELS.contains(chr)).count() > 2;
    }

    public boolean containsDoubleLetters(String str) {
        char[] chars = str.toCharArray();
        return IntStream.range(0,str.length()-1).anyMatch(i -> chars[i] == chars[i+1]);
    }

    public boolean doesNotContainBadLetters(String str) {
        List<String> badSet = Arrays.asList("ab", "cd", "pq", "xy");
        List<String> twoLetterStrings = IntStream.range(0,str.length()-1).boxed().map(i -> str.substring(i,i+2)).collect(Collectors.toList());
        return !twoLetterStrings.stream().anyMatch(ss -> badSet.contains(ss));
    }
```

This now allows for a simple stream function to be written

```java
    Long solutionPart1(List<String> strings) {
        return strings.stream()
                .filter(str -> vowels(str))
                .filter(str->containsDoubleLetters(str))
                .filter(str->doesNotContainBadLetters(str)).count();
    }
```


## Part 2 Solution:

The rules for a nice String has changed. Now it has to meet this requirements:
* A pair of two letters that appears at least twice in the string without overlapping
* Contains at least one letter that repeats with a letter between them.

Same logic as above:

```java
public boolean containsDoubleLettersTwice(String str) {
   List<String> subStrings = IntStream.range(0,str.length()-1).boxed().map(i -> str.substring(i,i+2)).collect(Collectors.toList());
   for (int i = 0; i < subStrings.size(); i++) {
       for (int j = i + 2; j < subStrings.size(); j++) {
           if (subStrings.get(i).equals(subStrings.get(j))) {
               return true;
           }
       }
   }
   return false;
}

public boolean letterSpace(String str) {
   char[] chars = str.toCharArray();
   return IntStream.range(0,str.length()-2).anyMatch(i -> chars[i] == chars[i+2]);
}

Long solutionPart2(List<String> strings) {
   return strings.stream()
           .filter(str -> containsDoubleLettersTwice(str))
           .filter(str -> letterSpace(str)).count();
   }
```


## Times

* Parsing: 6 ms
* Part 1 Solve time: 17 ms
* Part 2 Solve time: 7 ms

## Solutions: 

* Part 1: 238
* Part 2: 69

| | |
|:---|---:|
|[Previous (Year 2015, Day 4)](../../year2015/day04/README.md)|[Next (Year 2015, Day 6)](../../year2015/day06/README.md)|
