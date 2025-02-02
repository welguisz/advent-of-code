package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class HowYouSliceIt extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,3,false,0);
        Map<Pair<Integer,Integer>,List<Integer>> claimMap = createClaimMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(claimMap);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(claimMap, lines.size());
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Map<Pair<Integer,Integer>,List<Integer>> createClaimMap(List<String> lines) {
        Map<Pair<Integer,Integer>,List<Integer>> claimMap = new HashMap<>();
        for(String line : lines) {
            String split1[] = line.split(" @ ");
            Integer claimId = Integer.parseInt(split1[0].substring(1));
            String information[] = split1[1].split(": ");
            List<Integer> startingPoint = Arrays.stream(information[0].split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> area = Arrays.stream(information[1].split("x")).map(Integer::parseInt).collect(Collectors.toList());
            int startingX = startingPoint.get(0);
            int startingY = startingPoint.get(1);
            int xSize = area.get(0);
            int ySize = area.get(1);
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    Pair<Integer, Integer> newPoint = Pair.of(startingX + x, startingY + y);
                    List<Integer> value = claimMap.getOrDefault(newPoint, new ArrayList<>());
                    value.add(claimId);
                    claimMap.put(newPoint,value);
                }
            }
        }
        return claimMap;
    }

    public Long solutionPart1(Map<Pair<Integer, Integer>, List<Integer>> claimMap) {
        Long overLapInch = 0L;
        for (Map.Entry<Pair<Integer,Integer>,List<Integer>> entry : claimMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                overLapInch++;
            }
        }
        return overLapInch;
    }

    public Long solutionPart2(Map<Pair<Integer, Integer>, List<Integer>> claimMap, Integer numberOfClaims) {
        Set<Long> overlappedClaims = new HashSet<>();
        for(Map.Entry<Pair<Integer,Integer>,List<Integer>> entry : claimMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                for(Integer val : entry.getValue()) {
                    overlappedClaims.add(val.longValue());
                }
            }
        }
        Long totalSum = (numberOfClaims.longValue() * (numberOfClaims + 1))/2;
        Long findSum = overlappedClaims.stream().reduce(0L, (a,b) -> a+ b);
        return totalSum - findSum;
    }

}
