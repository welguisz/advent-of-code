package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuelingGenerators extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,15,false,0);
        List<Integer> startingValues = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(startingValues.get(0), startingValues.get(1), 40000000L);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(startingValues.get(0), startingValues.get(1), 5000000L);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Integer> parseLines(List<String> lines) {
        Pattern pattern = Pattern.compile("\\d+");
        List<Integer> values = new ArrayList<>();
        for(String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                values.add(Integer.parseInt(matcher.group(0)));
            }
        }
        return values;
    }

    public Long solutionPart1(int generatorAStart, int generatorBStart, Long runCount) {
        Long count = 0L;
        Long loopLeft = runCount;
        Long currentAValue = 0L + generatorAStart;
        Long currentBValue = 0L + generatorBStart;
        while (loopLeft > 0) {
            currentAValue *= 16807;
            currentBValue *= 48271;
            currentAValue %= 2147483647;
            currentBValue %= 2147483647;
            if (lower16BitsEqual(currentAValue,currentBValue)) {
                count++;
            }
            loopLeft--;
        }

        return count;
    }

    public Long solutionPart2(int generatorAStart, int generatorBStart, Long runCount) {
        Long count = 0L;
        Long loopLeft = runCount;
        Long currentAValue = 0L + generatorAStart;
        Long currentBValue = 0L + generatorBStart;
        Queue<Long> queueA = new LinkedList<>();
        Queue<Long> queueB = new LinkedList<>();
        while (loopLeft > 0) {
            currentAValue *= 16807;
            currentBValue *= 48271;
            currentAValue %= 2147483647;
            currentBValue %= 2147483647;
            if (currentAValue % 4 == 0) {
                queueA.add(currentAValue);
            }
            if (currentBValue % 8 == 0) {
                queueB.add(currentBValue);
            }
            if (!queueA.isEmpty() && !queueB.isEmpty()) {
                if (lower16BitsEqual(queueA.remove(), queueB.remove())) {
                    count++;
                }
                loopLeft--;
            }
        }
        return count;
    }

    public Boolean lower16BitsEqual(Long generatorAValue, Long generatorBValue) {
        Long tmpA = generatorAValue & 0xffff;
        Long tmpB = generatorBValue & 0xffff;
        return tmpA.equals(tmpB);
    }

}
