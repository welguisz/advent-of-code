package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ExperimentalEmergencyTeleportation extends AoCDay {

    public static class NanoBot {
        public Coord3D position;
        public Long radius;
        private final int hashCode;
        public List<NanoBot> otherBotsContained;
        public NanoBot(Coord3D position, Long radius) {
            this.position = position;
            this.radius = radius;
            this.hashCode = Objects.hash(position, radius);
            this.otherBotsContained = new ArrayList<>();
        }

        public boolean inRange(NanoBot other) {
            return this.position.manhattanDistance(other.position) <= this.radius;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        public Integer distancetoOrigin() {
            return this.position.manhattanDistance(new Coord3D(0,0,0));
        }

        public Integer overlapsAmount() {
            return this.otherBotsContained.size();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            NanoBot other = (NanoBot) o;
            return (this.position.equals(other.position)) && (this.radius.equals(other.radius));
        }

        public void calculateOverlay(List<NanoBot> bots) {
            otherBotsContained = new ArrayList<>();
            for (NanoBot bot : bots) {
                Integer distance = this.position.manhattanDistance(bot.position);
                if (distance <= this.radius + bot.radius) {
                    otherBotsContained.add(bot);
                }
            }
        }
    }

    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day23/input.txt");
        List<NanoBot> bots = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(bots);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(bots);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public List<NanoBot> parseLines(List<String> lines) {
        List<NanoBot> bots = new ArrayList<>();
        for (String l : lines) {
            String tmp[] = l.split(", ");
            Long radius = Long.parseLong(tmp[1].substring(2));
            String coords = tmp[0].substring(5,tmp[0].length()-1);
            String cs[] = coords.split(",");
            Coord3D loc = new Coord3D(Integer.parseInt(cs[0]),Integer.parseInt(cs[1]),Integer.parseInt(cs[2]));
            bots.add(new NanoBot(loc, radius));
        }
        return bots;
    }

    public Long solutionPart1(List<NanoBot> bots) {
        Long maxRadius = bots.stream().mapToLong(b -> b.radius).max().getAsLong();
        NanoBot maxBot = bots.stream().filter(b -> b.radius.equals(maxRadius)).collect(Collectors.toList()).get(0);
        return bots.stream().filter(b -> maxBot.inRange(b)).count();
    }

    // 121493969 -- Too Low
    // 196650906 -- Too High
    public Long solutionPart2(List<NanoBot> bots) {
        Integer minX = bots.stream().mapToInt(b -> b.position.x).min().getAsInt();
        Integer maxX = bots.stream().mapToInt(b -> b.position.x).max().getAsInt();
        Integer minY = bots.stream().mapToInt(b -> b.position.y).min().getAsInt();
        Integer maxY = bots.stream().mapToInt(b -> b.position.y).max().getAsInt();
        Integer minZ = bots.stream().mapToInt(b -> b.position.z).min().getAsInt();
        Integer maxZ = bots.stream().mapToInt(b -> b.position.z).max().getAsInt();
        Integer startX = (maxX+minX)/2;
        Integer startY = (maxY+minY)/2;
        Integer startZ = (maxZ+minZ)/2;
        NanoBot startingBot = startOctohedron(new Coord3D(startX, startY, startZ), bots);
        PriorityQueue<NanoBot> pQ =  new PriorityQueue<>(10, new Comparator<NanoBot>() {
            @Override
            public int compare(NanoBot x, NanoBot y) {
                if (x.overlapsAmount().equals(y.overlapsAmount())) {
                    return x.distancetoOrigin().compareTo(y.distancetoOrigin());
                } else {
                    return -1 * x.overlapsAmount().compareTo(y.overlapsAmount());
                }
            }
        });
        pQ.add(startingBot);
        while (!pQ.isEmpty()) {
            NanoBot n = pQ.poll();
            if (n.radius.equals(0L)) {
                return n.distancetoOrigin().longValue();
            }
            pQ.addAll(splitNanobot(n, bots));
        }
        return -1L;
    }

    public List<NanoBot> splitNanobot(NanoBot src, List<NanoBot> bots) {
        List<NanoBot> result = new ArrayList<>();
        Long newRadius = 0L;
        Long offset = 0L;
        if (src.radius.equals(1L)) {
            result.add(new NanoBot(new Coord3D(src.position.x, src.position.y, src.position.z),newRadius));
        }
        else if (src.radius.equals(2L)) {
            newRadius = 1L;
        } else {
            newRadius = (long) Math.ceil(0.556*src.radius);
            offset = src.radius - newRadius;
        }
        result.add(new NanoBot(new Coord3D((int) (src.position.x - offset), src.position.y, src.position.z),newRadius));
        result.add(new NanoBot(new Coord3D((int) (src.position.x + offset), src.position.y, src.position.z),newRadius));
        result.add(new NanoBot(new Coord3D(src.position.x,(int) (src.position.y - offset), src.position.z),newRadius));
        result.add(new NanoBot(new Coord3D(src.position.x,(int) (src.position.y + offset), src.position.z),newRadius));
        result.add(new NanoBot(new Coord3D(src.position.x,src.position.y,(int) (src.position.z - offset)),newRadius));
        result.add(new NanoBot(new Coord3D(src.position.x,src.position.y,(int) (src.position.z + offset)),newRadius));
        result.stream().forEach (r -> r.calculateOverlay(bots));
        return result;
    }

    public NanoBot startOctohedron(Coord3D starting, List<NanoBot> bots) {
        NanoBot result = new NanoBot(starting, 1L);
        while (!bots.stream().allMatch(b -> result.inRange(b))) {
            result.radius *= 2L;
        }
        result.calculateOverlay(bots);
        return result;
    }
}
