package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
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

        public void incPoints() {
            currentPoints++;
        }

        public Integer getCurrentPoints() {
            return currentPoints;
        }
    }

    List<Reindeer> reindeers;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,14,false,0);
        createReindeers(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = findWinnerDistance(2503);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = findNewWinnerPoints(2503);
        timeMarkers[3] = Instant.now().toEpochMilli();
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
        return reindeers.stream().mapToInt(r -> r.distanceTraveled(seconds)).max().getAsInt();
    }

    public Integer findNewWinnerPoints(Integer seconds) {
        for (int i = 0; i < seconds; i++) {
            reindeers.stream().forEach(r -> r.stepOneSecond());
            Integer currentMaxDistance = reindeers.stream().mapToInt(Reindeer::getDistanceTraveledInSecondRance).max().getAsInt();
            reindeers.stream()
                    .filter(r -> r.getDistanceTraveledInSecondRance().equals(currentMaxDistance)).
                    forEach(Reindeer::incPoints);
        }
        return reindeers.stream().mapToInt(Reindeer::getCurrentPoints).max().getAsInt();
    }
}
