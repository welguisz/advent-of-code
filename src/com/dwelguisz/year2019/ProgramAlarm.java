package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramAlarm extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2019/day02/input.txt");
        Map<Integer, Integer> intCode = new HashMap<>();
        Integer address = 0;
        for(String value : lines.get(0).split(",")){
            intCode.put(address,Integer.parseInt(value));
            address++;
        }
        Map<Integer, Integer> tempIntCode = new HashMap<>();
        tempIntCode.putAll(intCode);
        Integer part1 = solutionPart1(tempIntCode, 12, 2);
        Integer part2 = solutionPart2(intCode);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 1 Answer: %d",part2));
    }

    public Integer solutionPart1(Map<Integer, Integer> intCode, int noun, int verb) {
        intCode.put(1,noun);
        intCode.put(2,verb);
        IntCodeComputer intCodeComputer = new IntCodeComputer(intCode);
        intCodeComputer.run();
        return intCodeComputer.getMemoryLocation(0);
    }

    public Integer solutionPart2(Map<Integer, Integer> intCode) {
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                Map<Integer, Integer> tempIntCode = new HashMap<>();
                tempIntCode.putAll(intCode);
                Integer value = solutionPart1(tempIntCode, noun, verb);
                if (value == 19690720) {
                    return 100 * noun + verb;
                }
            }
        }
        return -1;
    }


}
