package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class CathodeRayTube extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day10/input.txt");
        Pair<Integer, String> part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1.getLeft()));
        System.out.println(String.format("Part 2 Answer:"));
        System.out.println(part1.getRight());
    }

    Pair<Integer,String> solutionPart1(List<String> lines) {
        Integer cycleCount = 0;
        Integer registerA = 1;
        List<Integer> values = new ArrayList<>();
        String screen = "";
        for (String l : lines) {
            String split[] = l.split(" ");
            String cmd = split[0];
            if (cmd.equals("noop")) {
                screen += printPixel(registerA, cycleCount);
                cycleCount++;
                if (cycleCount % 40 == 20) {
                    values.add(registerA * cycleCount);
                }
            }
            if (cmd.equals("addx")) {
                Integer val = Integer.parseInt(split[1]);
                for (int i = 0; i < 2; i++) {
                    screen += printPixel(registerA, cycleCount);
                    cycleCount++;
                    if (cycleCount % 40 == 20) {
                        values.add(registerA * cycleCount);
                    }
                }
                registerA += val;
            }
        }
        return Pair.of(values.stream().mapToInt(i -> i).sum(), screen);
    }

    String printPixel(Integer registerA, Integer cycleCount) {
        Integer xCoord = cycleCount % 40;
        List<Integer> spriteArea = List.of(registerA-1,registerA,registerA+1);
        String tmp = spriteArea.contains(xCoord) ? "#" : " ";
        tmp += (xCoord == 39) ? "\n" : "";
        return tmp;
    }

}
