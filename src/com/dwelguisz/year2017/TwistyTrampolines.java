package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.List;
import java.util.stream.Collectors;

public class TwistyTrampolines extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day05/input.txt");
        List<Integer> jumpInstructions = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
        Integer[] jumpInst = jumpInstructions.toArray(new Integer[0]);
        Integer[] part1List = jumpInst.clone();
        Integer part1 = solutionPart1(part1List);
        Long part2 = solutionPart2(jumpInst);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(Integer[] jumpInstructions) {
        int stepCount = 0;
        int programCounter = 0;
        while (programCounter < jumpInstructions.length) {
            int newProgramCounter = programCounter + jumpInstructions[programCounter];
            jumpInstructions[programCounter]++;
            programCounter = newProgramCounter;
            stepCount++;
        }
        return stepCount;
    }

    public Long solutionPart2(Integer[] jumpInstructions) {
        Long stepCount = 0L;
        int programCounter = 0;
        while (programCounter < jumpInstructions.length) {
            int newProgramCounter = programCounter + jumpInstructions[programCounter];
            if (jumpInstructions[programCounter] > 2) {
                jumpInstructions[programCounter]--;
            } else {
                jumpInstructions[programCounter]++;
            }
            programCounter = newProgramCounter;
            stepCount++;
        }
        return stepCount;
    }


}
