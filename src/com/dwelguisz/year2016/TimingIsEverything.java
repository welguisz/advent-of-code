package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.ChineseRemainderTheorem;

import java.time.Instant;
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
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,15,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lines, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
