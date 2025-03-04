package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RAMRun extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 18, false, 0);
        List<Coord2D> coords = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(coords.subList(0,1024));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(coords);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Coord2D> parseLines(List<String> lines) {
        Pattern pattern = Pattern.compile("(?<x>\\d+),(?<y>\\d+)");
        List<Coord2D> coords = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                coords.add(new Coord2D(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))));
            }
        }
        return coords;
    }

    class RamState {
        @Getter
        int length;
        @Getter
        Coord2D location;
        int hashCode;

        public RamState(int length, Coord2D location) {
            this.length = length;
            this.location = location;
            this.hashCode = Objects.hash(length, location);
        }

        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            RamState other = (RamState) obj;
            return length == other.length && location.equals(other.location);
        }

        boolean onGrid(RamState state) {
            Coord2D location = state.location;
            return (location.x >= 0) && (location.x < 71) && (location.y >= 0) && (location.y < 71);
        }

        List<RamState> nextStatesPart1(List<Coord2D> blocks, Set<Coord2D> visited) {
            return DELTA.stream().map(d -> new RamState(length+1, location.add(d)))
                    .filter(l -> !blocks.contains(l.getLocation()))
                    .filter(l -> !visited.contains(l.getLocation()))
                    .filter(this::onGrid)
                    .toList();
        }
    }

    List<Coord2D> DELTA = List.of(new Coord2D(0,1), new Coord2D(0,-1), new Coord2D(1,0), new Coord2D(-1,0));

    long solutionPart1(List<Coord2D> badBlocks) {
        Coord2D startingPoint = new Coord2D(0,0);
        Coord2D endingPoint = new Coord2D(70,70);
        RamState start = new RamState(0, startingPoint);
        PriorityQueue<RamState> queue = new PriorityQueue<>(300, Comparator.comparingInt(RamState::getLength));
        queue.add(start);
        Set<Coord2D> visited = new HashSet<>();
        while(!queue.isEmpty()) {
            RamState current = queue.poll();
            if (current.getLocation().equals(endingPoint)) {
                return current.getLength();
            }
            if (visited.contains(current.getLocation())) {
                continue;
            }
            visited.add(current.getLocation());
            List<RamState> nextStates = current.nextStatesPart1(badBlocks, visited);
            queue.addAll(nextStates);
        }
        return 0L;
    }

    String solutionPart2(List<Coord2D> coords) {
        Integer min = 1024;
        Integer max = coords.size();
        while ((max-min) > 1) {
            int halfWayThere = (min + max) / 2;
            List<Coord2D> badblocks = coords.subList(0, halfWayThere);
            long turns = solutionPart1(badblocks);
            if (turns != 0L) {
                min = halfWayThere;
            } else {
                max = halfWayThere;
            }
        }
        return coords.get(min).toString();
    }
}
