package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarePackage extends AoCDay {

    public static class ArcadeGame extends IntCodeComputer{
        Map<Coord2D, Integer> screen = new HashMap<>();
        Integer ballX;
        Integer paddleX;
        Integer outputNumber;
        Integer score;
        Integer currentX;
        Integer currentY;

        public ArcadeGame() {
            super();
            screen = new HashMap<>();
            ballX = 0;
            paddleX = 0;
            outputNumber = 0;
            score = 0;
            currentX = 0;
            currentY = 0;
        }

        @Override
        public Pair<Boolean, Long> getInputValue() {
            if (ballX > paddleX) {
                return Pair.of(true, 1L);
            } else if (ballX < paddleX) {
                return Pair.of(true, -1L);
            } else {
                return Pair.of(true, 0L);
            }
        }

        @Override
        public void addOutputValue(Long outputValue) {
           if (outputNumber % 3 == 0) {
               currentX = outputValue.intValue();
           } else if (outputNumber %3 == 1) {
               currentY = outputValue.intValue();
           } else {
               Integer value = outputValue.intValue();
               if (currentX == -1 && currentY == 0) {
                   score = value;
               } else {
                   screen.put(new Coord2D(currentX, currentY), value);
                   if (value == 3) {
                       paddleX = currentX;
                   } else if (value == 4) {
                       ballX = currentX;
                   }
               }
           }
           outputNumber++;
        }
    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day13/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(lines);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Long solutionPart1(List<String> lines) {
        ArcadeGame game = new ArcadeGame();
        game.initializeIntCode(lines);
        game.run();
        return game.screen.entrySet().stream().filter(e -> e.getValue() == 2).count();
    }

    Integer solutionPart2(List<String> lines) {
        ArcadeGame game = new ArcadeGame();
        game.initializeIntCode(lines);
        game.setIntCodeMemory(0L, 2L);
        game.run();
        return game.score;
    }

}
