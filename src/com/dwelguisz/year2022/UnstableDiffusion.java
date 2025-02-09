package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnstableDiffusion extends AoCDay {
    public static Coord2D NORTH = new Coord2D(-1,0);
    public static Coord2D NORTHEAST = new Coord2D(-1,1);
    public static Coord2D EAST = new Coord2D(0,1);
    public static Coord2D SOUTHEAST = new Coord2D(1,1);
    public static Coord2D SOUTH = new Coord2D(1,0);
    public static Coord2D SOUTHWEST = new Coord2D(1,-1);
    public static Coord2D WEST = new Coord2D(0,-1);
    public static Coord2D NORTHWEST = new Coord2D(-1,-1);

    List<Coord2D> NEIGHBORS = List.of(NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST);
    List<Integer> CHECK_NORTH = List.of(0,1,7);
    List<Integer> CHECK_SOUTH = List.of(4,3,5);
    List<Integer> CHECK_WEST = List.of(6,5,7);
    List<Integer> CHECK_EAST = List.of(2,1,3);
    public class Elf {
        Integer id;
        Coord2D position;
        List<List<Integer>> proposeCheck;
        public Elf(Integer id, Coord2D position) {
            this.id = id;
            this.position = position;
            this.proposeCheck = new ArrayList<>();
            proposeCheck.add(CHECK_NORTH);
            proposeCheck.add(CHECK_SOUTH);
            proposeCheck.add(CHECK_WEST);
            proposeCheck.add(CHECK_EAST);
        }

        public Coord2D propose(Map<Coord2D, Elf> elves) {
            List<Coord2D> myNeighbors = NEIGHBORS.stream()
                    .map(n -> position.add(n)).collect(Collectors.toList());
            Boolean noElvesNearMe = myNeighbors.stream().noneMatch(n -> elves.containsKey(n));
            if (noElvesNearMe) {
                return null;
            }
            List<Coord2D> proposeMoved = proposeCheck.stream()
                    .filter(p -> p.stream().map(i -> myNeighbors.get(i)).noneMatch(n -> elves.containsKey(n)))
                    .map(n -> position.add(NEIGHBORS.get(n.get(0))))
                    .collect(Collectors.toList());
            if (proposeMoved.isEmpty()) {
                return null;
            }
            return proposeMoved.get(0);
        }

        public void updateLocation(Coord2D newPosition) {
            this.position = newPosition;
        }

        public void updateProposeCheck() {
            List<Integer> check = proposeCheck.remove(0);
            proposeCheck.add(check);
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,23,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(parsedLines(lines));;
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(parsedLines(lines));;
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Map<Coord2D, Elf> parsedLines(List<String> lines) {
        Integer maxHeight = lines.size();
        Integer maxWidth = lines.stream().mapToInt(l -> l.length()).max().getAsInt();
        Map<Coord2D, Elf> elves = new HashMap<>();
        Integer id = 1;
        for (int y = 0; y < maxHeight; y++) {
            for (int x = 0; x < maxWidth; x++) {
                if (lines.get(y).charAt(x) == '#') {
                    elves.put(new Coord2D(y,x), new Elf(id, new Coord2D(y,x)));
                    id++;
                }
            }
        }
        return elves;
    }

    Integer solutionPart1(Map<Coord2D, Elf> elves) {
        return simulate(elves, 10, false);
    }

    Integer solutionPart2(Map<Coord2D, Elf> elves) {
        return simulate(elves, Integer.MAX_VALUE, true);
    }

    public Integer simulate(Map<Coord2D, Elf> elves, Integer maxSteps, Boolean part2) {
        Boolean elfMoved = true;
        Integer steps = 0;
        while(elfMoved && steps < maxSteps) {
            Map<Coord2D, List<Elf>> proposedMoves = new HashMap<>();
            for(Map.Entry<Coord2D,Elf> elf : elves.entrySet()) {
                Coord2D nextMove = elf.getValue().propose(elves);
                if (nextMove == null) {
                    continue;
                }
                List<Elf> elfProposing = proposedMoves.getOrDefault(nextMove, new ArrayList<>());
                elfProposing.add(elf.getValue());
                proposedMoves.put(nextMove, elfProposing);
            }
            elfMoved = !proposedMoves.isEmpty();
            for (Map.Entry<Coord2D, List<Elf>> elvesProposing : proposedMoves.entrySet()) {
                if (elvesProposing.getValue().size() != 1) {
                    continue;
                }
                Elf updateElf = elvesProposing.getValue().get(0);
                Coord2D newLoc = elvesProposing.getKey();
                elves.remove(updateElf.position);
                updateElf.updateLocation(newLoc);
                elves.put(newLoc, updateElf);
            }
            for (Map.Entry<Coord2D,Elf> elf : elves.entrySet()) {
                elf.getValue().updateProposeCheck();
            }
            steps++;
        }
        if (part2) {
            return steps;
        }
        Integer minX = elves.entrySet().stream().mapToInt(e -> e.getKey().x).min().getAsInt();
        Integer maxX = elves.entrySet().stream().mapToInt(e -> e.getKey().x).max().getAsInt() + 1;
        Integer minY = elves.entrySet().stream().mapToInt(e -> e.getKey().y).min().getAsInt();
        Integer maxY = elves.entrySet().stream().mapToInt(e -> e.getKey().y).max().getAsInt() + 1;
        return (maxX - minX) * (maxY - minY) - elves.size();
    }
}
