package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SpinLock extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,17,false,0);
        Integer stepCount = Integer.parseInt(lines.get(0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(stepCount, 2017);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(stepCount, 50000000);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
