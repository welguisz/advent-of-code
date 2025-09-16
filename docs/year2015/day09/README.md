# Day 9 Matchsticks

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/9)
* Difficult Level: 2 out of 10
* [Input](https://adventofcode.com/2015/day/9/input): lines of strings that have been encoded
* Skills/Knowledge: regex

## Setup

## Parsing
Parsing is straightforward with each line being of the format `City1 to 
City2 = distance`. From this data, we can create a HashMap of distances between
cities and a Set of cities.

## General approach to both parts
Since we know the distance from City1 to City2 and we need to visit all cities, we
can create all permutations for how Santa would travel. For my input, there are 8
cities, so there will be 8! different permutations. To find all distances, we
would use the following code:

```java
        Collection<List<String>> permutations = Collections2.permutations(places);
        List<Integer> travelledDistance = new ArrayList<>();
        for (List<String> p : permutations) {
            Integer distance = 0;
            for(int i = 0; i < p.size()-1; i++) {
                distance += distances.get(Pair.of(p.get(i),p.get(i+1)));
            }
            travelledDistance.add(distance);
        }
```


## Part 1 Solution:

For part1, it asked for the minimum distance that Santa could travelled. So that would be:

```java
return travelledDistance.stream().mapToInt(d -> d).min().getAsInt();
```


## Part 2 Solution:

For part2, just changed minimum to maximum and we get:
```java
return travelledDistance.stream().mapToInt(d -> d).max().getAsInt();
```


## Times

* Parsing: 40 ms
* Part 1 Solve time: 4 ms
* Part 2 Solve time: 3 ms

## Solutions: 

* Part 1: 141
* Part 2: 736

| | |
|:---|---:|
|[Previous (Year 2015, Day 8)](../../year2015/day08/README.md)|[Next (Year 2015, Day 10)](../../year2015/day10/README.md)|
