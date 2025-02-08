package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day18.SnailNumber;
import org.json.JSONArray;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Snailfish extends AoCDay {

    private List<SnailNumber> combinations;
    private Stack<SnailNumber> sums;

    public Snailfish() {
        super();
        combinations = new ArrayList<>();
        sums = new Stack<>();
    }

    private SnailNumber parse(Integer i) {
        return new SnailNumber(i);
    }

    private SnailNumber parse(JSONArray a, int level) {
        return new SnailNumber(parse(a.get(0).toString(), level), parse(a.get(1).toString(), level));
    }

    private SnailNumber parse(String s, int level) {
        if (s.length() == 1) {
            return parse(Integer.parseInt(s));
        }
        return parse(new JSONArray(s), level+1);
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021, 18, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Integer solutionPart1(List<String> lines) {
        List<SnailNumber> snailNumbers = new ArrayList<>();
        for (String line : lines) {
            SnailNumber sn = parse(line, 0);
            sums.insertElementAt(sn.clone(), 0);
            combinations.add(sn.clone());
        }
        SnailNumber sum = sums.pop();
        while (!sums.isEmpty()) {
            sum = sum.add(sums.pop());
        }
        return sum.magnitude();
    }

    private Integer solutionPart2() {
        Integer maximumN = -1;
        for (SnailNumber left : combinations) {
            for (SnailNumber right: combinations) {
                if (left.equals(right)) continue;
                maximumN = Math.max(maximumN, left.clone().add(right.clone()).magnitude());
                maximumN = Math.max(maximumN, right.clone().add(left.clone()).magnitude());
            }
        }
        return maximumN;
    }

}
