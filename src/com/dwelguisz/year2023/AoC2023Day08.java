package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AoC2023Day08 extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 8, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Map<String, Pair<String, String>> map;
    String dir;
    void parseLines(List<String> lines) {
        dir = lines.get(0);
        map = new HashMap<>();
        int i = 0;
        for (String l : lines) {
            if (i < 2) {
                i++;
                continue;
            }
            String key = l.substring(0,3);
            String left = l.substring(7,10);
            String right = l.substring(12,15);
            map.put(key,Pair.of(left,right));
            i++;
        }
    }

    Long solutionPart1(List<String> lines) {
        String current = "AAA";
        long steps = 0;
        while (!current.equals("ZZZ")) {
            char d = dir.charAt((int) (steps % dir.length()));
            current = (d == 'L') ? map.get(current).getLeft() : map.get(current).getRight();
            steps++;
        }
        return steps;
    }

    Long solutionPart2(List<String> lines) {
        List<String> current = map.keySet().stream()
                .filter(l -> l.endsWith("A"))
                .collect(Collectors.toList());
        boolean done = false;
        long steps = 0;
        List<Long> allSteps = current.stream()
                        .map(l -> {
                            long s = 0;
                            String c = l;
                            while (!c.endsWith("Z")) {
                                char d = dir.charAt((int) (s % dir.length()));
                                c = (d == 'L') ? map.get(c).getLeft() : map.get(c).getRight();
                                s++;
                            }
                            return s;
                        })
                .collect(Collectors.toList());
        return allSteps.stream().mapToLong(l -> l).reduce(1L, (a,b) -> lcm(a,b));
    }

    public Long lcm(Long a, Long b) {
        Long gcd = gcd(a,b);
        return (a*b)/gcd;
    }
    public Long gcd(Long n1, Long n2) {
        if (n1 == 0) {
            return n2;
        } else if (n2 == 0) {
            return n1;
        }
        Long i1 = n1;
        Long i2 = n2;
        while (i2 != 0L) {
            Long tmp = i1;
            i1 = i2;
            i2 = tmp % i2;
        }
        return i1;
    }

}
