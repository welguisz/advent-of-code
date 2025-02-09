package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FullOfHotAir extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,25,false,0);
        List<String> parsedClass = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(parsedClass);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<String> parseLines(List<String> lines) {
        List<String> values = new ArrayList<>();
        for (String l : lines) {
            values.add(l);
        }
        return values;
    }

    String solutionPart1(List<String> values) {
        return convertToSnafu(values.stream().mapToLong(v -> convertToLong(v)).sum());
    }

    String convertToSnafu(Long value) {
        while (value > 0) {
            Long remainder = value % 5;
            switch (remainder.intValue()) {
                case 0 : return convertToSnafu(value / 5) +"0";
                case 1 : return convertToSnafu(value / 5) + "1";
                case 2 : return convertToSnafu(value / 5) + "2";
                case 3 : return convertToSnafu((value+2) / 5) + "=";
                case 4 : return convertToSnafu((value+1) / 5) + "-";
            }
        }
        return "";
    }

    Long convertToLong(String value) {
        char[] vals = value.toCharArray();
        Long val = 0L;
        for(Character v : vals) {
            val *= 5L;
            val += convertToLong(v);
        }
        return val;
    }

    Long convertToLong(char c) {
        if (c == '-') {
            return -1L;
        } else if (c == '=') {
            return -2L;
        } else {
            return Long.parseLong("" + c);
        }
    }
    String solutionPart2() {
        return "Click link to finish";
    }
}
