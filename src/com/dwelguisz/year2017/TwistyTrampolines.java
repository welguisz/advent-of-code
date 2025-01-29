package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class TwistyTrampolines extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,5,false,0);
        List<Integer> jumpInstructions = lines.stream().map(Integer::parseInt).toList();
        Integer[] jumpInst = jumpInstructions.toArray(new Integer[0]);
        Integer[] part1List = jumpInst.clone();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(part1List);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(jumpInst);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
