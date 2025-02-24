# Day 2: I Was Told There Would Be No Math

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy

Input: Lines of Dimensions given as `l*w*h`

## Parsing the input
For each line, split the line by the character `x`. Store each line as a list of integers.

## Part 1

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

## Part 2
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

## Notes about this puzzle
* There is only 3 dimensions and luckily, 3 choose 2 results in 3 different combinations to worry about. If there were 14 different dimensions, there would have been 91 different combinations to think about.
* Might be useful to have a general function that can do combinations.

| | | 
|:----|----:|
|[Previous (Day 1)](../day01/README.md)|[Next (Day 3)](../day03/README.md)|
