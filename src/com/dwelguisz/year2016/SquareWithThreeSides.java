package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.*;
import java.util.stream.Collectors;

public class SquareWithThreeSides extends AoCDay {
    public void solve(){
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2016/day03/input.txt");
        Integer part1 = solutionPart1(lines);
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines) {
        int validTriangleCount = 0;
        for (String line : lines){
            List<String> sidesStr =Arrays.stream(line.split("\\s{1,}")).collect(Collectors.toList());
            List<Integer> sides = sidesStr.stream()
                    .map(str -> str.replaceAll(" ",""))
                    .filter(str -> str.length() > 0)
                    .map(Integer::parseInt).collect(Collectors.toList());
            if (isTriangle(sides)) {
                validTriangleCount++;
            }
        }

        return validTriangleCount;
    }

    public Integer solutionPart2(List<String> lines) {
        int validTriangleCount = 0;
        for(int i = 0; i < lines.size(); i+=3) {
            List<String> sides0Str =Arrays.stream(lines.get(i).split("\\s{1,}")).collect(Collectors.toList());
            List<String> sides1Str =Arrays.stream(lines.get(i+1).split("\\s{1,}")).collect(Collectors.toList());
            List<String> sides2Str =Arrays.stream(lines.get(i+2).split("\\s{1,}")).collect(Collectors.toList());
            List<Integer> sides0 = sides0Str.stream()
                    .map(str -> str.replaceAll(" ",""))
                    .filter(str -> str.length() > 0)
                    .map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> sides1 = sides1Str.stream()
                    .map(str -> str.replaceAll(" ",""))
                    .filter(str -> str.length() > 0)
                    .map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> sides2 = sides2Str.stream()
                    .map(str -> str.replaceAll(" ",""))
                    .filter(str -> str.length() > 0)
                    .map(Integer::parseInt).collect(Collectors.toList());
            for(int j = 0; j < 3; j++) {
                List<Integer> testSides = new ArrayList<>();
                testSides.add(sides0.get(j));
                testSides.add(sides1.get(j));
                testSides.add(sides2.get(j));
                if (isTriangle(testSides)) {
                    validTriangleCount++;
                }
            }
        }
        return validTriangleCount;
    }

    public boolean isTriangle(List<Integer> sides) {
        Integer sideA = Collections.min(sides);
        Integer sideC = Collections.max(sides);
        sides.remove(sideA);
        sides.remove(sideC);
        Integer sideB = sides.get(0);
        return (sideA + sideB > sideC);
    }
}
