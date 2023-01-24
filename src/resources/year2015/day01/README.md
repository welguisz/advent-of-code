# Day 1: Not Quite Lisp

[Back to Top README file](../../../../README.md)
## Overview
Difficult Level: Easy

Input: A string that contains `(` and `)`

## Part 1 Solution

### Basic Solution
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

## Part 2
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

[Next Day](../day02/README.md)