package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;

import java.util.List;

public class ProgramAlarm extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day02/input.txt");
        Long part1 = solutionPart1(lines, 12L, 2L);
        Long part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 1 Answer: %d",part2));
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
