package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.dwelguisz.base.SpecialMath.lcm;

public class HauntedWasteland extends AoCDay {

    Map<String, Pair<String, String>> map;
    String dir;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 8, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void parseLines(List<String> lines) {
        dir = lines.get(0);
        map = new HashMap<>();
        IntStream.range(2, lines.size()).forEach(i -> {
            String l = lines.get(i);
            String key = l.substring(0,3);
            String left = l.substring(7,10);
            String right = l.substring(12,15);
            map.put(key,Pair.of(left,right));
        });
    }

    Long solutionPart1() {
        return walkThePath("AAA", string -> !string.equals("ZZZ"));
    }

    Long walkThePath(String startingPoint, Function<String, Boolean> func) {
        String current = startingPoint;
        long steps = 0;
        while(func.apply(current)) {
            char d = dir.charAt((int) (steps % dir.length()));
            current = (d == 'L') ? map.get(current).getLeft() : map.get(current).getRight();
            steps++;
        }
        return steps;
    }

    Long solutionPart2() {
        List<String> current = map.keySet().stream()
                .filter(l -> l.endsWith("A"))
                .collect(Collectors.toList());
        List<Long> allSteps = current.stream()
                        .map(l -> walkThePath(l,string -> !string.endsWith("Z")))
                .collect(Collectors.toList());
        return allSteps.stream().mapToLong(l -> l).reduce(1L, (a,b) -> lcm(a,b));
    }
}
