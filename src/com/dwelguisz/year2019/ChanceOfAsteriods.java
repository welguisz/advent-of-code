package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;

import java.util.List;

public class ChanceOfAsteriods extends AoCDay {
    public void solve(){
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day05/input.txt");
        Integer part1 = solutionPart1(lines);
        Integer part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    public Integer solutionPart1(List<String> lines) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setInputValue(1);
        intCodeComputer.stopOnFirstOutput(true);
        intCodeComputer.run();
        return intCodeComputer.getDebugValue();
    }

    public Integer solutionPart2(List<String> lines) {
        IntCodeComputer intCodeComputer = new IntCodeComputer();
        intCodeComputer.initializeIntCode(lines);
        intCodeComputer.setInputValue(5);
        intCodeComputer.run();
        return intCodeComputer.getOutputValue().getRight();
    }
}
