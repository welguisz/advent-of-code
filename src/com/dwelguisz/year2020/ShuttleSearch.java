package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.ChineseRemainderTheorem;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ShuttleSearch extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,13,false,0);
        Integer arrivalTime = Integer.parseInt(lines.get(0));
        List<String> busIds = Arrays.stream(lines.get(1).split(",")).toList();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(arrivalTime, busIds);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(busIds);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Integer solutionPart1(Integer arrivalTime, List<String> busIds) {
        List<Integer> numIds = busIds.stream()
                .filter(val -> !val.equals("x"))
                .map(val -> Integer.parseInt(val))
                .collect(Collectors.toList());
        Integer minBusId = Integer.MAX_VALUE;
        Integer minTime = Integer.MAX_VALUE;
        for(Integer busId : numIds) {
            Integer diffTime = busId - (arrivalTime % busId);
            if (diffTime < minTime) {
                minTime = diffTime;
                minBusId = busId;
            }
        }
        return minBusId * minTime;
    }

    private Long solutionPart2(List<String> busIds) {
        Long lcv = 0L;
        List<Pair<Long, Long>> pairs = new ArrayList<>();
        for(String busId : busIds) {
            if (!"x".equals(busId)) {
                Long k = lcv;
                Long busNum = Long.parseLong(busId);
                k = k % busNum;
                k = busNum - k;
                k = k % busNum;
                pairs.add(Pair.of(busNum, k));
            }
            lcv++;
        }
        Long[] divisors = new Long[pairs.size()];
        Long[] remainders = new Long[pairs.size()];
        Integer i = 0;
        for(Pair<Long, Long> pair : pairs) {
            divisors[i] = pair.getLeft();
            remainders[i] = pair.getRight();
            i++;
        }
        return ChineseRemainderTheorem.chineseRemainder(divisors, remainders);
    }

    // Brute Force ... Works for small number, but time complexity is horrible
//    private Long solutionPart2(List<String> busIds) {
//        Map<Integer, Long> timeDepart = new HashMap<>();
//        int lcv = 0;
//        for(String busId : busIds) {
//            if (!"x".equals(busId)) {
//                timeDepart.put(lcv, Long.parseLong(busId));
//            }
//            lcv++;
//        }
//        System.out.println(timeDepart);
//        Long startNum = timeDepart.get(0);
//        boolean found = false;
//        while (!found) {
//            boolean possible = true;
//            for(Map.Entry<Integer, Long> entry : timeDepart.entrySet()) {
//                if ((startNum+entry.getKey()) % entry.getValue() != 0) {
//                    possible = false;
//                    break;
//                }
//            }
//            startNum += timeDepart.get(0);
//            found = possible;
//        }
//        return startNum - timeDepart.get(0);
//    }

}
