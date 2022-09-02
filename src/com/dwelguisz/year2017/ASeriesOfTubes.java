package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.List;

public class ASeriesOfTubes extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day19/input.txt");
        String part1 = solutionPart1(lines);
        System.out.println(String.format("Part 1 Answer: %s", part1));

    }

    public String solutionPart1(List<String> lines) {
        StringBuffer answer = new StringBuffer();
        boolean notDone = true;
        Character currentItem = '|';
        int xPosition = lines.get(0).indexOf(currentItem);
        int yPosition = 1;
        while(notDone) {
            Character val = lines.get(yPosition).toCharArray()[xPosition];
            if (currentItem == '|') {

            } else if (currentItem == '-') {

            } else if (currentItem == '+') {

            } else {
                answer.append(val);
            }

        }
        return answer.toString();

    }
}
