# Day 1: Counting Calories

[Back to Top README file](../../../README.md)

## Overview
Difficult Level: Easy

Input: A text file that has numbers listed with a line delimiting between elves

## Overall Solution
Parse the input file and add up the calories for each elf. Order the elves so that the elf
with the greatest amount of calories is selected.

I created a Priority Queue and put the elf with the most calories at the front of the queue.

Some key aspects of the parsing:
The `lines.stream().collect(Collectors.joining(",")).split(",,")` will take the following lines

```text
1
2
3

5
7

3
4
```
and create the following array: `["1,2,3","5,7","3,4"]`

After creating the array, we can take each string, split on `,` and add the integers together.

```java
public PriorityQueue<Integer> createQueue(List<String> lines) {
    PriorityQueue<Integer> queue = new PriorityQueue<>(200, (a,b) -> b- a);
    Arrays.stream(
            lines.stream()
                .collect(Collectors.joining(","))
                .split(",,"))
        .forEach(elf -> queue.add(Arrays.stream(elf.split(","))
                .mapToInt(s -> Integer.parseInt(s)).sum()));
        return queue;
    }
```

## Solution Part 1
Just do `queue.peek()` and that will return the amount of calories for the elf that has the most calories.

## Solution Part 2
With Part 2, it just is now `queue.poll() + queue.poll() + queue.poll()`.