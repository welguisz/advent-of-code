package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SecureContainer extends AoCDay {
    List<Integer> pass1List;

    public void solve() {
        pass1List = new ArrayList<>();
        Integer part1 = solutionPart1(153517,630395);
        Integer part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(Integer minNum, Integer maxNum) {
        Integer meetsKnownRules = 0;
        for (Integer checkNum = minNum; checkNum < maxNum; checkNum++) {
            if(isGoodNumber(checkNum)) {
                meetsKnownRules++;
                pass1List.add(checkNum);
            }
        }
        return meetsKnownRules;
    }

    public Integer solutionPart2() {
        Integer meetsKnownRules = 0;
        for (Integer checkMore : pass1List) {
            if(isBetterNumber(checkMore)) {
                meetsKnownRules++;
            }
        }
        return meetsKnownRules;
    }

    public boolean isBetterNumber(Integer num) {
        List<Integer> splitNum = Arrays.stream(num.toString().split("")).map(Integer::parseInt).collect(Collectors.toList());
        Integer count[] = new Integer[10];
        Arrays.fill(count, 0);
        for(int i = 0; i < 6; i++) {
            count[splitNum.get(i)]++;
        }
        for (int i = 0; i < 10; i++) {
            if (count[i] == 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isGoodNumber(Integer num) {
        List<Integer> splitNum = Arrays.stream(num.toString().split("")).map(Integer::parseInt).collect(Collectors.toList());
        //Check Digit Never decreases
        for (int i = 0; i < 5; i++) {
            if (splitNum.get(i) > splitNum.get(i+1)) {
                return false;
            }
        }
        //Check that two digits next to each other are there
        for (int i = 0; i < 5; i++) {
            if (splitNum.get(i) == splitNum.get(i+1)) {
                return true;
            }
        }
        return false;
    }
}
