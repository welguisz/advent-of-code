package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BeverageBandits extends AoCDay {

    public static Coord2D TOP = new Coord2D(-1,0);
    public static Coord2D LEFT = new Coord2D(0,-1);
    public static Coord2D RIGHT = new Coord2D(0,1);
    public static Coord2D BOTTOM = new Coord2D(1,0);
    public static List<Coord2D> STEP_ORDER = List.of(TOP, LEFT, RIGHT, BOTTOM);
    public static class Fighter {
        Coord2D location;
        Integer hitPoints;
        Boolean type;

        public Integer attackPoints;

        public Fighter(Coord2D location, Boolean type) {
            this(location, type, 3);
        }

        public Fighter(Coord2D location, Boolean type, Integer attackPoints) {
            this.location = location;
            this.type = type;
            this.hitPoints = 200;
            this.attackPoints = attackPoints;
        }

        public List<Coord2D> getNeighbors() {
            return STEP_ORDER.stream().map(p -> location.add(p)).collect(Collectors.toList());
        }
        public void reduceHitPoints(Integer amount) {
            this.hitPoints -= amount;
        }

    }
    Set<Coord2D> walls;
    Map<Coord2D, Fighter> goblins;
    Map<Coord2D, Fighter> elves;
    Set<Coord2D> empty;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,15,false,0);
        parseLines(lines, 3);
        String[][] grid = convertToGrid(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public void parseLines(List<String> lines, Integer elfAttackPower) {
        Integer y = 0;
        walls = new HashSet<>();
        goblins = new HashMap<>();
        elves = new HashMap<>();
        empty = new HashSet<>();
        for (String l : lines) {
            String t[]= l.split("");
            for (int x = 0; x < t.length; x++) {
                if (t[x].equals("#")) {
                    walls.add(new Coord2D(y,x));
                } else if (t[x].equals("G")) {
                    goblins.put(new Coord2D(y,x), new Fighter(new Coord2D(y,x), false));
                } else if (t[x].equals("E")) {
                    elves.put(new Coord2D(y,x), new Fighter(new Coord2D(y,x), true, elfAttackPower));
                } else {
                    empty.add(new Coord2D(y,x));
                }
            }
            y++;
        }
    }

    public boolean isTargetSpace(Fighter fighter) {
        Map<Coord2D, Fighter> otherTeam = fighter.type ? goblins : elves;
        return fighter.getNeighbors().stream().anyMatch(n -> otherTeam.containsKey(n));
    }

    public List<Coord2D> getNeighbors(Coord2D pos) {
        return STEP_ORDER.stream().map(p -> pos.add(p)).collect(Collectors.toList());
    }

    public boolean isTargetSpace(Coord2D pos, Boolean team) {
        Map<Coord2D, Fighter> otherTeam = team ? goblins : elves;
        return getNeighbors(pos).stream().anyMatch(n -> otherTeam.containsKey(n));
    }

    public static class FighterMovement {
        Coord2D pos;
        Coord2D nextStep;
        public Integer distance;
        private int hashCode;
        public FighterMovement(Coord2D pos, Coord2D nextStep, Integer distance) {
            this.pos = pos;
            this.nextStep = nextStep;
            this.distance = distance;
            this.hashCode = Objects.hash(pos,nextStep,distance);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            FighterMovement other = (FighterMovement) o;
            return (this.pos.equals(other.pos) && (this.nextStep.equals(other.nextStep)) && (this.distance.equals(other.distance)));

        }
    }
    public Coord2D getStep(Coord2D pos) {
        Boolean team = elves.containsKey(pos);
        Fighter fighter = team ? elves.get(pos) : goblins.get(pos);
        if (isTargetSpace(fighter)) {
            return pos;
        }
        Set<Coord2D> seen = new HashSet<>();
        ArrayDeque<FighterMovement> movements = new ArrayDeque<>();
        for (Coord2D neighbor : fighter.getNeighbors()) {
            if (empty.contains(neighbor)) {
                movements.add(new FighterMovement(neighbor, neighbor, 1));
            }
        }
        while (!movements.isEmpty()) {
            FighterMovement movement = movements.pollFirst();
            if (seen.contains(movement.pos)) {
                continue;
            }
            seen.add(movement.pos);
            if (isTargetSpace(movement.pos, team)) {
                return movement.nextStep;
            }
            for (Coord2D next : getNeighbors(movement.pos)) {
                if (empty.contains(next) && !seen.contains(next)) {
                    movements.add(new FighterMovement(next, movement.nextStep, movement.distance+1));
                }
            }
        }
        return pos;
    }

    public Fighter getOpponent(Fighter fighter) {
        Map<Coord2D, Fighter> opponents = fighter.type ? goblins : elves;
        List<Fighter> fighters = fighter.getNeighbors().stream().filter(n -> opponents.containsKey(n)).map(n -> opponents.get(n)).collect(Collectors.toList());
        if (fighters.isEmpty()) {
            return null;
        }
        Integer minHip = fighters.stream().mapToInt(f -> f.hitPoints).min().getAsInt();
        List<Fighter> minHPFighters = fighters.stream().filter(f -> f.hitPoints.equals(minHip)).collect(Collectors.toList());
        return minHPFighters.get(0);
    }
    public Pair<Integer, Integer> fight(String[][] grid, Boolean part2) {
        Integer round = 0;
        while (!elves.isEmpty() && !goblins.isEmpty()) {
            Set<Fighter> alreadyMoved = new HashSet<>();
            for (Integer y = 0; y < grid.length; y++) {
                for (Integer x = 0; x < grid[y].length; x++) {
                    Coord2D position = new Coord2D(y,x);
                    if (walls.contains(position) || empty.contains(position)) {
                        continue;
                    } else if (elves.containsKey(position) && alreadyMoved.contains(elves.get(position))) {
                        continue;
                    } else if (goblins.containsKey(position) && alreadyMoved.contains(goblins.get(position))) {
                        continue;
                    }
                    if (elves.isEmpty() || goblins.isEmpty()) {
                        Integer score = elves.isEmpty() ? goblins.entrySet().stream().mapToInt(g -> g.getValue().hitPoints).sum() :
                                elves.entrySet().stream().mapToInt(e -> e.getValue().hitPoints).sum();
                        return Pair.of(round, score);
                    }
                    Fighter fighter = elves.containsKey(position) ? elves.get(position) : goblins.get(position);
                    alreadyMoved.add(fighter);
                    Coord2D nextStep = getStep(position);
                    fighter.location = nextStep;
                    if (fighter.type) {
                        if (!position.equals(nextStep)) {
                            elves.remove(position);
                            elves.put(nextStep, fighter);
                            empty.remove(nextStep);
                            empty.add(position);
                        }
                    } else {
                        if (!position.equals(nextStep)) {
                            goblins.remove(position);
                            goblins.put(nextStep, fighter);
                            empty.remove(nextStep);
                            empty.add(position);
                        }
                    }
                    Fighter opponent = getOpponent(fighter);
                    if (opponent != null && opponent.hitPoints >= 0) {
                        opponent.reduceHitPoints(fighter.attackPoints);
                        if (opponent.hitPoints <= 0) {
                            if (opponent.type) {
                                if (part2) {
                                    return Pair.of(-1,-1);
                                }
                                elves.remove(opponent.location);
                                empty.add(opponent.location);
                            } else {
                                goblins.remove(opponent.location);
                                empty.add(opponent.location);
                            }
                        }
                    }
                }
            }
            round++;
        }
        Integer score = elves.isEmpty() ? goblins.entrySet().stream().mapToInt(g -> g.getValue().hitPoints).sum() :
                elves.entrySet().stream().mapToInt(e -> e.getValue().hitPoints).sum();
        printMap(elves, goblins, empty);
        System.out.println(String.format("Round %d, Score: %d",round, score));
        return Pair.of(round, score);
    }
    public Integer solutionPart1(String[][] grid) {
        Pair<Integer, Integer> results = fight(grid, false);
        return results.getLeft() * results.getRight();
    }

    public Integer solutionPart2(List<String> lines) {
        Integer elfAttackPower = 3;
        while(true) {
            parseLines(lines, elfAttackPower);
            String[][] grid = convertToGrid(lines);
            elfAttackPower++;
            Pair<Integer, Integer> results = fight(grid, true);
            if (results.getLeft() > 0) {
                System.out.println("Elf Attack Power: " + elfAttackPower);
                return results.getLeft() * results.getRight();
            }
        }
    }

    public void printMap(Map<Coord2D, Fighter> elves, Map<Coord2D, Fighter> goblins, Set<Coord2D> empty) {
        Integer minX = walls.stream().mapToInt(p -> p.y).min().getAsInt();
        Integer minY = walls.stream().mapToInt(p -> p.x).min().getAsInt();
        Integer maxX = walls.stream().mapToInt(p -> p.y).max().getAsInt();
        Integer maxY = walls.stream().mapToInt(p -> p.x).max().getAsInt();
        for(Integer y = minY; y <= maxY; y++) {
            List<Fighter> fighters = new ArrayList<>();
            for (Integer x = minX; x <= maxX; x++) {
                Coord2D pos = new Coord2D(y,x);
                if (walls.contains(pos)) {
                    System.out.print("#");
                } else if (empty.contains(pos)) {
                    System.out.print(".");
                } else if (elves.containsKey(pos)) {
                    System.out.print("E");
                    fighters.add(elves.get(pos));
                } else if (goblins.containsKey(pos)) {
                    System.out.print("G");
                    fighters.add(goblins.get(pos));
                }
            }
            if (!fighters.isEmpty()) {
                System.out.print("  ");
            }
            for (Fighter f : fighters) {
                String type = f.type ? "E" : "G";
                System.out.print(String.format("%s: %d; ",type, f.hitPoints));
            }
            System.out.println();
        }
    }

}
