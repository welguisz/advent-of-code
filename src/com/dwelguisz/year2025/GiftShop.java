package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GiftShop extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 2, false, 0);
        List<Pair<Long,Long>> values = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Pair<Long,Long>> parseLines(List<String> lines) {
        List<Pair<Long,Long>> values = new ArrayList<>();
        List<String> items = List.of(lines.get(0).split(","));
        for(String item : items) {
            String[] coords = item.split("-");
            values.add(Pair.of(Long.parseLong(coords[0]), Long.parseLong(coords[1])));
        }
        return values;
    }

    public long solutionPart1(List<Pair<Long,Long>> values) {
        long answer = 0l;
        for (Pair<Long,Long> coord : values) {
            for (long lcv = coord.getLeft(); lcv <= coord.getRight(); lcv++) {
                answer += invalidPart1(lcv) ? lcv : 0;
            }
        }
        return answer;
    }

    public boolean invalidPart1(long lcv) {
        String newValue = "" + lcv;
        int size = newValue.length();
        if (size % 2 != 0) {return false;}
        int half = size / 2;
        String firstHalf = newValue.substring(0, half);
        String secondHalf = newValue.substring(size - half, size);
        return firstHalf.equals(secondHalf);
    }

    public long solutionPart2(List<Pair<Long,Long>> values) {
        long answer = 0l;
        for (Pair<Long,Long> coord : values) {
            for (long lcv = coord.getLeft(); lcv <= coord.getRight(); lcv++) {
                answer += invalidPart2(lcv) ? lcv : 0;
            }
        }
        return answer;
    }

    public boolean invalidPart2(long lcv) {
        String newValue = "" + lcv;
        int size = newValue.length();
        //TODO: There must be a regex that can make this better
        // Maybe something of \d{size}\1
        for (int i = 1; i < size; i++) {
            if (size % i == 0) {
                int replicateSize = size / i;
                String base = newValue.substring(0, i);
                String newString = base.repeat(replicateSize);
                if(newString.toString().equals(newValue)) {
                    return true;
                }
            }
        }
        return false;
    }

}
