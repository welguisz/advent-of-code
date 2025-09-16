# Day 10 Elves Look Elves Say

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/10)
* Difficult Level: 3 out of 10
* [Input](https://adventofcode.com/2015/day/10/input): A 10 digit number that has numbers repeating
* Skills/Knowledge: positional, regex lookahead

## Setup

## Parsing
Parsing is straightforward and is just a number that is given. The biggest thing is to count
consecutive numbers.  So, if you have the number 112222111, it should be read as Two 1s (21), Four twos (42), Three 1s (31)

## General approach to both parts
There might be a way to do this with regex where you match the current position of the string and count the number of identical
numbers. There might be a way to do with regex but the approach of just counting the numbers for each step works.
This approach might fail if the number of steps gets too high. Right now, at 50 steps, it takes 125 ms. For 40 steps,
it took 27 ms.


## Part 1 Solution:

For part1, it asked for the length of the string after 40 steps.

```java
        for (int i = 0; i < steps; i++) {
            tmpStr = LookAndSay(tmpStr);
        }
```


## Part 2 Solution:

Same as part1, but with 50 steps.


## Times

* Parsing: 5 ms
* Part 1 Solve time: 23 ms
* Part 2 Solve time: 135 ms

## Solutions: 

* Part 1: 252594
* Part 2: 3579328

| | |
|:---|---:|
|[Previous (Year 2015, Day 9)](../../year2015/day09/README.md)|[Next (Year 2015, Day 11)](../../year2015/day11/README.md)|
