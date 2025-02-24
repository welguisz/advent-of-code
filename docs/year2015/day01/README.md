# Day 1 Not Quite Lisp

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/1)
* Difficult Level: 1 out of 10
* [Input](https://adventofcode.com/2015/day/1/input): String containing many `(` and `)`
* Skills/Knowledge: Streaming, while loops

## Part 1 Solution:

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


## Part 2 Solution:

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


## Times

* Parsing: 4 ms
* Part 1 Solve time: 1 ms
* Part 2 Solve time: 2 ms

## Solutions: 

* Part 1: 74
* Part 2: 1795

| |
|:---|
[Next (Year 2015, Day 2)](../../year2015/day02/README.md)