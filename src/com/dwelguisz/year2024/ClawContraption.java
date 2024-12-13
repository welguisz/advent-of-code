package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClawContraption extends AoCDay {

   public static class ClawWork {
        public Coord2D buttonA;
        public Coord2D buttonB;
        public Coord2D prize;

        public ClawWork(Coord2D buttonA, Coord2D buttonB, Coord2D prize) {
            this.buttonA = buttonA;
            this.buttonB = buttonB;
            this.prize = prize;
        }

        public long cost(long offset) {
            long denominator = buttonA.x * buttonB.y - buttonA.y * buttonB.x;
            long px = prize.x + offset;
            long py = prize.y + offset;
            if (denominator == 0) {
                return 0l;
            }
            if (((px * buttonB.y - py * buttonB.x) % denominator) != 0) {
                return 0l;
            }
            long buttonAPresses = (px * buttonB.y - py * buttonB.x) / denominator;
            if (((py - buttonA.y * buttonAPresses) % buttonB.y) != 0) {
                return 0l;
            }
            long buttonBPresses = (py - buttonA.y * buttonAPresses) / buttonB.y;
            return buttonAPresses * 3 + buttonBPresses;
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 13, false, 0);
        List<ClawWork> clawWorks = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solve(clawWorks, 0l);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solve(clawWorks, 10000000000000L);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<ClawWork> parseLines(List<String> lines) {
        Coord2D buttonA = new Coord2D(0, 0);
        Coord2D buttonB = new Coord2D(0, 0);
        Coord2D prize = new Coord2D(0, 0);
        List<ClawWork> clawWorks = new ArrayList<>();
        int lineNumber = 0;
        for(String line : lines) {
            if (line.isEmpty()) {
                clawWorks.add(new ClawWork(buttonA, buttonB, prize));
                lineNumber = 0;
            } else {
                if (lineNumber == 0) {
                    String[] s1 = line.split(", ");
                    int x = Integer.parseInt(s1[0].split("\\+")[1]);
                    int y = Integer.parseInt(s1[1].split("\\+")[1]);
                    buttonA = new Coord2D(x,y);
                } else if (lineNumber == 1) {
                    String[] s1 = line.split(", ");
                    int x = Integer.parseInt(s1[0].split("\\+")[1]);
                    int y = Integer.parseInt(s1[1].split("\\+")[1]);
                    buttonB = new Coord2D(x,y);
                } else if (lineNumber == 2) {
                    String[] s1 = line.split(", ");
                    int x = Integer.parseInt(s1[0].split("=")[1]);
                    int y = Integer.parseInt(s1[1].split("=")[1]);
                    prize = new Coord2D(x,y);
                }
                lineNumber++;
            }
        }
        clawWorks.add(new ClawWork(buttonA, buttonB, prize));
        return clawWorks;
    }

    long solve(List<ClawWork> clawWorks, Long offset) {
        return clawWorks.stream().mapToLong(c -> c.cost(offset)).sum();
    }
}
