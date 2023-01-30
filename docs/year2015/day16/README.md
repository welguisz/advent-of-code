# Day 15: Science for Hungry People

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy/Medium

Input: List of Aunts and What you remembered about them

## Part 1 and Part 2
On my first pass, I did filtering that got the correct answer for Part 1, but
produced two answers for Part 2. When I changed to add a point for each
characteristic that matches, it produces the correct answer.

### The wrong way
For each characteristic, return true/false if the condition matches.

```java
public boolean checkChildren(Integer val) {
    if (children < 0) {
        return true;
    }
    return children == val;
}
```

### The correct way
If the characteristic matches, add a point.
```java
public Integer checkPerfumes(Integer val) {
    if (perfumes == -1) {
        return 0;
    }
        return (perfumes == val) ? 1 : 0;
}
```

By doing this you can calculate the score and find the id of the Aunt Sue to 
send the Thank you card to.

|[Previous (Day 15)](../day15/README.md)|[Next (Day 17)](../day17/README.md)|