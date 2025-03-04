# Day 7 Some Assembly Required

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/7)
* Difficult Level: 3 out of 10
* [Input](https://adventofcode.com/2015/day/7/input): List of gates that have inputs from previous levels. Have to solve one level before solving the next.
* Skills/Knowledge: Logic Gates, Processing Gates when allowed

## Setup

## Parsing
For the parsing portion, I created a class that holds the following information:
* Instruction
* Operand 1
* Operand 2
* Result

The trick here is that the operation (AND, OR, NOT, LSHIFT, RSHIFT) could be in
position 0 or 1.  For a BUFFER instruction (`in -> out`), there are no operation.
So when parsing, have to figure out the instruction layout.

The class also contains the following functions:
* `boolean ableToProcess(Map<String,Integer>)`: checks to see if the operation can be completed.
* `Map<String,Integer> process(Map<String,Integer>)`: processes the instruction
* `Integer make16bit(Integer)`: makes the value 16 bit
* `Integer getValue(String, Map<String,Integer>)`: checks to see if the String is a number, if a number, return the value, else get the value from the Map.
* `boolan isNumber(String)`: Checks to see if a string is a number

By having this class, it will make the solution for both parts straightforward


## Part 1 Solution:

After we have parsed the input file, we can just keep track of known values in
a Map. If we can process an instruction (all inputs are known), we can set the
output in the Map and remove the instruction from instruction list.

```java
    Integer part1(List<LogicGate> logicGates, Map<String, Integer> values) {
        while (!logicGates.isEmpty()) {
            List<LogicGate> unknownLogicGates = new ArrayList<>();
            for(LogicGate logicGate : logicGates) {
                if (logicGate.ableToProcess(values)) {
                    values = logicGate.process(values);
                } else {
                    unknownLogicGates.add(logicGate);
                }
            }
            logicGates = unknownLogicGates;
        }
        return values.get("a");
    }
```

For part1, we just call `part1Answer = part1(logicGates, new HashMap<>())`


## Part 2 Solution:

For part 2, we have to add the following to our code:
```java
Map<String,Integer> values = new HashMap<>();
values.put("b",part1Answer);
part2Answer = part1(logicGates, values);
```


## Times

* Parsing: 9 ms
* Part 1 Solve time: 6 ms
* Part 2 Solve time: 4 ms

## Solutions: 

* Part 1: 956
* Part 2: 40149

| | |
|:---|---:|
|[Previous (Year 2015, Day 6)](../../year2015/day06/README.md)|[Next (Year 2015, Day 8)](../../year2015/day08/README.md)|
