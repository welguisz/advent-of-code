# Day 16: Proboscidea Volcanium

[Back to Top README file](../../../README.md)

## Overview

Difficult Level: Medium/Hard

Input: Text file that has valves, flow, and tunnels to other valves

## Approach
Since we are dealing with tunnels and everything is interconnected, we can assume that all valves
will connect. Some of the valves have flow and others don't.  It doesn't make sense to go to a
valve that has zero flow. We can pass by it, but should not spend time opening it.  This means
we need to find the shortest distances between each valve.  To do this, we will use *Floyd-Warshall*
algorithms.

### Parsing
Each line has the following format:
```text
Valve ?? has flow rate = ?; tunnels lead to valve(s) ??, ??, ??
```

When we parse, we will store the information in a `Map<Valve Name, Valve>`

```java
public static Valve {
    public String name;
    public Integer flow;
    public List<String> tunnels;
    public Valve(String name, Integer flow, List<String> tunnels) {
        this.name = name;
        this.flow = flow;
        this.tunnels = tunnels;
    }
}
```

### Setup global variables
#### graph
* HashMap with a key of the Valve Name and value of the `Valve`
* Will contain one key for each line of the text file.

#### flows
* HashMap with a key of the Valve Name and the value of the flow.
* Only valves that do not have flow rate != 0 should be included.

#### indicies
* HashMap with a key of the Valve that has a positive flow and the bitMask for that valve.
* This will make it easier to see if we have visited the valve. If so, we can ignore it.

```java
int bitLocation = 0;
for (String valName : flows.keySet()) {
    indicies.put(valName, 1 << bitLocation);
    bitLocation++;
}
```

#### distances
* This will be a HashMap between points will contain the distance between the two points.
* Will initialize the distances hashMap as follows:
  * If the valve Names equal to each other, the distance will automatically be 0.
  * If the valve Names is connected via tunnel, the distance will be 1.
  * If the valve Names are not connected via tunnel, the distance will be 1_000_000 (or any big number).
* After initializing, loop through using *Floyd-Warshall* to find the shortest distances between each valve

```java
distances = new HashMap<>();
for (String valve1 : valveNames) {
    List<String> adjacentValves = graph.get(valve1).tunnelsDestinations;
    for (String valve2 : valveNames) {
        if (valve1.equals(valve2)) {
            distances.put(Pair.of(valve1, valve2), 0);
        } else if (adjacentValves.contains(valve2)) {
            distances.put(Pair.of(valve1, valve2), 1);
        } else {
            distances.put(Pair.of(valve1, valve2), 1_000_000);
        }
    }
}
for (String k: valveNames) {
    for (String i : valveNames) {
        for (String j : valveNames) {
            distances.put(Pair.of(i,j), Integer.min(distances.get(Pair.of(i,j)), distances.get(Pair.of(i,k))+distances.get(Pair.of(k,j))));
        }
    }
}
```

### General Approach
#### Create a new class PressureState
PressureState will be a class that contains a possible state of the valves that have flow. PressureState
will contain the following information:

* Valve currently at
* Time Remaining
* Current Pressure Reading
* Current bitMask

As we go through each pressureState, we will evaluate if we can go to the next Valve with a flow.
The criteria to go to the next valve is:

* There is timeRemaining; AND
* we have not visited that valve before (done by bitMasking AND with the current bitMask)

This leads to the following code:

```java
Map<Integer, Integer> visit(Valve valve, Integer minutes, Integer bitMask, Integer pressure) {
    Map<Integer, Integer> result = new HashMap<>();
    PriorityQueue<PressureState> pressureStates = new PriorityQueue<>(2000, (a,b) -> b.minutes - a.minutes);
    pressureStates.add(new PressureState(valve, bitMask, pressure, minutes));
    while (!pressureStates.isEmpty()) {
        PressureState currentState = pressureStates.poll();
        result.put(currentState.bitMask, Integer.max(result.getOrDefault(currentState.bitMask, 0), currentState.pressure));
        for (Map.Entry<String, Integer> valve2 : flows.entrySet()) {
            String valveName = valve2.getKey();
            Valve valve2Name = graph.get(valveName);
            Integer remainingMinutes = currentState.minutes - distances.get(Pair.of(currentState.valve.name, valve2Name.name)) - 1;
            if (((indicies.get(valveName) & currentState.bitMask) != 0) || (remainingMinutes <= 0)) {
                continue;
            }
            pressureStates.add(new PressureState(
                    graph.get(valveName),
                    currentState.bitMask | indicies.get(valve2.getKey()),
                    currentState.pressure + flows.get(valveName) * remainingMinutes,
                    remainingMinutes
            ));
        }
    }
    return result;
}
```

## Solution to Part 1
From the work that we have done, the solution for Part 1 comes down to:

```java
Integer solutionPart1() {
    Map<Integer, Integer> answers = visit(graph.get("AA"), 30, 0, 0);
    return answers.values().stream().mapToInt(i -> i).max().getAsInt();
}
```

## Solution to Part 2
The work that we have done can be reused and we can just change one parameter to `visit` function
to get the bitMasking for 26 minutes. From the map, we can go through each entry and see that the
two bitMaskings don't overlap. If they don't overlap, we can add those two values together to
see if they produce the maximum flow.

```java
Integer solutionPart2() {
    Map<Integer, Integer> visited = visit(graph.get("AA"), 26, 0, 0);
    Integer maxValue = Integer.MIN_VALUE;
    for (Map.Entry<Integer, Integer> visit1 : visited.entrySet()) {
        for (Map.Entry<Integer, Integer> visit2 : visited.entrySet()) {
            if ((visit1.getKey() & visit2.getKey()) == 0) {
                maxValue = Integer.max(maxValue, visit1.getValue() + visit2.getValue());
            }
        }
    }
    return maxValue;
}
```

# Overall
Once we got to creating a map with a key of the bitMask and the value of the flow for that
scenario, we can create that map and just go through that map to find the values we needed.