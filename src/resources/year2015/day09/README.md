# Day 9: All In A Single Night

[Back to Top README file](../../../../README.md)
## Overview
Difficult Level: Easy

Input: List of cities with distances between the cities

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

## Part 1
For part1, it asked for the minimum distance that Santa could travelled. So that would be:

```java
return travelledDistance.stream().mapToInt(d -> d).min().getAsInt();
```

## Part 2,
For part2, just changed minimum to maximum and we get:
```java
return travelledDistance.stream().mapToInt(d -> d).max().getAsInt();
```

|[Previous (Day 8)](../day08/README.md)|[Next (Day 10)](../day10/README.md)|