package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class HowYouSliceIt extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2018/day03/input.txt");
        Map<Pair<Integer,Integer>,List<Integer>> claimMap = createClaimMap(lines);
        Long part1 = solutionPart1(claimMap);
        Long part2 = solutionPart2(claimMap, lines.size());
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 1 Answer: %d",part2));

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
