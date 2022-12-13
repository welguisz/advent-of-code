package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;

import java.util.List;

public class ProgramAlarm extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2019/day02/input.txt");
        Integer part1 = solutionPart1(lines, 12, 2);
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 1 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines, int noun, int verb) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setIntCodeMemory(1, noun);
        intCodeComputer.setIntCodeMemory(2, verb);
        intCodeComputer.run();
        return intCodeComputer.getMemoryLocation(0);
    }

    public Integer solutionPart2(List<String> lines) {
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                Integer value = solutionPart1(lines, noun, verb);
                if (value == 19690720) {
                    return 100 * noun + verb;
                }
            }
        }
        return -1;
    }


}
