package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class AllInASingleNight extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,9,false,0);
        List<Integer> allDistances = findAllDistances(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(allDistances);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(allDistances);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
        documentation();
    }

    public List<Integer> findAllDistances(List<String> lines) {
        Map<Pair<String,String>, Integer> distances = new HashMap<>();
        Set<String> places = new HashSet<>();
        for (String line:lines) {
            String[] points = line.split(" ");
            Integer distance = parseInt(points[4]);
            places.add(points[0]);
            places.add(points[2]);
            distances.put(Pair.of(points[0],points[2]), distance);
            distances.put(Pair.of(points[2],points[0]), distance);
        }
        Collection<List<String>> permutations = Collections2.permutations(places);
        List<Integer> travelledDistance = new ArrayList<>();
        for (List<String> p : permutations) {
            Integer distance = 0;
            for(int i = 0; i < p.size()-1; i++) {
                distance += distances.get(Pair.of(p.get(i),p.get(i+1)));
            }
            travelledDistance.add(distance);
        }
        return travelledDistance;
    }

    public Integer solutionPart1(List<Integer> distances) {
        return distances.stream().mapToInt(d -> d).min().getAsInt();
    }

    public Integer solutionPart2(List<Integer> distances) {
        return distances.stream().mapToInt(d -> d).max().getAsInt();
    }

    void documentation() {
        puzzleName = "All In A Single Night";
        difficultLevel = "2 out of 10";
        inputDescription = "lines of strings that have been encoded";
        skills = List.of("regex");
        setup = """
## Parsing
Parsing is straightforward with each line being of the format `City1 to\s
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
""";

        part1Solution = """
For part1, it asked for the minimum distance that Santa could travelled. So that would be:

```java
return travelledDistance.stream().mapToInt(d -> d).min().getAsInt();
```
""";
        part2Solution = """
For part2, just changed minimum to maximum and we get:
```java
return travelledDistance.stream().mapToInt(d -> d).max().getAsInt();
```
""";
    }

}
