package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class ComboBreaker extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,25,false,0);
        List<Long> publicKeys = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(publicKeys);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "Press the link to finish";
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Long> parseLines (List<String> lines) {
        return lines.stream().map(l -> Long.parseLong(l)).collect(Collectors.toList());
    }

    Long discreteLog(Long target, Long base, Long modulus) {
        Long value = 1L;
        Long power = 0L;
        while (!value.equals(target)) {
            value *= base;
            value %= modulus;
            power += 1L;
            if (power >= modulus) {
                return -1L;
            }
        }
        return power;
    }

    Long discretePower(Long base, Long loop, Long modulus) {
        Long value = 1L;
        for (int i = 0; i < loop; i++) {
            value *= base;
            value %= modulus;
        }
        return value;
    }

    Long solutionPart1(List<Long> publicKeys) {
        Long keyPublicKey = publicKeys.get(0);
        Long doorPublicKey = publicKeys.get(1);
        Long cardLoops = discreteLog(keyPublicKey, 7L, 20201227L);
        return discretePower(doorPublicKey, cardLoops, 20201227L);
    }
}
