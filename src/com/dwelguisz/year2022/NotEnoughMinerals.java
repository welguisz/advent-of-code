package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class NotEnoughMinerals extends AoCDay {

    private Integer maxGeodes;
    public static class Blueprint {
        public Integer id;
        public Integer[][] costs;
        public Integer maxOreCost;

        public Blueprint(Integer id, Integer[] oreRobotCost, Integer[] clayRobotCost, Integer[] obsidianRobotCost, Integer[] geodeRobotCost) {
            this.id = id;
            costs = new Integer[][]{oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost};
            this.maxOreCost = Integer.max(Integer.max(costs[0][0], costs[1][0]), Integer.max(costs[2][0], costs[3][0]));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) {
                return false;
            }
            Blueprint other = (Blueprint)o;
            return (id == other.id) && (costs.equals(other.costs));
        }

        @Override
        public int hashCode(){
            return Objects.hash(id, costs);
        }

    }
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,19,false,0);
        List<Blueprint> parsedClass = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(parsedClass);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(parsedClass);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Blueprint> parseLines(List<String> lines) {
        List<Blueprint> values = new ArrayList<>();
        for (String l : lines) {
            String words[] = l.split(" ");
            Integer id = Integer.parseInt(words[1].substring(0, words[1].length()-1));
            Integer[] oreRobotCost = new Integer[]{Integer.parseInt(words[6]),0,0};
            Integer[] clayRobotCost = new Integer[]{Integer.parseInt(words[12]),0,0};
            Integer[] obsidianCost = new Integer[]{Integer.parseInt(words[18]), Integer.parseInt(words[21]),0};
            Integer[] geodeCost = new Integer[]{Integer.parseInt(words[27]), 0, Integer.parseInt(words[30])};
            values.add(new Blueprint(id, oreRobotCost, clayRobotCost, obsidianCost, geodeCost));
        }
        return values;
    }

    Integer solutionPart1(List<Blueprint> values) {
        Integer sum = 0;
        for (Blueprint b : values) {
            maxGeodes = 0;
            Integer geodes = findMaxGeodes(b, new BlueprintState(new Integer[]{0,0,0,0}, new Integer[]{1,0,0,0},24));
            sum += b.id * geodes;
        }
        return sum;
    }

    Integer solutionPart2(List<Blueprint> values) {
        List<Blueprint> notEatenBlueprints = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            notEatenBlueprints.add(values.get(i));
        }
        Integer value = 1;
        for (Blueprint b : notEatenBlueprints) {
            Integer geodes = findMaxGeodes(b, new BlueprintState(new Integer[]{0,0,0,0}, new Integer[]{1,0,0,0},32));
            value *= geodes;
        }
        return value;
    }


    public static class BlueprintState {
        final Integer[] inventory;
        final Integer[] robots;
        final Integer timeLeft;
        private int hashCode;
        public BlueprintState(Integer[] inventory, Integer[] robots, Integer timeLeft) {
            this.inventory = inventory;
            this.robots = robots;
            this.timeLeft = timeLeft;
            this.hashCode = Objects.hash(inventory, robots, timeLeft);
        }

        public String getName() {
            String invent = Arrays.stream(inventory).map(i -> ""+i).collect(Collectors.joining(","));
            String robot = Arrays.stream(robots).map(i -> ""+i).collect(Collectors.joining(","));
            return "time:"+timeLeft+",inventory:["+invent+"],robots:["+robot+"],maxPossibleGeodes:"+maxPossible();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BlueprintState other = (BlueprintState) o;
            return this.inventory.equals(other.inventory) && this.robots.equals(other.robots) && this.timeLeft.equals(other.timeLeft);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        private Integer maxPossible() {
            return (timeLeft * (timeLeft - 1) / 2) + (robots[3] * timeLeft) + inventory[3];
        }

        @Override
        public String toString() {
            return getName();
        }

    }
    public Integer findMaxGeodes(Blueprint blueprint, BlueprintState state) {
        PriorityQueue<BlueprintState> queue = new PriorityQueue<>(1000000, (a,b) -> {
            int diffPossible = b.maxPossible() - a.maxPossible();
            if (diffPossible == 0) {
                return b.timeLeft - a.timeLeft;
            }
            return diffPossible;
        });
        Set<String> seen = new HashSet<>();
        Set<String> inQueue = new HashSet<>();
        inQueue.add(state.getName());
        queue.add(state);
        Integer maxGeodes = Integer.MIN_VALUE;
        while (!queue.isEmpty()) {
            BlueprintState current = queue.poll();
            inQueue.remove(current.getName());
            maxGeodes = Integer.max(maxGeodes,current.inventory[3]);
            if (current.timeLeft == 0) {
                continue;
            }
            BlueprintState lookInFuture = maxPossibleOres(blueprint, current.inventory, current.robots, current.timeLeft);

            if (seen.contains(lookInFuture.getName())) {
                continue;
            }
            seen.add(current.getName());
            List<BlueprintState> nextStates = createNextStates(blueprint, lookInFuture);
            for (BlueprintState next : nextStates) {
                if (inQueue.add(next.getName())) {
                    queue.add(next);
                }
            }
        }
        return maxGeodes;
    }

    private BlueprintState maxPossibleOres(Blueprint bp, Integer[] inventory, Integer[] robots, Integer time) {
        Integer[] nextInventory = inventory.clone();
        Integer[] nextRobots = robots.clone();
        if (nextRobots[0] >= bp.maxOreCost) {
            nextRobots[0] = bp.maxOreCost;
        }
        for (int i = 1; i < 3; i++) {
            if (nextRobots[i] >= bp.costs[i + 1][i]) {
                nextRobots[i] = bp.costs[i + 1][i];
            }
        }
        Integer tmp = time * bp.maxOreCost - nextRobots[0]*(time-1);
        if (nextInventory[0] >= tmp) {
            nextInventory[0] = tmp;
        }
        for (int i = 1; i < 3; i++) {
            tmp = time * bp.costs[i+1][i] - nextRobots[i] * (time - 1);
            if (nextInventory[i] >= tmp) {
                nextInventory[i] = tmp;
            }
        }
        return new BlueprintState(nextInventory, nextRobots, time);
    }

    private List<BlueprintState> createNextStates(Blueprint blueprint, BlueprintState current) {
        List<BlueprintState> nextState = new ArrayList<>();
        Integer[] nextInv = nextInventory(current.inventory, current.robots);
        nextState.add(new BlueprintState(nextInv, current.robots.clone(), current.timeLeft-1));
        for (int i = 0; i < 4; i++) {
            if (canBuyRobot(current.inventory, blueprint.costs[i])) {
                nextState.add(new BlueprintState(boughtRobot(nextInv, blueprint, i), incRobot(current.robots, i), current.timeLeft - 1));
            }
        }
        return nextState;
    }

    public Boolean canBuyRobot(Integer[] inventory, Integer[] costs) {
        for (int i = 0; i < 3; i++) {
            if (costs[i] == 0) {
                continue;
            }
            if (costs[i] > inventory[i]) {
                return false;
            }
        }
        return true;
    }

    public Integer[] boughtRobot(Integer[] currentInventory, Blueprint blueprint, int robotNo) {
        Integer[] newInventory = currentInventory.clone();
        Integer[] decrement = blueprint.costs[robotNo];
        for (int i = 0; i < 3; i++) {
            newInventory[i] -= decrement[i];
        }
        return newInventory;
    }

    public Integer[] incRobot(Integer[] currentRobot, int robotNo) {
        Integer[] newRobot = currentRobot.clone();
        newRobot[robotNo]++;
        return newRobot;
    }

    private Integer[] nextInventory(Integer[] inventory, Integer[] robots) {
        Integer[] nextInv = inventory.clone();
        for (int i = 0; i < 4; i ++) {
            nextInv[i] += robots[i];
        }
        return nextInv;
    }
}
