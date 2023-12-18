package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class LavaductLagoon extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 18, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Coord2D UP = new Coord2D(-1,0);
    Coord2D RIGHT = new Coord2D(0,1);
    Coord2D DOWN = new Coord2D(1,0);
    Coord2D LEFT = new Coord2D(0,-1);

    Map<String, Coord2D> selectDir = Map.of("U",UP,"R",RIGHT,"D",DOWN,"L",LEFT);
    Map<String, Coord2D> selectDirPart2 = Map.of("3",UP,"0",RIGHT,"1",DOWN,"2",LEFT);
    Long solutionPart1(List<String> lines) {
        Coord2D currentLoc = new Coord2D(0,0);
        Long sumR = 0L;
        Long sumC = 0L;
        Long sumDir = 0L;
        for (String line : lines) {
            String[] splits = line.split(" ");
            Coord2D dir = selectDir.get(splits[0]);
            Integer steps = Integer.parseInt(splits[1]);
            Coord2D nextLoc = currentLoc.add(dir.multiply(steps));
            sumR += currentLoc.x * nextLoc.y;
            sumC += currentLoc.y * nextLoc.x;
            sumDir += steps;
            currentLoc = nextLoc;
        }
        Long area = Math.abs(sumR - sumC)/2;
        return (area + (sumDir/2) + 1);
    }

    Long solutionPart2(List<String> lines) {
        List<Long> currentLoc = List.of(0L,0L);
        Long sumR = 0L;
        Long sumC = 0L;
        Long sumDir = 0L;
        for (String line : lines) {
            String[] splits = line.split(" ");
            Coord2D dir = selectDirPart2.get(splits[2].substring(7,8));
            Integer steps = Integer.decode("0x"+splits[2].substring(2,7));
            List<Long> nextLoc = List.of(
                    currentLoc.get(0) + dir.x * steps,
                    currentLoc.get(1) + dir.y * steps
            );
            sumR +=  currentLoc.get(0) * nextLoc.get(1);
            sumC += currentLoc.get(1) * nextLoc.get(0);
            sumDir += steps;
            currentLoc = nextLoc;
        }
        Long area = Math.abs(sumR - sumC)/2;
        return (area + (sumDir/2) + 1);
    }

}
