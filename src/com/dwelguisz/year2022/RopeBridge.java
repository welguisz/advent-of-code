package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RopeBridge extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,9,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Pair<Integer, Integer> updateKnot(Pair<Integer,Integer> prevKnot, Pair<Integer,Integer> followingKnot) {
        Integer prevKnotX = prevKnot.getLeft();
        Integer prevKnotY = prevKnot.getRight();
        Integer followKnotX = followingKnot.getLeft();
        Integer followKnotY = followingKnot.getRight();
        Integer diffX = prevKnotX - followKnotX;
        Integer diffY = prevKnotY - followKnotY;
        Integer deltaX = diffX / 2;
        Integer deltaY = diffY / 2;
        followKnotX += deltaX;
        followKnotY += deltaY;
        if (deltaX != 0 && deltaY == 0) {
            followKnotY = prevKnotY;
        }
        if (deltaY != 0 && deltaX == 0) {
            followKnotX =  prevKnotX;
        }
        return Pair.of(followKnotX, followKnotY);
    }

    Integer simulate(List<String> lines, Integer knots) {
        List<Pair<Integer,Integer>> rope = new ArrayList<>();
        for (int i = 0; i < knots; i++) {
            rope.add(Pair.of(0,0));
        }
        Set<Pair<Integer,Integer>> tailVisited = new HashSet<>();
        for (String line : lines) {
            String cmd[] = line.split(" ");
            String dir = cmd[0];
            Integer steps = Integer.parseInt(cmd[1]);
            Integer dy = (dir.equals("U")) ? -1 : (dir.equals("D")) ? 1 : 0;
            Integer dx = (dir.equals("L")) ? -1 : (dir.equals("R")) ? 1 : 0;
            for (int i = 0; i < steps; i++) {
                Pair<Integer, Integer> tmp = rope.remove(0);
                Integer headX = tmp.getLeft();
                Integer headY = tmp.getRight();
                headX += dx;
                headY += dy;
                rope.add(0,Pair.of(headX,headY));
                for (int j = 1; j < knots; j++) {
                    tmp = rope.remove(j);
                    rope.add(j, updateKnot(rope.get(j-1),tmp));
                }
                tailVisited.add(rope.get(rope.size()-1));
            }
        }
        return tailVisited.size();

    }
    Integer solutionPart1(List<String> lines) {
        return simulate(lines, 2);
    }

    Integer solutionPart2(List<String> lines) {
        return simulate(lines, 10);
    }

    Integer solutionPart3(List<String> lines) {
        List<Pair<Integer,Integer>> rope = new ArrayList<>();
        rope.add(Pair.of(0,0));
        Set<Pair<Integer,Integer>> headVisited = new HashSet<>();
        for (String line : lines) {
            String cmd[] = line.split(" ");
            String dir = cmd[0];
            Integer steps = Integer.parseInt(cmd[1]);
            Integer dy = (dir.equals("U")) ? -1 : (dir.equals("D")) ? 1 : 0;
            Integer dx = (dir.equals("L")) ? -1 : (dir.equals("R")) ? 1 : 0;
            for (int i = 0; i < steps; i++) {
                Pair<Integer, Integer> tmp = rope.remove(0);
                Integer headX = tmp.getLeft();
                Integer headY = tmp.getRight();
                headX += dx;
                headY += dy;
                headVisited.add(Pair.of(headX,headY));
                rope.add(Pair.of(headX,headY));
            }
        }
        Integer maxDistance = Integer.MIN_VALUE;
        for (Pair<Integer, Integer> head : headVisited) {
            maxDistance = Integer.max(maxDistance, distance(head));
        }
        Integer tailVisited = 1;
        maxDistance +=  maxDistance / 10;
        while (tailVisited == 1) {
            maxDistance--;
            tailVisited = simulate(lines, maxDistance);
        }
        maxDistance++;
        return maxDistance;
    }

    public Integer distance(Pair<Integer, Integer> point) {
        double square = point.getLeft() * point.getLeft() + point.getRight() * point.getRight();
        double sqrt = Math.sqrt(square);
        return (int) sqrt;
    }

}
