# Day 5: Supply Stacks

[Back to Top README file](../../../README.md)

## Overview
Difficult Level: Easy

Input File: Current position of the boxes and the rearrangement procedure.

## Parsing
There are two different parsing instructions that have to be done. The first parsing is creating
the individual stacks and the other portion is parsing the rearrangement procedure.

### Creating the Stacks
The stacks look like:

```text
    [D]
[N] [C]
[Z] [M] [P]
 1   2   3
```

To create the stacks, create a stack and push each line onto the Stack until you reach a row that has numbers on it.
After finding the row with the numbers, find the location of each character in the stack. After finding
each location, start popping the rows and creating the individual stacks.

### Rearrangement Procedure
The rearrangement procedure is much more straightforward: `move N from Sa to Sb`.
We can just split the string and take the numbers from spot 1, 3, and 5.  Since arrays will be
zero-based, we will need to subtract from the numbers in spots 3 and 5.

## Solutions
### Part 1
Once the stacks have been created, we just have to pop from Sa and then push immediately to Sb. Do that N times and that is it.

### Part 2
We do the same thing as before, but now we have to break the move operation into two steps. The
first step is to push N blocks from Sa to a temporary stack.  The second step is to move the
N blocks from the temporary stack to Sb.