package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class RadioisotepeThermoelectricGenerators extends AoCDay {

    public static class State {
        Integer distance;
        Integer stepNo;
        Integer elevator;
        List<List<String>> floors;
        Long timestamp;

        public State() {
            distance = 0;
            stepNo = 0;
            elevator = 0;
            floors = new ArrayList<>();
            timestamp = Instant.now().toEpochMilli();
        }

        public boolean isEquivalent(State other) {
            for(int i = 0; i < 4; i++) {
                List<String> thisFloor = this.floors.get(i);
                List<String> otherFloor = other.floors.get(i);
                Pair<Integer, Integer> thisFloorSetup = numberOfPairsAndNonPairs(thisFloor);
                Pair<Integer, Integer> otherFloorSetup = numberOfPairsAndNonPairs(otherFloor);
                if ( (!thisFloorSetup.getRight().equals(otherFloorSetup.getRight())) ||(!thisFloorSetup.getLeft().equals(otherFloorSetup.getLeft()))) {
                    return false;
                }
            }
            return this.elevator == other.elevator;
        }

        public boolean isEqual(State other) {
            for(int i = 0; i < 4; i++) {
                List<String> thisFloor = this.floors.get(i);
                List<String> otherFloor = other.floors.get(i);
                List<String> diffs = thisFloor.stream().filter(t -> !otherFloor.contains(t)).collect(Collectors.toList());
                List<String> diffs1 = otherFloor.stream().filter(t -> !thisFloor.contains(t)).collect(Collectors.toList());
                if (!diffs.isEmpty() || !diffs1.isEmpty()) {
                    return false;
                }
            }
            return this.elevator == other.elevator;
        }

        public boolean isValidState() {
            if (floors.get(elevator).isEmpty()) {
                return false;
            }
            for (List<String> floor : floors) {
                List<String> generators = floor.stream().filter(s -> s.substring(1).equals("G")).map(s -> s.substring(0,1)).collect(Collectors.toList());
                List<String> microchips = floor.stream().filter(s -> s.substring(1).equals("M")).map(s -> s.substring(0,1)).collect(Collectors.toList());
                if (!validFloor(generators, microchips)) {
                    return false;
                }
            }
            return true;
        }

        public boolean validFloor(List<String> generators, List<String> microchips) {
            if (!generators.isEmpty() && microchips.isEmpty()) {
                return true;
            }
            if (generators.isEmpty() && !microchips.isEmpty()) {
                return true;
            }
            for (String m : microchips) {
                if (!generators.contains(m)) {
                    return false;
                }
            }
            return true;
        }

        public boolean notInPreviousStates(List<State> previousStates, State newState) {
            for (State ps : previousStates) {
                if (ps.isEquivalent(newState)) {
                    return false;
                }
            }
            return true;
        }

        public List<State> getNextStates(List<State> previousStates) {
            List<List<String>> combinations = moveCombinations();
            List<State> nextStates = new ArrayList<>();
            for (List<String> combination : combinations) {
                if (elevator > 0) {
                    State newState = moveToNextState(elevator-1, combination);
                    if ((newState.isValidState()) && (notInPreviousStates(previousStates, newState))) {
                        previousStates.add(newState);
                        nextStates.add(newState);
                    }
                }
                if (elevator < 3) {
                    State newState = moveToNextState(elevator+1, combination);
                    if ((newState.isValidState()) && (notInPreviousStates(previousStates, newState))) {
                        previousStates.add(newState);
                        nextStates.add(newState);
                    }
                }
            }
            return nextStates;
        }

        public State moveToNextState(Integer nextFloor, List<String> load) {
            State newState = new State();
            newState.stepNo = stepNo + 1;
            newState.elevator = nextFloor;
            for (List<String> floor : floors) {
                List<String> nf = new ArrayList<>(floor);
                newState.floors.add(nf);
            }
            List<String> cur_floor = newState.floors.get(elevator)
                    .stream()
                    .filter(s -> !load.contains(s))
                    .collect(Collectors.toList());
            List<String> next_floor = new ArrayList<>(newState.floors.get(newState.elevator));
            next_floor.addAll(load);
            newState.updateFloor(elevator, cur_floor);
            newState.updateFloor(newState.elevator, next_floor);
            newState.calculateDistance();
            return newState;
        }

        public void updateFloor(int floorNumber, List<String> values) {
            int currentFloor = 0;
            List<List<String>> newFloors = new ArrayList<>();
            for (List<String> floor : floors) {
                if (currentFloor == floorNumber) {
                    newFloors.add(values);
                } else {
                    newFloors.add(floor);
                }
                currentFloor++;
            }
            this.floors = newFloors;
        }

        private List<List<String>> moveCombinations() {
            List<String> currentFloor = floors.get(elevator);
            List<List<String>> possibleMoves = new ArrayList<>();
            for (int i = 1; i < 3; i++) {
                possibleMoves.addAll(combinations(currentFloor,i));
            }
            return possibleMoves;
        }

        public List<List<String>> combinations(List<String> inputSet, int k) {
            List<List<String>> results = new ArrayList<>();
            combinationsInternal(inputSet, k, results, new ArrayList<>(), 0);
            return results;
        }

        public void combinationsInternal(List<String> inputSet, int k, List<List<String>> results, ArrayList<String> accumulator, int index) {
            int needToAccumulate = k - accumulator.size();
            int canAccumulate = inputSet.size() - index;
            if (accumulator.size() == k) {
                results.add(new ArrayList<>(accumulator));
            } else if (needToAccumulate <= canAccumulate) {
                combinationsInternal(inputSet, k, results, accumulator, index + 1);
                accumulator.add(inputSet.get(index));
                combinationsInternal(inputSet,k,results,accumulator,index+1);
                accumulator.remove(accumulator.size()-1);
            }
        }


        public void calculateDistance() {
            distance  = 3 - elevator;
            Integer index = 0;
            for(List<String> floor : floors) {
                Pair<Integer, Integer> values = numberOfPairsAndNonPairs(floor);
                Map<String, Integer> diffMap = findDistanceToPair(index);
                Integer v = diffMap.entrySet().stream().map(e -> e.getValue()).reduce(0, (a,b)->a+b);
                distance += (3 - index) * ((values.getLeft() * 4) + (values.getRight() * 6));
                index++;
            }
        }

        public Map<String, Integer> findDistanceToPair(int index) {
            List<String> floor = floors.get(index);
            List<String> generators = floor.stream().filter(s -> s.substring(1).equals("G")).map(s -> s.substring(0,1)).collect(Collectors.toList());
            List<String> microchips = floor.stream().filter(s -> s.substring(1).equals("M")).map(s -> s.substring(0,1)).collect(Collectors.toList());
            if (microchips.isEmpty()) {
                return Map.of("ignore",0);
            }
            List<String> diffs1 = microchips.stream().filter(m -> !generators.contains(m)).map(e -> e + "G").collect(Collectors.toList());
            int floorIndex = 0;
            Map<String, Integer> diffMap = new HashMap<>();
            for (List<String> compareFloor : floors) {
                if (index == floorIndex) {
                    continue;
                }
                List<String> check = compareFloor.stream().filter(c -> diffs1.contains(c)).collect(Collectors.toList());
                int diff = Math.abs(floorIndex - index);
                for (String c : check) {
                    diffMap.put(c,diff);
                }
                floorIndex++;
            }
            return diffMap;
        }

        private Pair<Integer, Integer> numberOfPairsAndNonPairs(List<String> floor) {
            List<String> generators = floor.stream().filter(s -> s.substring(1).equals("G")).map(s -> s.substring(0,1)).collect(Collectors.toList());
            List<String> microchips = floor.stream().filter(s -> s.substring(1).equals("M")).map(s -> s.substring(0,1)).collect(Collectors.toList());
            if (generators.isEmpty() || microchips.isEmpty()) {
                return Pair.of(0, microchips.size() + generators.size());
            }
            //Since generators and microchips have at least 1 and we know that this is a valid state, we can
            //assume that microchips.size() <= generators.size()
            return Pair.of(microchips.size(), generators.size() - microchips.size());


        }
    }


    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day11/input.txt");
        Integer part1 = solutionPart1(lines, new ArrayList<>());
        System.out.println(String.format("Part 1 Answer: %d",part1));
        List <String> extraItems = new ArrayList<>();
        extraItems.add("EG");
        extraItems.add("EM");
        extraItems.add("DG");
        extraItems.add("DM");
        //Integer part2 = solutionPart1(lines, extraItems);
        //System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(List<String> lines, List<String> extraItems) {
        List<List<String>> initialFloors = setupInitialState(lines);

        List<State> previousStates = new ArrayList<>();
        State initialState = new State();
        initialState.elevator = 0;
        initialState.floors.addAll(initialFloors);
        List<String> newFloor = new ArrayList<>(initialFloors.get(0));
        newFloor.addAll(extraItems);
        initialState.updateFloor(0, newFloor);
        initialState.calculateDistance();
        previousStates.add(initialState);
        PriorityQueue<Pair<Integer, State>> states = new PriorityQueue<>(500,
                (a,b) -> {
                    int value = a.getLeft() - b.getLeft();
                    if (value == 0) {
                        return (int) (a.getRight().timestamp - b.getRight().timestamp);
                    }
                    return value;
                }
                );
        states.add(Pair.of(initialState.distance, initialState));
        while (!states.isEmpty()) {
            Pair<Integer, State> temp = states.poll();
            List<State> newStates = temp.getRight().getNextStates(previousStates);
            for (State newState : newStates) {
                if (newState.distance.equals(0)) {
                    return newState.stepNo;
                }
                states.add(Pair.of(newState.distance, newState));
            }
        }
        return -1;
    }

    public List<List<String>> setupInitialState(List<String> lines) {
        List<List<String>> currentState = new ArrayList<>();
        for (String line : lines) {
            List<String> onThisFloor = new ArrayList<>();
            String lineSplit[] = line.split(" ");
            Integer i = 0;
            for (String ls : lineSplit) {
                if (ls.contains("microchip")) {
                    String element = lineSplit[i-1].substring(0,1);
                    String mc = element.toUpperCase()+"M";
                    onThisFloor.add(mc);
                } else if (ls.contains("generator")) {
                    String element = lineSplit[i-1].substring(0,1);
                    String gen = element.toUpperCase()+"G";
                    onThisFloor.add(gen);
                }
                i++;
            }
            currentState.add(onThisFloor);
        }
        return currentState;
    }
}
