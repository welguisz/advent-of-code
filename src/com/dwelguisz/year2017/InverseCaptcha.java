package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class InverseCaptcha extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,1,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(String line) {
        char[] chars = line.toCharArray();
        Long sum = 0L;
        for (int i = 0; i < chars.length; i++) {
            int next = (i+1)%chars.length;
            if (chars[i] == chars[next]) {
                sum += Long.parseLong(String.valueOf(chars[i]));
            }
        }
        return sum;
    }
    public Long solutionPart2(String line) {
        char[] chars = line.toCharArray();
        int jump = line.length() / 2;
        Long sum = 0L;
        for (int i = 0; i < chars.length; i++) {
            int next = (i+jump)%chars.length;
            if (chars[i] == chars[next]) {
                sum += Long.parseLong(String.valueOf(chars[i]));
            }
        }
        return sum;
    }
}
