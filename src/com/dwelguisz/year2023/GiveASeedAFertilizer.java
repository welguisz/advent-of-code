package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3DLong;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GiveASeedAFertilizer extends AoCDay {

    List<Long> seeds = new ArrayList<>();
    List<Coord3DLong> seedToSoilMap = new ArrayList<>();
    List<Coord3DLong> soilToFertilizerMap = new ArrayList<>();
    List<Coord3DLong> fertilizerToWaterMap = new ArrayList<>();
    List<Coord3DLong> waterToLightMap = new ArrayList<>();
    List<Coord3DLong> lightToTemperatureMap = new ArrayList<>();
    List<Coord3DLong> temperatureToHumidityMap = new ArrayList<>();
    List<Coord3DLong> humidityToLocationMap = new ArrayList<>();

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
        int tmp = 0;
        for (String line : lines) {
            if (line.length() == 0) {
                tmp++;
            } else if (line.contains("map")) {

            } else {
                if (tmp == 0) {
                    seeds = Arrays.stream(line.split(": ")[1].split(" ")).map(Long::parseLong).collect(Collectors.toList());
                } else if (tmp == 1) {
                    List<Long> nums = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                    seedToSoilMap.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                } else if (tmp == 2) {
                    List<Long> nums = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                    soilToFertilizerMap.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                } else if (tmp == 3) {
                    List<Long> nums = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                    fertilizerToWaterMap.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                } else if (tmp == 4) {
                    List<Long> nums = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                    waterToLightMap.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                } else if (tmp == 5) {
                    List<Long> nums = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                    lightToTemperatureMap.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                } else if (tmp == 6) {
                    List<Long> nums = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                    temperatureToHumidityMap.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                } else if (tmp == 7) {
                    List<Long> nums = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                    humidityToLocationMap.add(new Coord3DLong(nums.get(0), nums.get(1), nums.get(2)));
                }
            }
        }
    }

    private Long processMap(List<Coord3DLong> map, Long val) {
        for(Coord3DLong m : map) {
            if (m.y <= val && (val < (m.y + m.z))) {
                return m.x + (val - m.y);
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
        final Long soil = processMap(seedToSoilMap, seed);
        final Long fertilizer = processMap(soilToFertilizerMap, soil);
        final Long water = processMap(fertilizerToWaterMap, fertilizer);
        final Long light = processMap(waterToLightMap, water);
        final Long temperature = processMap(lightToTemperatureMap, light);
        final Long humidity = processMap(temperatureToHumidityMap, temperature);
        return processMap(humidityToLocationMap, humidity);
    }

    private Long processAllMapsByRange(Pair<Long,Long> seed) {
        Long seedEnd = seed.getLeft() + seed.getRight();
        //Sample here
        Long delta = (long) Math.sqrt(seed.getRight());
        Long seedMin = Long.MAX_VALUE;
        Long locationMin = Long.MAX_VALUE;
        for (Long s = seed.getLeft(); s < seedEnd; s += delta) {
            Long value = processAllMapsByOne(s);
            if (value < locationMin) {
                locationMin = value;
                seedMin = s;
            }
        }
        Long value = processAllMapsByOne(seedEnd);
        if (value < locationMin) {
            locationMin = value;
            seedMin = seedEnd;
        }
        System.out.println(String.format("seedMin: %d; locationMin: %d", seedMin, locationMin));
        Long seedSubStart = (seedMin - delta) < seed.getLeft() ? seed.getLeft() : seedMin - delta;
        Long seedSubEnd = (seedMin + delta) > seedEnd ? seedEnd : seedMin + delta;
        for (Long s = seedSubStart; s < seedSubEnd; s+= 1) {
            value = processAllMapsByOne(s);
            if (value < locationMin) {
                locationMin = value;
            }
        }
        System.out.println(String.format("locationMin: %d", locationMin));
        return locationMin;
    }


    private Long solutionPart2() {
        List<Pair<Long, Long>> newSeeds = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i+=2) {
            newSeeds.add(Pair.of(seeds.get(i), seeds.get(i) + seeds.get(i+1)));
        }
        List<Long> mins = new ArrayList<>();
        for (Pair<Long, Long> s : newSeeds) {
            mins.add(processAllMapsByRange(s));
        }
        List<Long> ordered = mins.stream()
                .filter(v -> v > 0)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Min list: " + ordered.stream()
                        .map(l -> l.toString())
                .collect(Collectors.joining(", ")));
        return ordered.get(0);
    }
}
