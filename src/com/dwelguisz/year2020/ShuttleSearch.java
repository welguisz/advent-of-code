package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.ChineseRemainderTheorem;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ShuttleSearch extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2020/day13/input.txt");
        Integer arrivalTime = Integer.parseInt(lines.get(0));
        List<String> busIds = Arrays.stream(lines.get(1).split(","))
                .collect(Collectors.toList());
        Integer part1 = solutionPart1(arrivalTime, busIds);
        Long part2 = solutionPart2(busIds);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));

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

    private Pair<Long, Long> extGCD(Long a, Long b) {
        Long x = 1L;
        Long y = 0L;
        Long x1 = 0L;
        Long y1 = 1L;
        Long a1 = a;
        Long b1 = b;

        while (b1 > 0) {
            Long q = Math.floorDiv(a1, b1);
            Long tmp = x;
            x = x1;
            x1 = tmp - q*x1;
            tmp = y;
            y = y1;
            y1 = tmp - q*y1;
            tmp = a1;
            a1 = b1;
            b1 = tmp - q*b1;
        }
        return Pair.of(x,y);
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
