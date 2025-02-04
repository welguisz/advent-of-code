package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;

import java.time.Instant;
import java.util.List;

public class ChanceOfAsteriods extends AoCDay {
    public void solve(){
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,5,false,0);
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
        intCodeComputer.stopOnFirstOutput(true);
        intCodeComputer.run();
        return intCodeComputer.getDebugValue();
    }

    public Long solutionPart2(List<String> lines) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setInputValue(5L);
        intCodeComputer.run();
        return intCodeComputer.getDebugValue();
    }
}
