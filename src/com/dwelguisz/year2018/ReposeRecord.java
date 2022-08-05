package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ReposeRecord extends AoCDay {
    public Map<Integer, Integer> numberOfDaysBefore;

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2018/day04/input.txt");
        numberOfDaysBefore = new HashMap<>();
        numberOfDaysBefore.put(1,0);
        numberOfDaysBefore.put(2,31);
        numberOfDaysBefore.put(3,59);
        numberOfDaysBefore.put(4,90);
        numberOfDaysBefore.put(5,120);
        numberOfDaysBefore.put(6,151);
        numberOfDaysBefore.put(7,181);
        numberOfDaysBefore.put(8,212);
        numberOfDaysBefore.put(9,243);
        numberOfDaysBefore.put(10,273);
        numberOfDaysBefore.put(11,304);
        numberOfDaysBefore.put(12,334);
        PriorityQueue<Pair<Integer,String>> queue = createPQ(lines);
        List<Pair<Integer,String>> orderedList = createOrderedList(queue);
        Map<Integer, List<Integer>> reposeRecord = createReposeRecord(orderedList);
        Long part1 = solutionPart1(reposeRecord);
        Long part2 = solutionPart2(reposeRecord);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    public Long solutionPart1(Map<Integer, List<Integer>> reposeRecord) {
        Integer guardId = -1;
        Integer minutesAsleep = 0;
        for (Map.Entry<Integer, List<Integer>> entry : reposeRecord.entrySet()) {
            if (entry.getValue().size() > minutesAsleep) {
                guardId = entry.getKey();
                minutesAsleep = entry.getValue().size();
            }
        }
        List<Integer> sleepTime = reposeRecord.get(guardId);
        Map<Integer, Integer> sleepMap = new HashMap<>();
        for (Integer time : sleepTime) {
            int minute = time % 60;
            int hour = (time / 60) % 24;
            if (hour == 0) {
                Integer count = sleepMap.getOrDefault(minute,0);
                count++;
                sleepMap.put(minute,count);
            }
        }
        Integer maxMinuteSleep = 0;
        Integer minuteSleep = -1;
        for (Map.Entry<Integer, Integer> entry : sleepMap.entrySet()) {
            if (entry.getValue() > maxMinuteSleep) {
                minuteSleep = entry.getKey();
                maxMinuteSleep = entry.getValue();
            }
        }
        return guardId.longValue() * minuteSleep;
    }

    public Long solutionPart2(Map<Integer, List<Integer>> reposeRecord) {
        Map<Integer,Map<Integer, Integer>> sleepMap = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : reposeRecord.entrySet()) {
            Integer guardId = entry.getKey();
            List<Integer> sleepMinutes = entry.getValue();
            for (Integer sleepMinute : sleepMinutes) {
                Integer trueMinute = sleepMinute % 60;
                Map<Integer,Integer> sleepMinuteEntry = sleepMap.getOrDefault(trueMinute, new HashMap<>());
                Integer value = sleepMinuteEntry.getOrDefault(guardId, 0);
                value++;
                sleepMinuteEntry.put(guardId,value);
                sleepMap.put(trueMinute,sleepMinuteEntry);
            }
        }
        Integer finalGuardId = -1;
        Integer mostMinutesAsleep = -1;
        Integer minuteAsleep = -1;
        for (Map.Entry<Integer, Map<Integer,Integer>> entry : sleepMap.entrySet()) {
            for (Map.Entry<Integer, Integer> guardEntry : entry.getValue().entrySet()) {
                if (guardEntry.getValue() > mostMinutesAsleep) {
                    finalGuardId = guardEntry.getKey();
                    minuteAsleep = entry.getKey();
                    mostMinutesAsleep = guardEntry.getValue();
                }
            }
        }
        return finalGuardId.longValue() * minuteAsleep;
    }

    public Map<Integer, List<Integer>> createReposeRecord(List<Pair<Integer,String>> eventList) {
        Integer guardId = -1;
        Integer sleepStart = -1;
        Map<Integer, List<Integer>> reposeRecord = new HashMap<>();
        for(Pair<Integer, String> event : eventList) {
            if (event.getRight().startsWith("Guard")) {
                guardId = Integer.parseInt(event.getRight().split(" ")[1].substring(1));
            } else if (event.getRight().startsWith("falls")) {
                sleepStart = event.getLeft();
            } else if (event.getRight().startsWith("wakes")) {
                List<Integer> value = reposeRecord.getOrDefault(guardId, new ArrayList<>());
                for (int i = sleepStart; i < event.getLeft(); i++) {
                    value.add(i);
                }
                reposeRecord.put(guardId, value);
            }
        }
        return reposeRecord;
    }

    public PriorityQueue<Pair<Integer, String>> createPQ(List<String> lines) {
        PriorityQueue<Pair<Integer,String>> queue = new PriorityQueue<>(lines.size(),(a,b) -> a.getLeft() - b.getLeft());
        for(String line : lines) {
            String split1[] = line.split("] ");
            Integer timeStamp = timeSinceNewYear(split1[0].substring(1));
            queue.add(Pair.of(timeStamp,split1[1]));
        }
        return queue;
    }

    public List<Pair<Integer,String>> createOrderedList(PriorityQueue<Pair<Integer,String>> queue) {
        List<Pair<Integer,String>> orderedList = new ArrayList<>();
        while(!queue.isEmpty()) {
            orderedList.add(queue.poll());
        }
        return orderedList;
    }

    public Integer timeSinceNewYear(String timeStamp) {
        String timeStampSplit[] = timeStamp.split(" ");
        List<Integer> days = Arrays.stream(timeStampSplit[0].split("-")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> minutes = Arrays.stream(timeStampSplit[1].split(":")).map(Integer::parseInt).collect(Collectors.toList());
        Integer numberOfMinutes = (numberOfDaysBefore.get(days.get(1)) + days.get(2)) * (60 * 24);
        numberOfMinutes += (minutes.get(0) * 60) + minutes.get(1);
        return numberOfMinutes;
    }

    public Long solutionPart1() {
        return 0L;
    }
}
