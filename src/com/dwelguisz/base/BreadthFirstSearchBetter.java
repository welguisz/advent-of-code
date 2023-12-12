package com.dwelguisz.base;

import com.dwelguisz.utilities.Coord2D;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BreadthFirstSearchBetter<T> {

    public BreadthFirstSearchBetter() {

    }

    public abstract Stream<T> nextSteps(T currentPos, Collection<T> neighbors, Set<T> allowedSpaces);

    public abstract Collection<T> getNeighbors(T currentPos, Collection<T> neighborsToCheck);
    public Long bfsRoute(T start, T end, Long startTime, Collection<T> neighborsToCheck) {
        Set<T> states = new HashSet<>();
        states.add(start);
        for (Long i = startTime; true; i++) {
//            Set<T> nextPlacesToLook = getNeighbors();
//            states = states.stream().flatMap(s -> nextSteps(s, nextPlacesToLook, new HashSet<>())).collect(Collectors.toSet());
            if (states.stream().anyMatch(s -> s.equals(end))) {
                return i+1;
            }
        }

    }
}
