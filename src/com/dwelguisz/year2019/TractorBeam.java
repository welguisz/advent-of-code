package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TractorBeam extends AoCDay {

    public static class TBIntCode extends IntCodeComputer {
        Long x;
        Long y;
        Boolean inputIsX;
        Long sum;

        public TBIntCode(Long x, Long y) {
            super();
            this.x = x;
            this.y = y;
            inputIsX = true;
            sum = 0L;
        }

        @Override
        public Pair<Boolean, Long> getInputValue() {
            Long inputValue = inputIsX ? x : y;
            inputIsX ^= true;
            return Pair.of(true, inputValue);
        }

        @Override
        public void addOutputValue(Long outputValue) {
            sum += outputValue;
        }

    }

    Map<Coord2D, String> tractorBeam;

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day19/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(lines);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(lines);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public Long solutionPart1(List<String> lines) {
        Long sum = 0L;
        tractorBeam = new HashMap<>();
        for (Long x = 0L; x < 50; x++) {
            for (Long y = 0L; y < 50; y++) {
                TBIntCode computer = new TBIntCode(x,y);
                computer.setId(1L);
                computer.initializeIntCode(lines);
                computer.run();
                sum += computer.sum;
                tractorBeam.put(new Coord2D(x.intValue(),y.intValue()),(computer.sum == 0L ? "." : "#"));
            }
        }
        return sum;
    }

    public Integer solutionPart2(List<String> lines) {
        //Find Coord2D that is first 3x3 available from initial tractorBeam Map
        Coord2D first3x3 = new Coord2D(-1,-1);
        for(int y = 0; y < 47; y++) {
            for (int x = 0; x < 47; x++) {
                final Integer fx = x;
                if (IntStream.range(y,y+3).boxed().allMatch(ly -> IntStream.range(fx,fx+3).boxed().allMatch(lx -> tractorBeam.get(new Coord2D(ly,lx)).equals("#")))) {
                    first3x3 = new Coord2D(y,x);
                }
            }
        }
        Coord2D currentTry = new Coord2D(first3x3.x * 33, first3x3.y * 33);
        Integer currentY = currentTry.x;
        Integer maxY = null;
        Integer minY = currentTry.x;
        boolean found = false;
        Coord2D target = new Coord2D(0,0);
        Integer startingX = findTargets(lines, currentY, currentTry.y/2).x;
        while (!found) {
            Coord2D rowWidth = findTargets(lines, currentY, startingX);
            Boolean otherCorner = InTractorBeam(lines, rowWidth.y - 100, currentY + 99);
            if (otherCorner) {
                maxY = currentY;
                startingX = rowWidth.x/2;
            } else {
                minY = currentY;
            }
            if (maxY != null && maxY - minY == 1) {
                rowWidth = findTargets(lines, maxY, startingX);
                target = new Coord2D(maxY, rowWidth.y - 100);
                found = true;
            }
            if (maxY == null) {
                currentY *= 2;
            } else {
                currentY = (minY + maxY) / 2;
            }
        }
        return (target.y * 10000) + target.x;
    }

    private Coord2D findTargets(List<String> lines, Integer y, Integer startingX) {
        Integer x = startingX;
        while (!InTractorBeam(lines,x,y)) {
            x++;
        }
        Integer startX = x;
        while (InTractorBeam(lines,x,y)) {
            x++;
        }
        Integer endX = x;
        return new Coord2D(startX, endX);
    }

    private boolean InTractorBeam(List<String> lines, Integer x, Integer y) {
        TBIntCode computer = new TBIntCode(x.longValue(),y.longValue());
        computer.setId(1L);
        computer.initializeIntCode(lines);
        computer.run();
        return computer.sum == 1L;
    }


}
