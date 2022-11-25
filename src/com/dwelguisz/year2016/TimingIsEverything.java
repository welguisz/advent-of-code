package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.ChineseRemainderTheorem;

import java.util.ArrayList;
import java.util.List;

public class TimingIsEverything extends AoCDay {

    public static class Disc {
        Integer numberOfPositions;
        Integer currentPosition;

        public Disc(Integer numberOfPositions, Integer currentPosition) {
            this.numberOfPositions = numberOfPositions;
            this.currentPosition = currentPosition;
        }

        public boolean aligned() {
            return currentPosition.equals(0);
        }

        public void inc() {
            this.currentPosition = (currentPosition + 1) % numberOfPositions;
        }

    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day15/input.txt");
        Integer part1 = solutionPart1(lines, false);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart1(lines, true);
        System.out.println(String.format("Part 1 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines, boolean part2) {
        List<Disc> currentPositions = setUpDiscs(lines);
        if (part2) {
            currentPositions.add(new Disc(11,0));
        }
        Integer time = 0;
        Integer initialTime = 0;
        Integer discNumber = 0;
        while (true) {
            time++;
            for (Disc disc : currentPositions) {
                disc.inc();
            }
            Disc currentDisc = currentPositions.get(discNumber);
            if (currentDisc.aligned()) {
                discNumber++;
            } else {
                discNumber = 0;
                initialTime = time;
            }
            if (discNumber == currentPositions.size()) {
                return initialTime;
            }
        }
    }

    public List<Disc> setUpDiscs(List<String> lines) {
        List<Disc> initialPositions = new ArrayList<>();
        for (String line : lines) {
            String split[] = line.split(" ");
            Integer nP = Integer.parseInt(split[3]);
            Integer cP = Integer.parseInt(split[11].substring(0,split[11].length()-1));
            initialPositions.add(new Disc(nP, cP));
        }
        return initialPositions;
    }
}
