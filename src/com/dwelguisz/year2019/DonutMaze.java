package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DonutMaze extends AoCDay {
    Map<String, List<Coord2D>> telePorts;
    Map<Coord2D, Coord2D> telePortSwitch;
    Integer rightEdge;
    Integer bottomEdge;
    Integer topEdge;
    Integer leftEdge;

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day20/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        Map<Coord2D, String> locations = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(locations);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(locations);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    Map<Coord2D, String> parseLines(List<String> lines) {
        Map<Coord2D, String> locations = new HashMap<>();
        telePorts = new HashMap<>();
        Integer y = 0;
        for (String l : lines) {
            String[] t = l.split("");
            for (int x = 0; x < t.length; x++) {
                if (t[x].equals(" ") || t[x].equals("#")) {
                    continue;
                } else if (t[x].equals(".")) {
                    Coord2D posLoc = new Coord2D(y,x);
                    locations.put(posLoc,t[x]);
                } else { //
                    Coord2D loc = new Coord2D(y,x);
                    List<Coord2D> possibleLocs = List.of(
                            loc.add(new Coord2D(-1,0)),
                            loc.add(new Coord2D(0,1)),
                            loc.add(new Coord2D(1,0)),
                            loc.add(new Coord2D(0,-1)));
                    List<Coord2D> entrances = possibleLocs.stream()
                            .filter(lo -> lo.x >= 0)
                            .filter(lo -> lo.y >= 0)
                            .filter(lo -> lo.x < lines.size())
                            .filter(lo -> lo.y < lines.get(lo.x).length())
                            .filter(lo -> lines.get(lo.x).charAt(lo.y) == '.').collect(Collectors.toList());
                    if (entrances.isEmpty()) {
                        continue;
                    }
                    Coord2D entrance = entrances.get(0);
                    Coord2D diff = new Coord2D(-entrance.x, -entrance.y).add(loc);
                    String name = null;
                    if (diff.x + diff.y < 0) {
                        name = lines.get(loc.x+diff.x).charAt(loc.y+diff.y)+ t[x];
                    } else {
                        name = t[x] + lines.get(loc.x+diff.x).charAt(loc.y+diff.y);
                    }
                    List<Coord2D> tmp = telePorts.getOrDefault(name, new ArrayList<>());
                    tmp.add(entrance);
                    telePorts.put(name, tmp);
                    locations.put(entrance, name);
                }
            }
            y++;
        }
        bottomEdge = locations.entrySet().stream().mapToInt(e -> e.getKey().x).max().getAsInt();
        rightEdge = locations.entrySet().stream().mapToInt(e -> e.getKey().y).max().getAsInt();
        topEdge = locations.entrySet().stream().mapToInt(e -> e.getKey().x).min().getAsInt();
        leftEdge = locations.entrySet().stream().mapToInt(e -> e.getKey().y).min().getAsInt();

        telePortSwitch = new HashMap<>();
        for (Map.Entry<String, List<Coord2D>> e : telePorts.entrySet()) {
            if (e.getValue().size() < 2) {
                continue;
            }
            List<Coord2D> s = e.getValue();
            telePortSwitch.put(s.get(0), s.get(1));
            telePortSwitch.put(s.get(1), s.get(0));
        }
        return locations;
    }

    Long solutionPart1 (Map<Coord2D, String> locations) {
        Coord2D startingPoint = telePorts.get("AA").get(0);
        Coord2D endingPoint = telePorts.get("ZZ").get(0);
        return findShortestPathNoRecursion(0, startingPoint, endingPoint, locations).longValue();
    }

    Long solutionPart2(Map<Coord2D, String> locations) {
        Coord2D startingPoint = telePorts.get("AA").get(0);
        Coord2D endingPoint = telePorts.get("ZZ").get(0);
        Coord3D sP = new Coord3D(startingPoint.x, startingPoint.y, 0);
        Coord3D eP = new Coord3D(endingPoint.x, endingPoint.y, 0);
        return findShortestPathRecursion(0, sP, eP, locations).longValue();
    }

    Stream<Coord2D> nextSpotsNoRecursion(Coord2D loc, Map<Coord2D, String> locations, Set<Coord2D> visited) {
        if (telePortSwitch.containsKey(loc)) {
            if (!visited.contains(telePortSwitch.get(loc))) {
                return Stream.of(telePortSwitch.get(loc));
            }
        }
        List<Coord2D> possibleLocs = List.of(
                loc.add(new Coord2D(-1,0)),
                loc.add(new Coord2D(0,1)),
                loc.add(new Coord2D(1,0)),
                loc.add(new Coord2D(0,-1)));
        return possibleLocs.stream()
                .filter(lo -> !visited.contains(lo))
                .filter(n -> locations.containsKey(n));
    }

    public Integer findShortestPathNoRecursion(
            Integer initialTime,
            Coord2D startingLocation,
            Coord2D endingLocation,
            Map<Coord2D, String> locations
    ) {
        Set<Coord2D> states = new HashSet<>();
        Set<Coord2D> seen = new HashSet<>();
        states.add(startingLocation);

        for (Integer i = initialTime; true; i++) {
            seen.addAll(states);
            states = states.stream().flatMap(s -> nextSpotsNoRecursion(s,locations,seen)).collect(Collectors.toSet());
            if (states.stream().anyMatch(s -> s.equals(endingLocation))) {
                return i+1;
            }
        }
    }

    Stream<Coord3D> nextSpotsRecursion(Coord3D loc3D, Map<Coord2D, String> locations, Set<Coord3D> visited) {
        Coord2D loc = new Coord2D(loc3D.x, loc3D.y);
        Integer level = loc3D.z;
        if (telePortSwitch.containsKey(loc)) {
            Coord2D jump = telePortSwitch.get(loc);
            if ((loc.y == leftEdge) || (loc.y == rightEdge) || (loc.x == topEdge) || (loc.x == bottomEdge)) {
                if (level > 0) {
                    Coord3D nextStep = new Coord3D(jump.x, jump.y, level-1);
                    if (!visited.contains(nextStep)) {
                        return Stream.of(nextStep);
                    }
                }
            } else {
                Coord3D nextStep = new Coord3D(jump.x, jump.y, level+1);
                if (!visited.contains(nextStep)) {
                    return Stream.of(nextStep);
                }
            }
        }
        List<Coord2D> possibleLocs = List.of(
                loc.add(new Coord2D(-1,0)),
                loc.add(new Coord2D(0,1)),
                loc.add(new Coord2D(1,0)),
                loc.add(new Coord2D(0,-1)));
        return possibleLocs.stream()
                .filter(lo -> !visited.contains(new Coord3D(lo.x,lo.y,level)))
                .filter(n -> locations.containsKey(n))
                .map(n -> new Coord3D(n.x, n.y, level));
    }

    public Integer findShortestPathRecursion(
            Integer initialTime,
            Coord3D startingLocation,
            Coord3D endingLocation,
            Map<Coord2D, String> locations
    ) {
        Set<Coord3D> states = new HashSet<>();
        Set<Coord3D> seen = new HashSet<>();
        states.add(startingLocation);

        for (Integer i = initialTime; true; i++) {
            seen.addAll(states);
            states = states.stream().flatMap(s -> nextSpotsRecursion(s,locations,seen)).collect(Collectors.toSet());
            if (states.stream().anyMatch(s -> s.equals(endingLocation))) {
                return i+1;
            }
        }
    }


}
