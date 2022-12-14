package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class SensorBoost extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day09/input.txt");
        Long part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
    }

    public Long solutionPart1(List<String> lines) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setInputValue(1L);
        intCodeComputer.run();
        Boolean hasData = true;
        while (hasData) {
            Pair<Boolean, Long> result = intCodeComputer.getOutputValue();
            hasData = result.getLeft();
            if (hasData) {
                System.out.print(result.getRight() + " ");
            }
        }
        System.out.println();
        return intCodeComputer.getDebugValue();

    }
}
