package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

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
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2016/day01/input.txt");
        String directions = lines.get(0);
        Integer part1 = solutionPart1(directions);
        Integer part2 = solutionPart2(directions);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
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
