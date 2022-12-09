package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AoC2022Day9 extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day09/input.txt");
        Integer part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    Pair<Integer, Integer> updateTail(Pair<Integer,Integer> head, Pair<Integer,Integer> tail) {
        return updateTail(head.getLeft(),head.getRight(),tail.getLeft(),tail.getRight());
    }
    Pair<Integer, Integer> updateTail(Integer headX, Integer headY, Integer tailX, Integer tailY) {
        Integer diffX = headX - tailX;
        Integer diffY = headY - tailY;
        Integer deltaX = diffX / 2;
        Integer deltaY = diffY / 2;
        tailX += deltaX;
        tailY += deltaY;
        if (deltaX != 0 && deltaY == 0) {
            tailY = headY;
        }
        if (deltaY != 0 && deltaX == 0) {
            tailX = headX;
        }
        return Pair.of(tailX, tailY);
    }
    Integer solutionPart1(List<String> lines) {
        Integer headX = 0;
        Integer headY = 0;
        Integer tailX = 0;
        Integer tailY = 0;
        Set<Pair<Integer,Integer>> tailVisited = new HashSet<>();
        for (String line : lines) {
            String cmd[] = line.split(" ");
            String dir = cmd[0];
            Integer steps = Integer.parseInt(cmd[1]);
            Integer dy = (dir.equals("U")) ? -1 : (dir.equals("D")) ? 1 : 0;
            Integer dx = (dir.equals("L")) ? -1 : (dir.equals("R")) ? 1 : 0;
            for (int i = 0; i < steps; i++) {
                headX += dx;
                headY += dy;
                Pair<Integer, Integer> newTail = updateTail(headX,headY,tailX,tailY);
                tailVisited.add(newTail);
                tailX = newTail.getLeft();
                tailY = newTail.getRight();
            }
        }
        return tailVisited.size();
    }

    Integer solutionPart2(List<String> lines) {
        List<Pair<Integer,Integer>> rope = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
                Pair<Integer, Integer> head = rope.remove(0);
                Integer headX = head.getLeft();
                Integer headY = head.getRight();
                headX += dx;
                headY += dy;
                rope.add(0,Pair.of(headX,headY));
                for (int j = 1; j < 10; j++) {
                    head = rope.remove(j);
                    rope.add(j,updateTail(rope.get(j-1),head));
                }
                tailVisited.add(rope.get(9));
            }
        }
        return tailVisited.size();
    }

}
