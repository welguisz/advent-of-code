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
        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
        long sum = 0;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String[] split = matcher.group(0).split("\\(");
                String[] split1 = split[1].split("\\)");
                String[] nums = split1[0].split(",");
                sum += Long.parseLong(nums[0]) * Long.parseLong(nums[1]);
            }
        }
        return sum;
    }

    long solutionPart2(List<String> lines) {
        Pattern pattern = Pattern.compile("do\\(\\)|mul\\(\\d{1,3},\\d{1,3}\\)|don't\\(\\)");
        boolean enabled = true;
        long sum = 0;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String[] split = matcher.group(0).split("\\(");
                if (split[0].equals("do")) {
                    enabled = true;
                } else if (split[0].equals("don't")) {
                    enabled = false;
                } else if (enabled && split[0].equals("mul")) {
                    String[] split1 = split[1].split("\\)");
                    String[] nums = split1[0].split(",");
                    sum += Long.parseLong(nums[0]) * Long.parseLong(nums[1]);
                }
            }
        }
        return sum;
    }
}
