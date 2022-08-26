package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class SpinLock extends AoCDay {
    public void solve() {
        Integer part1 = solutionPart1(382, 2017);
        Integer part2 = solutionPart2(382, 50000000);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public Integer solutionPart1(Integer stepCount, Integer length) {
        List<Integer> circularBuffer = new ArrayList<>();
        circularBuffer.add(0);
        Integer currentPosition = 0;
        for (int i = 1; i <= length; i++) {
            currentPosition = (currentPosition + stepCount) % circularBuffer.size();
            circularBuffer.add(currentPosition+1,i);
            currentPosition++;
        }
        return circularBuffer.get((currentPosition+1)% circularBuffer.size());
    }

    public Integer solutionPart2(Integer stepCount, Integer length) {
        int currentPosition = 0;
        int result = 0;
        int n = 0;
        while (n < length) {
            if (currentPosition == 1) {
                result = n;
            }
            int fits = (n-currentPosition)/stepCount;
            n+=(fits+1);
            currentPosition = ((currentPosition + (fits+1)*(stepCount+1) - 1) % n) + 1;
        }
        return result;
    }

}
