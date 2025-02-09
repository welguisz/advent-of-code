package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BeaconExclusionZone extends AoCDay {

    public static class Sensor {
        Integer id;
        Integer bx;
        Integer by;
        Integer sx;
        Integer sy;
        Integer md;

        public Sensor(Integer id, Integer sx, Integer sy, Integer bx, Integer by) {
            this.id = id;
            this.sx = sx;
            this.sy = sy;
            this.bx = bx;
            this.by = by;
            this.md = manhattanDistance(bx,by,sx,sy);
        }

        public List<Integer> getKnownRange(Integer y) {
            if (Math.abs(y - sy) < md) {
                Integer k = md - Math.abs(sy-y);
                return List.of(sx -k, sx+k);
            }
            return null;
        }

        public Integer manhattanDistance(Integer cx, Integer cy, Integer ax, Integer ay) {
            return Math.abs(cx-ax) + Math.abs(cy-ay);
        }

    }


    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,15,false,0);
        List<Sensor> sensors = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(sensors, 2000000);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer =  solutionPart2(sensors);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<Sensor> parseLines(List<String> lines) {
        List<Sensor> sensors = new ArrayList<>();
        Integer id = 0;
        for (String line : lines) {
            String sp[] = line.split(" ");
            Integer sx = Integer.parseInt(sp[2].substring(2,sp[2].length()-1));
            Integer sy = Integer.parseInt(sp[3].substring(2,sp[3].length()-1));
            Integer bx = Integer.parseInt(sp[8].substring(2, sp[8].length()-1));
            Integer by = Integer.parseInt(sp[9].substring(2));
            sensors.add(new Sensor(id,sx,sy,bx,by));
            id++;
        }
        return sensors;
    }

    public Integer solutionPart1(List<Sensor> sensors, Integer line) {
        List<List<Integer>> sensorRanges = sensors.stream().map(s -> s.getKnownRange(line)).filter(s -> s != null).collect(Collectors.toList());
        //The naive approach takes this one function from 2 ms to 740 ms.
        //Set<Integer> naive = new HashSet<>();
        //for (List<Integer> s : sensorRanges) {
        //    naive.addAll(IntStream.range(s.get(0),s.get(1)).boxed().collect(Collectors.toSet()));
        //}
        //return naive.size();
        sensorRanges = mergeIntervals(sensorRanges);
        return sensorRanges.get(0).get(1)-sensorRanges.get(0).get(0);
    }

    public Integer comparePairs(List<Integer> a, List<Integer> b) {
        return a.get(0) - b.get(0);
    }

    public List<List<Integer>> mergeIntervals(List<List<Integer>> intervals) {
        if (intervals.isEmpty()) {
            return new ArrayList<>();
        }
        intervals = intervals.stream().sorted((a,b) -> comparePairs(a,b)).collect(Collectors.toList());
        int i = 0;
        while (i < intervals.size()-1) {
            if (intervals.get(i).get(1)+1 >= intervals.get(i+1).get(0)) {
                Integer maxV = Integer.max(intervals.get(i).get(1), intervals.get(i+1).get(1));
                List<Integer> tmp = List.of(intervals.get(i).get(0), maxV);
                intervals.remove(i+1);
                intervals.remove(i);
                intervals.add(i,tmp);
                continue;
            }
            i++;
        }
        return intervals;
    }

    public Long runningSum(Integer n) {
        Integer nA = Math.abs(n);
        Long sum = nA.longValue()*(nA+1)/2;
        return sum;
    }

    public Long solutionPart2(List<Sensor> sensors) {
        Long time0 = Instant.now().toEpochMilli();
        for(Integer i = 0; i < 4000000; i++) {
            final Integer line = i;
            List<List<Integer>> ranges = sensors.stream().map(s -> s.getKnownRange(line)).filter(s -> s != null).collect(Collectors.toList());
            // Naive approach for Part 2.  For the first 16 lines, this took the following times:
            // 645 ms, 614 ms, 550 ms, 644 ms, 492 ms, 472 ms, 603 ms, 659 ms, 632 ms, 594 ms, 446 ms, 721 ms
            // 539 ms, 441 ms, 576 ms, 402 ms
            //Long time1 = Instant.now().toEpochMilli();
            //System.out.println("At line: " + i + ", took " + (time1 - time0) + " ms");
            //time0 = time1;
            //Set<Integer> naive = new HashSet<>();
            //for (List<Integer> s : ranges) {
            //    naive.addAll(IntStream.range(s.get(0),s.get(1)).boxed().collect(Collectors.toSet()));
            //}
            //Integer minV = naive.stream().mapToInt(in -> in).min().getAsInt();
            //Integer maxV = naive.stream().mapToInt(in -> in).max().getAsInt();
            //Long sumV = naive.stream().mapToLong(in -> in).sum();
            //Long minSum = runningSum(minV);
            //Long maxSum = runningSum(maxV);
            //Long diff = (maxSum - minSum) - sumV;
            //if (diff > 0) {
            //    diff /= 2;
            //    diff +=1;
            //    System.out.println("Beacon is at (" + diff + ","+i+")");
            //    Long val = 4000000L * diff;
            //    return val + i;
            //}
            ranges = mergeIntervals(ranges);
            if (ranges.size() > 1) {
                Integer hole = ranges.get(0).get(1) + 1;
                Long val = 4000000L * hole;
                return val + i;
            }
        }
        return -1L;
    }
}
