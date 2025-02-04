package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramAlarm extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,2,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines, 12L, 2L);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<String> lines, Long noun, Long verb) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setIntCodeMemory(1L, noun);
        intCodeComputer.setIntCodeMemory(2L, verb);
        intCodeComputer.run();
        return intCodeComputer.getMemoryLocation(0L);
    }

    public Long solutionPart2(List<String> lines) {
        for (Long noun = 0L; noun < 100; noun++) {
            for (Long verb = 0L; verb < 100; verb++) {
                Long value = solutionPart1(lines, noun, verb);
                if (value == 19690720L) {
                    return 100 * noun + verb;
                }
            }
        }
        return -1L;
    }


}
