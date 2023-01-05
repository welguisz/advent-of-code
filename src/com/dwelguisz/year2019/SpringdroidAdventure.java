package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringdroidAdventure extends AoCDay {

    public static class SpringDroid extends IntCodeComputer {

        byte[] program;
        Map<Coord2D, String> map;

        Integer programPointer;
        Long damage;
        Integer x;
        Integer y;
        Integer maxX;
        Integer maxY;
        public SpringDroid() {
            super();
            programPointer = 0;
            map = new HashMap<>();
            damage = 0L;
            x = 0;
            y = 0;
            maxX = 0;
            maxY = 0;
        }

        public void setFunctions(List<String> instructions) {
            String total = instructions.stream().collect(Collectors.joining("\n")) + "\n";
            program = total.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public Pair<Boolean, Long> getInputValue() {
            if (programPointer < program.length) {
                programPointer++;
                return Pair.of(true, Long.valueOf(program[programPointer-1]));
            }
            return Pair.of(false, 0L);
        }

        @Override
        public void addOutputValue(Long outputValue) {
            if (outputValue > 255L) {
                damage = outputValue;
            }
            else if (outputValue == 35L) {
                map.put(new Coord2D(y,x), "#");
                x++;
            } else if (outputValue == 46L) {
                map.put(new Coord2D(y,x), ".");
                x++;
            } else if (outputValue == 10L) {
                x = 0;
                y++;
            } else if (outputValue == 96L) { //robot Here; pointing up
                map.put(new Coord2D(y,x),"@");
                x++;
            }
            maxX = Integer.max(maxX, x);
            maxY = Integer.max(maxY, y);
        }

        public void printMap() {
            System.out.print("   ");
            for (int x = 0; x < maxX; x++) {
                System.out.print(x / 10);
            }
            System.out.println();
            System.out.print("   ");
            for (int x = 0; x < maxX; x++) {
                System.out.print(x % 10);
            }
            System.out.println();
            for (int y = 0; y < maxY-1; y++) {
                System.out.print(String.format("%2d ",y));
                for (int x = 0; x < maxX; x++) {
                    System.out.print(map.getOrDefault(new Coord2D(y,x),"?"));
                }
                System.out.println();
            }
        }


    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day21/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(lines);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Long solutionPart1(List<String> lines) {
        SpringDroid droid = new SpringDroid();
        droid.setId(1L);
        droid.initializeIntCode(lines);
        List<String> instructions = List.of("NOT T T", "AND A T", "AND B T", "AND C T", "NOT T J", "AND D J","WALK");
        droid.setFunctions(instructions);
        droid.run();
        droid.printMap();
        return droid.getDebugValue();
    }

    Long solutionPart2(List<String> lines) {
        SpringDroid droid = new SpringDroid();
        droid.setId(1L);
        droid.initializeIntCode(lines);
        List<String> instructions = List.of("NOT T T", "AND A T", "AND B T", "AND C T", "NOT T J", "AND D J","NOT E T", "NOT T T", "OR H T", "AND T J", "RUN");
        droid.setFunctions(instructions);
        droid.run();
        droid.printMap();
        return droid.getDebugValue();
    }

}
