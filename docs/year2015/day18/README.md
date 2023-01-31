# Day 18: Like a GIF For Your Yard

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy/Medium

Input: Initial state of lights

## Part 1 and Part 2
This puzzle is very similar to [Day 6, Probably a Fire Hazard](../day06/README.md). 
The main difference is that instead of 1000x1000 grid, it is a 100x100 grid. Instead
of being static, the lights will turn on and off depending on the lights next to
it.

For a light to stay on, the number of neighboring lights have to equal 2 or 3. For
a light to turn on, the number of neighboring lights have to equal 3.  If the
neighboring lights are not 2 or 3, then the light is off.

To determine if the light stays on or off, just write the following function:
```java
    public Boolean newLight(Set<Coord2D> lights, Coord2D coordinate) {
        Long sum = NEIGHBORS.stream().map(n -> coordinate.add(n)).filter(n -> lights.contains(n)).count();
        List<Long> keepOn = List.of(2L,3L);
        return (lights.contains(coordinate) && (keepOn.contains(sum))) || (!lights.contains(coordinate) && sum.equals(3L));
    }
```

For the entire grid, we can just write two IntStreams and check each light.

```java
    public Set<Coord2D> updateGrid(final Set<Coord2D> lights, Boolean part2) {
        Set<Coord2D> lightsOn = new HashSet<>();
        IntStream.range(0,100).boxed().forEach(row -> {
            IntStream.range(0,100).boxed().forEach(col -> {
                if (newLight(lights, new Coord2D(row,col))) {
                    lightsOn.add(new Coord2D(row,col));
                }
            });
        });
        if (part2) {
            lightsOn.add(new Coord2D(0,0));
            lightsOn.add(new Coord2D(0,99));
            lightsOn.add(new Coord2D(99,0));
            lightsOn.add(new Coord2D(99,99));
        }
        return lightsOn;
    }
```

And finally, we need to do 100 iterations.

```java
    public Integer solutionPart1(Set<Coord2D> lights, Boolean part2) {
        for (int i = 0; i < 100; i++) {
            lights = updateGrid(lights, part2);
        }
        return lights.size();
    }
```