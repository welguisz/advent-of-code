package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.List;

public class SensorBoost extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,9,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<String> lines) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setInputValue(1L);
        intCodeComputer.run();
        return intCodeComputer.getDebugValue();
    }

    public Long solutionPart2(List<String> lines) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setInputValue(2L);
        intCodeComputer.run();
        return intCodeComputer.getDebugValue();
    }

}
