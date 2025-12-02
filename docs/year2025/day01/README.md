# Day 1 Secret Entrance

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2025/day/1)
* Difficult Level: 1 out of 10
* [Input](https://adventofcode.com/2025/day/1/input): lines of strings with L/R follwed by integer
* Skills/Knowledge: floorDiv, floorMod

## Setup

## Parsing
Parsing is straightforward with each line being of the format `[DIR][CLICKS]`.
This can be turn into a list of positive and negative numbers. If L, negative. If R, positive.

## General approach to both parts
We can just use a number line. So we start at 50 and add/subtract the list of numbers as we
go along. When we see `00`, we add to our count if we stop on it for part 1 and whenever we see
it in part 2.


## Part 1 Solution:

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


## Part 2 Solution:

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


## Times

* Parsing: 10 ms
* Part 1 Solve time: 0 ms
* Part 2 Solve time: 1 ms

## Solutions: 

* Part 1: 1145
* Part 2: 6561

| |
|---:|
[Previous (Year 2024, Day 25)](../../year2024/day25/README.md)