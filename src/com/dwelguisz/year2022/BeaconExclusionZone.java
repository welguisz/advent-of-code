package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

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
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day15/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<Sensor> sensors = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(sensors, 2000000);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(sensors);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
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

    public Long solutionPart2(List<Sensor> sensors) {
        for(Integer i = 0; i < 4000000; i++) {
            final Integer line = i;
            List<List<Integer>> ranges = sensors.stream().map(s -> s.getKnownRange(line)).filter(s -> s != null).collect(Collectors.toList());
            ranges = mergeIntervals(ranges);
            if (ranges.size() > 1) {
                Integer hole = ranges.get(0).get(1) + 1;
                System.out.println("Beacon is at (" + hole + ","+i+")");
                Long val = 4000000L * hole;
                return val + i;
            }
        }
        return -1L;
    }
}
