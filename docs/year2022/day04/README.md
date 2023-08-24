# Day 4: Camp Cleanup

[Back to Top README file](../../../README.md)

## Overview
Difficult Level: Easy

Input: A text file that has lines of `A-B,C-D` where A,B,C,D are integers.

## Solution
The trick here is the parsing. When doing the parsing, we want to end up where each line is a Pair of
List of Integers separated by the comma.

Setting up the master List of Pairs goes like:

```java
public List<Pair<List<Integer>,List<Integer>>> createPairs(List<String> lines) {
    List<Pair<List<Integer>, List<Integer>>> pairs = new ArrayList<>();
    for (String line : lines) {
        List<Integer> values  = Arrays.stream(line.replace(",", "-").split("-")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> first = IntStream.range(values.get(0),values.get(1)+1).boxed().collect(Collectors.toList());
        List<Integer> second = IntStream.range(values.get(2), values.get(3)+1).boxed().collect(Collectors.toList());
        pairs.add(Pair.of(first, second));
    }
    return pairs;
}
```

### Part 1
Since we have everything in pairs now, we just need to come up with the needed filters. For Part 1,
it becomes:
```java
public Long solutionPart1(List<Pair<List<Integer>,List<Integer>>> pairs) {
    return pairs.stream()
        .filter(p -> p.getRight().containsAll(p.getLeft()) || p.getLeft().containsAll(p.getRight()))
        .count();
}
```

### Part 2
Part 2 now becomes:
```java
public Long solutionPart2(List<Pair<List<Integer>,List<Integer>>> pairs) {
    return pairs.stream()
        .filter(p -> p.getRight().stream()
                   .anyMatch(f -> p.getLeft().contains(f)))
        .count();
}
```