package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NoTimeForATaxicab extends AoCDay {

    enum direction{
        North,
        East,
        South,
        West
    }

    List<Pair<Integer, Integer>> visitedPlaces;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,1,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(String directions) {
        String[] directionsArr = directions.split(", ");
        direction currentDirection = direction.North;
        Pair<Integer, Integer> currentLocation = Pair.of(0,0);
        for(String direction : directionsArr) {
            currentDirection = calculateNextDirection(currentDirection, direction.substring(0,1));
            Integer blocks = Integer.parseInt(direction.substring(1));
            if (currentDirection == NoTimeForATaxicab.direction.North) {
                currentLocation = Pair.of(currentLocation.getLeft() + blocks, currentLocation.getRight());
            } else if (currentDirection == NoTimeForATaxicab.direction.East) {
                currentLocation = Pair.of(currentLocation.getLeft(), currentLocation.getRight() + blocks);
            } else if (currentDirection == NoTimeForATaxicab.direction.South) {
                currentLocation = Pair.of(currentLocation.getLeft() - blocks, currentLocation.getRight());
            } else {
                currentLocation = Pair.of(currentLocation.getLeft(), currentLocation.getRight() - blocks);
            }
        }
        return Math.abs(currentLocation.getLeft()) + Math.abs(currentLocation.getRight());
     }

     public Integer solutionPart2(String directions) {
         String[] directionsArr = directions.split(", ");
         direction currentDirection = direction.North;
         Pair<Integer, Integer> currentLocation = Pair.of(0,0);
         visitedPlaces = new ArrayList<>();
         for(String direction : directionsArr) {
             currentDirection = calculateNextDirection(currentDirection, direction.substring(0,1));
             Integer blocks = Integer.parseInt(direction.substring(1));
             Pair<Integer, Integer> nextLocation = currentLocation;
             if (currentDirection == NoTimeForATaxicab.direction.North) {
                 nextLocation = Pair.of(currentLocation.getLeft() + blocks, currentLocation.getRight());
                 for(int i = currentLocation.getLeft()+1; i <= currentLocation.getLeft() + blocks; i++) {
                     Pair<Integer, Integer> passThru = Pair.of(i, currentLocation.getRight());
                     if (visitedPlaces.contains(passThru)) {
                         currentLocation = passThru;
                         return Math.abs(currentLocation.getLeft()) + Math.abs(currentLocation.getRight());
                     }
                     visitedPlaces.add(passThru);
                 }
                 currentLocation = nextLocation;
             } else if (currentDirection == NoTimeForATaxicab.direction.East) {
                 nextLocation = Pair.of(currentLocation.getLeft(), currentLocation.getRight() + blocks);
                 for(int i = currentLocation.getRight()+1; i <= currentLocation.getRight() + blocks; i++) {
                     Pair<Integer, Integer> passThru = Pair.of(currentLocation.getLeft(), i);
                     if (visitedPlaces.contains(passThru)) {
                         currentLocation = passThru;
                         return Math.abs(currentLocation.getLeft()) + Math.abs(currentLocation.getRight());
                     }
                     visitedPlaces.add(passThru);
                 }
                 currentLocation = nextLocation;
             } else if (currentDirection == NoTimeForATaxicab.direction.South) {
                 nextLocation = Pair.of(currentLocation.getLeft() - blocks, currentLocation.getRight());
                 for(int i = currentLocation.getLeft()-1; i >= currentLocation.getLeft() - blocks; i--) {
                     Pair<Integer, Integer> passThru = Pair.of(i, currentLocation.getRight());
                     if (visitedPlaces.contains(passThru)) {
                         currentLocation = passThru;
                         return Math.abs(currentLocation.getLeft()) + Math.abs(currentLocation.getRight());
                     }
                     visitedPlaces.add(passThru);
                 }
                 currentLocation = nextLocation;
             } else {
                 nextLocation = Pair.of(currentLocation.getLeft(), currentLocation.getRight() - blocks);
                 for(int i = currentLocation.getRight()-1; i >= currentLocation.getRight() - blocks; i--) {
                     Pair<Integer, Integer> passThru = Pair.of(currentLocation.getLeft(), i);
                     if (visitedPlaces.contains(passThru)) {
                         currentLocation = passThru;
                         return Math.abs(currentLocation.getLeft()) + Math.abs(currentLocation.getRight());
                     }
                     visitedPlaces.add(passThru);
                 }
                 currentLocation = nextLocation;
             }
         }
         return Math.abs(currentLocation.getLeft()) + Math.abs(currentLocation.getRight());

     }

    public direction calculateNextDirection(direction current, String turn) {
        if (current == direction.North) {
            if ("R".equals(turn)) {
                return direction.East;
            } else {
                return direction.West;
            }
        } else if (current == direction.East) {
            if ("R".equals(turn)) {
                return direction.South;
            } else {
                return direction.North;
            }
        } else if (current == direction.South) {
            if ("R".equals(turn)) {
                return direction.West;
            } else {
                return direction.East;
            }
        } else {
            if ("R".equals(turn)) {
                return direction.North;
            } else {
                return direction.South;
            }
        }
    }

}
