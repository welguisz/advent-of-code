# Day 8 Matchsticks

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/8)
* Difficult Level: 2 out of 10
* [Input](https://adventofcode.com/2015/day/8/input): lines of strings that have been encoded
* Skills/Knowledge: regex

## Setup

This puzzle becomes very easy if you have the right regex expression. With the 
right regex expression, the puzzle becomes a straightforward counting exercise.

## Special Regex String

```java
Pattern pattern = Pattern.compile("\\\\(\\\\|\\\"|x[0123456789abcdef]{2})");
```

The above regex says look for `\` followed by one of these conditions:
* `\`
* `"`
* `x00` -> `xff` (hexadecimal)


## Part 1 Solution:

With the above regex, we can write the solution as:

```java
    public Integer solutionPart1(List<String> strings) {
        return strings.stream().mapToInt(s->2 + matchedCharacters(s)).sum();
    }

    public int matchedCharacters(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int diff = matcher.end() - matcher.start();
            count += diff - 1;
        }
        return count;
    }

```

Why not write the above as:

```java
Integer allChars = strings.stream().mapToInt(s -> s.length());
Integer NoneEscapedCharacters = strings.stream().mapToInt(s -> s.length()-2-matchedCharacters(s)).sum();
return allChars - NoneEscapedCharacters;
```

If we do some math, we can rewrite it as:
```java
return strings.stream().mapToInt(s -> s.length() - (s.length()-2-matchedCharacters(s))).sum();
```

The `mapToInt porition` can be rewritten as `s -> s.length() - s.length() + 2 + matchedCharacters(s)`,
which can be reduced to `s -> 2 + matchedCharacters(s)`


## Part 2 Solution:

```java
    public Integer solutionPart2(List<String> strings) {
        return strings.stream().mapToInt(s -> 4 + matchedCharactersPart2(s)).sum();
    }

    public int matchedCharactersPart2(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String subString = str.substring(matcher.start(), matcher.end());
            if (subString.contains("\\x")) {
                count += 1;
            } else if (subString.equals("\\\"")) {
                count += 2;
            } else if (subString.equals("\\\\")) {
                count += 2;
            }
        }
        return count;
    }

```


## Times

* Parsing: 4 ms
* Part 1 Solve time: 2 ms
* Part 2 Solve time: 1 ms

## Solutions: 

* Part 1: 1342
* Part 2: 2074

| | |
|:---|---:|
|[Previous (Year 2015, Day 7)](../../year2015/day07/README.md)|[Next (Year 2015, Day 9)](../../year2015/day09/README.md)|
