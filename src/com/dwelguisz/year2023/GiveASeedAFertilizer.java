package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3DLong;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class GiveASeedAFertilizer extends AoCDay {

    List<Long> seeds = new ArrayList<>();
    List<List<Coord3DLong>> transformers = new ArrayList<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 5, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void parseLines(List<String> lines) {
        List<Coord3DLong> transforms = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("map")) {
                if (!transforms.isEmpty()) {
                    transformers.add(new ArrayList<>(transforms));
                    transforms = new ArrayList<>();
                }
            } else if (line.length() != 0) {
                if (line.startsWith("seeds:")) {
                    seeds = Arrays.stream(line.split(": ")[1].split("\\s+")).map(Long::parseLong).collect(Collectors.toList());
                }
                else {
                    List<Long> nums = Arrays.stream(line.split("\\s+")).map(Long::parseLong).collect(Collectors.toList());
                    transforms.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                }
            }
        }
        transformers.add(transforms);
    }

    private Long processMap(List<Coord3DLong> map, Long val) {
        for(Coord3DLong m : map) {
            if (m.y <= val && (val < (m.y + m.z))) {
                return val + m.x - m.y;
            }
        }
        return val;
    }

    private Long solutionPart1() {
        return seeds.stream()
                .mapToLong(l -> processAllMapsByOne(l))
                .min().getAsLong();
    }

    private Long processAllMapsByOne(Long seed) {
        Long value = seed;
        for (List<Coord3DLong> transforms : transformers) {
            value = processMap(transforms, value);
        }
        return value;
    }

    private Long processAllMapsByRange(Pair<Long,Long> seed) {
        Long seedEnd = seed.getLeft() + seed.getRight();
        //Sample here
        Long delta = (long) Math.sqrt(seed.getRight());
        PriorityQueue<Pair<Long,Long>> pq = new PriorityQueue<>(20000, Comparator.comparingLong(Pair::getRight));
        //Todo: Can this be turned into a LongStream
        for (Long s = seed.getLeft(); s < seedEnd; s += delta) {
            Long value = processAllMapsByOne(s);
            pq.add(Pair.of(s,value));
        }
        Pair<Long,Long> mins = pq.poll();
        Long seedSubStart = (mins.getLeft() - delta) < seed.getLeft() ? seed.getLeft() : mins.getLeft() - delta;
        Long seedSubEnd = (mins.getLeft() + delta) > seedEnd ? seedEnd : mins.getLeft() + delta;
        return LongStream.range(seedSubStart, seedSubEnd)
                .map(l -> processAllMapsByOne(l))
                .min().getAsLong();
    }


    private Long solutionPart2() {
        List<Pair<Long, Long>> newSeeds = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i+=2) {
            newSeeds.add(Pair.of(seeds.get(i), seeds.get(i+1)));
        }
        return newSeeds.stream()
                .map(s -> processAllMapsByRange(s))
                .min(Long::compare).get();
    }
}
