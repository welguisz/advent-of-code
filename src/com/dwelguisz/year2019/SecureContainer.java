package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SecureContainer extends AoCDay {
    List<Integer> pass1List;

    public void solve() {
        pass1List = new ArrayList<>();
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,4,false,0);
        List<Integer> values = parseLine(lines.get(0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values.get(0), values.get(1));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<Integer> parseLine(String line) {
        List<Integer> values = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?<value1>\\d+)-(?<value2>\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            values.add(Integer.parseInt(matcher.group("value1")));
            values.add(Integer.parseInt(matcher.group("value2")));
        }
        return values;
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
