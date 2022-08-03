package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.List;

public class InverseCaptcha extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day01/input.txt");
        String line = lines.get(0);
        Long part1 = solutionPart1(line);
        Long part2 = solutionPart2(line);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
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
