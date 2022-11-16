package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReindeerOlympics extends AoCDay {

    public static class Reindeer {
        String name;
        Integer speed;
        Integer flyTime;
        Integer restTime;

        Integer distanceTraveledInSecondRace;
        Integer timeLeftInPeriod;
        Boolean currentlyFlying;
        Integer currentPoints;
        public Reindeer(String name, Integer speed, Integer flyTime, Integer restTime) {
            this.name = name;
            this.speed = speed;
            this.flyTime = flyTime;
            this.restTime = restTime;
            this.distanceTraveledInSecondRace = 0;
            this.timeLeftInPeriod = this.flyTime;
            this.currentlyFlying = true;
            this.currentPoints = 0;
        }

        public Integer distanceTraveled(int seconds) {
            Integer distance = 0;
            boolean flying = true;
            while (seconds > 0) {
                if (flying) {
                    Integer flyingTime = (seconds > flyTime) ? flyTime : seconds;
                    distance += speed * flyingTime;
                    seconds -= flyingTime;
                    flying = false;
                } else {
                    seconds -= restTime;
                    flying = true;
                }
            }
            return distance;
        }

        public void stepOneSecond() {
            if (currentlyFlying) {
                distanceTraveledInSecondRace += speed;
            }
            timeLeftInPeriod--;
            if (timeLeftInPeriod == 0) {
                timeLeftInPeriod = currentlyFlying ? restTime : flyTime;
                currentlyFlying ^= true;
            }
        }

        public Integer getDistanceTraveledInSecondRance() {
            return distanceTraveledInSecondRace;
        }

        public void atMaxDistance(Integer distance) {
            if (distanceTraveledInSecondRace.equals(distance)) {
                currentPoints++;
            }
        }

        public Integer getCurrentPoints() {
            return currentPoints;
        }
    }

    List<Reindeer> reindeers;

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day14/input.txt");
        createReindeers(lines);
        Integer part1 = findWinnerDistance(2503);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        Integer part2 = findNewWinnerPoints(2503);
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public void createReindeers(List<String> lines) {
        reindeers = new ArrayList<>();
        for(String line : lines) {
            String[] items = line.split(" ");
            String name = items[0];
            Integer speed = Integer.parseInt(items[3]);
            Integer flyTime = Integer.parseInt(items[6]);
            Integer restTime = Integer.parseInt(items[13]);
            reindeers.add(new Reindeer(name,speed,flyTime,restTime));
        }
    }

    public Integer findWinnerDistance(Integer seconds) {
        List<Integer> distances = reindeers.stream().map(r -> r.distanceTraveled(seconds)).collect(Collectors.toList());
        return distances.stream().max(Integer::compareTo).get();
    }

    public Integer findNewWinnerPoints(Integer seconds) {
        for (int i = 0; i < seconds; i++) {
            reindeers.stream().forEach(r -> r.stepOneSecond());
            Integer currentMaxDistance = reindeers.stream().map(Reindeer::getDistanceTraveledInSecondRance).max(Integer::compareTo).get();
            reindeers.stream().forEach(r -> r.atMaxDistance(currentMaxDistance));
        }
        return reindeers.stream().map(Reindeer::getCurrentPoints).max(Integer::compareTo).get();
    }
}
