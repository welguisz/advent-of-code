# Day 6 Probably a Fire Hazard

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/6)
* Difficult Level: 2 out of 10
* [Input](https://adventofcode.com/2015/day/6/input): List of instructions to turn on/off lights
* Skills/Knowledge: Grid Operations, Processing instructions

## Setup

### Parsing
For the parsing portion, I created a class that holds the following information:
* Toggle
* Turn On
* Starting Coordinate
* Ending Coordinate

This class also includes a function that will return all points from the upper left
corner of the square (starting coordinate) to the lower right corner of the 
square (ending coordinate).


## Part 1 Solution:

Create a Map that has a Key of the `Coord2D` and value of `Boolean`. Depending on
the instruction, change the value to `true`, `false`, or flip the value.

```java
    Long solutionPart1(List<Instruction> instructions) {
        Map<Coord2D, Boolean> map = new HashMap<>();
        for (Instruction instruction : instructions) {
            List<Coord2D> points = instruction.pointsToGet();
            for (Coord2D point : points) {
                Boolean value = map.getOrDefault(point, false);
                if (instruction.toggle) {
                    value ^= true;
                } else {
                    if (instruction.turnOn) {
                        value = true;
                    } else {
                        value = false;
                    }
                }
                map.put(point, value);
            }
        }
        return map.entrySet().stream().filter(e -> e.getValue()).count();
    }
```


## Part 2 Solution:

Similar to Part 1, but instead of Boolean, use Integer. The brightness of the lights
can't be negative.

```java
    Integer solutionPart2(List<Instruction> instructions) {
        Map<Coord2D, Integer> map = new HashMap<>();
        for (Instruction instruction : instructions) {
            List<Coord2D> points = instruction.pointsToGet();
            for (Coord2D point : points) {
                Integer value = map.getOrDefault(point, 0);
                if (instruction.toggle) {
                    value += 2;
                } else {
                    if (instruction.turnOn) {
                        value += 1;
                    } else {
                        value -= (value == 0) ? 0 : 1;
                    }
                }
                map.put(point, value);
            }
        }
        return map.entrySet().stream().mapToInt(e -> e.getValue()).sum();
    }
```


## Times

* Parsing: 10 ms
* Part 1 Solve time: 3753 ms
* Part 2 Solve time: 3414 ms

## Solutions: 

* Part 1: 543903
* Part 2: 14687245

| | |
|:---|---:|
|[Previous (Year 2015, Day 5)](../../year2015/day05/README.md)|[Next (Year 2015, Day 7)](../../year2015/day07/README.md)|
