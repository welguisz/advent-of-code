package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MullItOver extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 3, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    long solutionPart1(List<String> lines) {
        Pattern pattern = Pattern.compile("mul\\((?<mult1>\\d{1,3}),(?<mult2>\\d{1,3})\\)");
        long sum = 0;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String mult1 = matcher.group("mult1");
                String mult2 = matcher.group("mult2");
                sum += Long.parseLong(mult1) * Long.parseLong(mult2);
            }
        }
        return sum;
    }

    long solutionPart2(List<String> lines) {
        Pattern pattern = Pattern.compile("(?<enabled>do\\(\\))|(?<mull>mul\\()(?<mult1>\\d{1,3}),(?<mult2>\\d{1,3})\\)|(?<disabled>don't\\(\\))");
        boolean enabled = true;
        long sum = 0;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String enabledStr = matcher.group("enabled");
                String disabledStr = matcher.group("disabled");
                String mulStr = matcher.group("mull");
                if (enabledStr != null) {
                    enabled = true;
                } else if (disabledStr != null) {
                    enabled = false;
                } else if (enabled && mulStr != null) {
                    String mult1 = matcher.group("mult1");
                    String mult2 = matcher.group("mult2");
                    sum += Long.parseLong(mult1) * Long.parseLong(mult2);
                }
            }
        }
        return sum;
    }
}
