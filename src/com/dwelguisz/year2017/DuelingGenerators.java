package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class DuelingGenerators extends AoCDay {

    public void solve() {
        Long part1 = solutionPart1(516,190, 40000000L);
        Long part2 = solutionPart2(516,190,5000000L);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long solutionPart1(int generatorAStart, int generatorBStart, Long runCount) {
        Long count = 0L;
        Long loopLeft = runCount;
        Long currentAValue = 0L + generatorAStart;
        Long currentBValue = 0L + generatorBStart;
        while (loopLeft > 0) {
            currentAValue *= 16807;
            currentBValue *= 48271;
            currentAValue %= 2147483647;
            currentBValue %= 2147483647;
            if (lower16BitsEqual(currentAValue,currentBValue)) {
                count++;
            }
            loopLeft--;
        }

        return count;
    }

    public Long solutionPart2(int generatorAStart, int generatorBStart, Long runCount) {
        Long count = 0L;
        Long loopLeft = runCount;
        Long currentAValue = 0L + generatorAStart;
        Long currentBValue = 0L + generatorBStart;
        Queue<Long> queueA = new LinkedList<>();
        Queue<Long> queueB = new LinkedList<>();
        while (loopLeft > 0) {
            currentAValue *= 16807;
            currentBValue *= 48271;
            currentAValue %= 2147483647;
            currentBValue %= 2147483647;
            if (currentAValue % 4 == 0) {
                queueA.add(currentAValue);
            }
            if (currentBValue % 8 == 0) {
                queueB.add(currentBValue);
            }
            if (!queueA.isEmpty() && !queueB.isEmpty()) {
                if (lower16BitsEqual(queueA.remove(), queueB.remove())) {
                    count++;
                }
                loopLeft--;
            }
        }
        return count;
    }

    public Boolean lower16BitsEqual(Long generatorAValue, Long generatorBValue) {
        Long tmpA = generatorAValue & 0xffff;
        Long tmpB = generatorBValue & 0xffff;
        return tmpA.equals(tmpB);
    }

}
