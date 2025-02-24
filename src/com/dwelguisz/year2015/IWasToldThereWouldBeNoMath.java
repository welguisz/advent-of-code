package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class IWasToldThereWouldBeNoMath extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,2,false,0);
        List<List<Integer>> ribbons = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(ribbons);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(ribbons);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
        documentation();
    }

    List<List<Integer>> parseLines(List<String> lines) {
        List<List<Integer>> ints = new ArrayList<>();
        for (String l : lines) {
            ints.add(Arrays.stream(l.split("x")).map(Integer::parseInt).collect(Collectors.toList()));
        }
        return ints;
    }

    Integer solutionPart1(List<List<Integer>> ribbons) {
        List<List<Integer>> ribbonAreas = ribbons.stream()
                .map(dim -> List.of(dim.get(0)*dim.get(1),dim.get(0)*dim.get(2),dim.get(1)*dim.get(2)))
                .toList();
        List<Integer> extraArea = ribbonAreas.stream()
                .map(areas -> areas.stream().mapToInt(a -> a).min().getAsInt())
                .toList();
        List<Integer> totalArea = ribbonAreas.stream()
                .map(areas -> areas.stream().mapToInt(a -> a).sum())
                .toList();
        return extraArea.stream().mapToInt(a -> a).sum() + totalArea.stream().mapToInt(a -> 2*a).sum();
    }

    Integer solutionPart2(List<List<Integer>> ribbons) {
        List<Integer> minimumPerimeter = ribbons.stream()
                .map(dims -> List.of(2*(dims.get(0)+dims.get(1)),2*(dims.get(0)+dims.get(2)),2*(dims.get(1)+dims.get(2))))
                .map(perimeters -> perimeters.stream().mapToInt(p -> p).min().getAsInt())
                .toList();
        List<Integer> ribbonVolume = ribbons.stream().map(dims -> dims.get(0)*dims.get(1)*dims.get(2))
                .toList();
        return minimumPerimeter.stream().mapToInt(p->p).sum() + ribbonVolume.stream().mapToInt(v->v).sum();
    }

    void documentation() {
        puzzleName = "I was Told There Would Be No Math";
        difficultLevel = "1 out of 10";
        inputDescription = "List of dimensions of in the form of lxwxh";
        skills = List.of("Streaming", "min");
        part1Solution = """
### Basic Approach
For each line:
* Calculate the area of each face. Take the sum of each face area.
* Find the area of the face of the box that is the smallest.
* Add the sum of each area and the smallest area
* Keep a running sum

### Straight forward approach
```java
    Long solutionPart1(List<List<Integer>> ribbons) {
        Long total = 0L;
        for(List<Integer> dims : ribbons) {
            Integer l = dims.get(0);
            Integer w = dims.get(1);
            Integer h = dims.get(2);
            List<Integer> areas = Arrays.asList(l*w, l*h, w*h);
            Integer extra = areas.stream().min(Integer::compareTo).get();
            Integer totalArea = ((areas.get(0) + areas.get(1) + areas.get(2)) * 2)  + extra;
            total += totalArea;
        }
        return total;
    }
```

### Java Streams
```java
    Integer solutionPart1(List<List<Integer>> ribbons) {
        List<List<Integer>> ribbonAreas = ribbons.stream()
                .map(dim -> List.of(dim.get(0)*dim.get(1),dim.get(0)*dim.get(2),dim.get(1)*dim.get(2)))
                .collect(Collectors.toList());
        List<Integer> extraArea = ribbonAreas.stream()
                .map(areas -> areas.stream().mapToInt(a -> a).min().getAsInt())
                .collect(Collectors.toList());
        List<Integer> totalArea = ribbonAreas.stream()
                .map(areas -> areas.stream().mapToInt(a -> a).sum())
                .collect(Collectors.toList());
        return extraArea.stream().mapToInt(a -> a).sum() + totalArea.stream().mapToInt(a -> 2*a).sum();
    }
```
""";
        part2Solution = """
### Basic Approach
For each line:
* Find the perimeter of the rectangle that is the smallest
* Find the volume of the rectangular prism.
* Add the smallest perimeter to the volume.
* Keep a running sum

### Straight forward approach
```java
    Long solutionPart2(List<List<Integer>> ribbons) {
        Long total = 0L;
        for(List<Integer> dims : ribbons) {
            Integer l = dims.get(0);
            Integer w = dims.get(1);
            Integer h = dims.get(2);
            List<Integer> perimeters = Arrays.asList(2 * (l + w), 2 * (l + h), 2 * (h + w));
            Integer perimeter = perimeters.stream().min(Integer::compareTo).get();
            Integer volume = l * w * h;
            Integer totalRibbon = perimeter + volume;
            total += totalRibbon;
        }
        return total;
    }
```

### Using Streams
```java
    Integer solutionPart2(List<List<Integer>> ribbons) {
        List<Integer> minimumPerimeter = ribbons.stream()
                .map(dims -> List.of(2*(dims.get(0)+dims.get(1)),2*(dims.get(0)+dims.get(2)),2*(dims.get(1)+dims.get(2))))
                .map(perimeters -> perimeters.stream().mapToInt(p -> p).min().getAsInt())
                .collect(Collectors.toList());
        List<Integer> ribbonVolume = ribbons.stream().map(dims -> dims.get(0)*dims.get(1)*dims.get(2))
                .collect(Collectors.toList());
        return minimumPerimeter.stream().mapToInt(p->p).sum() + ribbonVolume.stream().mapToInt(v->v).sum();
```
""";
    notesAboutPuzzle = """
* There is only 3 dimensions and luckily, 3 choose 2 results in 3 different combinations to worry about. If there were 14 different dimensions, there would have been 91 different combinations to think about.
* Might be useful to have a general function that can do combinations.
""";
    }

}
